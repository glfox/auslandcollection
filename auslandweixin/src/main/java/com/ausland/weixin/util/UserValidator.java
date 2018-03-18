package com.ausland.weixin.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ausland.weixin.dao.UserRepository;
import com.ausland.weixin.model.db.User;
import com.ausland.weixin.model.reqres.UserForm;

@Component
public class UserValidator implements Validator {
    //@Autowired
    //private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserForm userForm = (UserForm) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "用户名不能为空","用户名不能为空");
        if (userForm.getUsername().length() < 4 || userForm.getUsername().length() > 20) {
            errors.rejectValue("username", "用户名长度必须在4和20之间","用户名长度必须在4和20之间");
        }
        if (userRepository.findByUsername(userForm.getUsername()) != null) {
            errors.rejectValue("username", "该用户名已经存在", "该用户名已经存在");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "密码不能为空","密码不能为空");
        if (userForm.getPassword().length() < 8 || userForm.getPassword().length() > 32) {
            errors.rejectValue("password", "密码长度必须在8和20之间", "密码长度必须在8和20之间");
        }

        if (!userForm.getPasswordConfirm().equals(userForm.getPassword())) {
            errors.rejectValue("passwordConfirm", "确认密码和密码必须完全一致","确认密码和密码必须完全一致");
        }
    }
}
