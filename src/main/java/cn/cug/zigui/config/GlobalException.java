package cn.cug.zigui.config;


import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.dev33.satoken.exception.NotLoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalException {

    @ModelAttribute
    public void get(HttpServletRequest request) throws IOException {
    }

    @ResponseBody
    @ExceptionHandler
    public Result<String> handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        if (e instanceof NotLoginException) {
            return ZiguiUtil.notLogin();
        }
        return ZiguiUtil.error(e.getMessage());
    }
}