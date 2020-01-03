package com.wenjun.recsys.response;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @Author: wenjun
 * @Date: 2020/1/3 14:19
 */
public class CommonUtil {
    public static String processErrorString(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            buffer.append(fieldError.getDefaultMessage()).append(",");
        }
        return buffer.substring(0,buffer.length()-1);
    }
}
