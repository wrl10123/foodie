package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息相关接口", tags = {"用户信息详情api"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;
    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "修改用户信息")
    @PostMapping("update")
    public IMOOCJSONResult update(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return IMOOCJSONResult.errorMap(errors);
        }
        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        setNullPreperty(users);

        //设置cookie
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(users), true);

        //todo 后续要改，增加令牌token，整合redis

        return IMOOCJSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            //发送验证错误对应的某一个属性
            String errorField = error.getField();
            //验证错误的信息
            String defaultMessage = error.getDefaultMessage();
            errorMap.put(errorField, defaultMessage);
        }
        return errorMap;
    }

    private void setNullPreperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
    }

    @ApiOperation(value = "用户头像修改")
    @PostMapping("uploadFace")
    public IMOOCJSONResult uploadFace(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file) {
        if (file == null) {
            return IMOOCJSONResult.errorMsg("文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            return IMOOCJSONResult.errorMsg("文件名字不能为空");
        }

        System.out.println(file.getSize());

        //开始组装完整的文件路径。
        //1.分割文件名
        String[] fileNameArr = fileName.split("\\.");
        //2.拿到后缀
        String suffix = fileNameArr[fileNameArr.length - 1];
        //验证图片后缀
        if (validSuffix(suffix)) {
            return IMOOCJSONResult.errorMsg("图片格式不正确");
        }
        //3.组装文件名格式： face-{userId}.jpg。覆盖式文件名，
        String newFileName = "face-" + userId + "." + suffix;
        //完整的文件路径： 本地路径+用户id文件夹+新的文件名
        String finalFilePath = fileUpload.getImageUserFaceLocation()
                + File.separator + userId       //在路径上为每一个用户增加userId，用于区分不同的用户上传
                + File.separator + newFileName;
        //开始上传文件
        uploadFileInfo(file, finalFilePath);

        //web服务用来访问头像文件的真实地址
        String imageServerUrl = fileUpload.getImageServerUrl()
                + userId + File.separator + newFileName;

        //由于浏览器可能存在缓存，所以这里要添加一个时间戳
        imageServerUrl += "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        Users users = centerUserService.updateUserFace(userId, imageServerUrl);
        setNullPreperty(users);

        //设置cookie
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(users), true);

        //todo 后续要改，增加令牌token，整合redis
        return IMOOCJSONResult.ok();
    }

    private boolean validSuffix(String suffix) {
        if ("jpg".equalsIgnoreCase(suffix)
                || "jpeg".equalsIgnoreCase(suffix)
                || "png".equalsIgnoreCase(suffix)) {
            return true;
        }
        return false;
    }


    /**
     * 上传文件
     *
     * @param file
     * @param finalFilePath
     */
    private void uploadFileInfo(MultipartFile file, String finalFilePath) {
        File outFile = new File(finalFilePath);
        //判断路径是否存在
        if (outFile.getParent() != null) {
            //创建文件夹，递归生成
            outFile.getParentFile().mkdirs();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(outFile);
             InputStream inputStream = file.getInputStream();) {
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
