package com.imooc.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@ApiModel(value = "用户对象")
public class CenterUserBO {

    private String password;
    private String confirmPassword;
    private String nickname;
    private String realname;
    @Length(max = 12, message = "用户名不能超过12位")
    private String username;
    //正则表达式
//    @Pattern(regexp = "")
    private String mobile;
    @Email
    private String email;
    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    private Integer sex;
    private Date birthday;
}
