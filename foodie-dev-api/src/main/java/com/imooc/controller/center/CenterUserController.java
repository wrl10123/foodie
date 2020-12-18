package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
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

        //拿到新的文件名
        String newFileName = getNewFileName(fileName, userId);
        if (ERROR_IMG_FILE.equals(newFileName)) {
            return IMOOCJSONResult.errorMsg(ERROR_IMG_FILE);
        }

        //（1） 本地的完整的文件路径： 本地路径+用户id文件夹+新的文件名
        String finalFilePath = fileUpload.getImageUserFaceLocation()
                + File.separator + userId       //在路径上为每一个用户增加userId，用于区分不同的用户上传
                + File.separator + newFileName;
        //开始上传文件
        uploadFileInfo(file, finalFilePath);

        //（2） web服务用来访问头像文件的真实地址： web服务地址+用户id文件夹+新的文件名
        String imageServerUrl = fileUpload.getImageServerUrl()
                + File.separator + userId
                + File.separator + newFileName;
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

    /**
     * 拿到一个新的图片文件名字
     *
     * @param fileName
     * @param userId
     * @return
     */
    private String getNewFileName(String fileName, String userId) {
        //开始组装完整的文件路径。
        //1.分割文件名
        String[] fileNameArr = fileName.split("\\.");
        //2.拿到后缀
        String suffix = fileNameArr[fileNameArr.length - 1];
        //验证图片后缀
        if (!validSuffix(suffix)) {
            return ERROR_IMG_FILE;
        }
        //3.组装文件名格式： face-{userId}.jpg。覆盖式文件名，
        return "face-" + userId + "." + suffix;
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

    @ApiOperation(value = "修改用户密码", httpMethod = "POST")
    @PostMapping("updatePassword")
    public IMOOCJSONResult updatePassword(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "oldPassword", value = "用户旧密码", required = true)
            @RequestParam String oldPassword,
            @ApiParam(name = "newPassword", value = "用户新密码", required = true)
            @RequestParam String newPassword) {
        Boolean status = centerUserService.updateUserPassword(userId, oldPassword, newPassword);
        if (status) {
            return IMOOCJSONResult.ok();
        }
        return IMOOCJSONResult.errorMsg("修改失败");
    }
}
