package com.web.config;

import com.web.exception.MessageException;
import com.web.enums.LogLevel;
import com.web.service.AppLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private AppLogService appLogService;

    // Pointcut cho tất cả các phương thức trong package service (ngoại trừ AppLogService để tránh loop)
    @Pointcut("(execution(* com.web.service.*.save*(..)) || " +
            "execution(* com.web.service.*.add*(..)) || " +
            "execution(* com.web.service.*.update*(..)) || " +
            "execution(* com.web.service.*.delete*(..)) || " +
            "execution(* com.web.service.*.import*(..)) || " +
            "execution(* com.web.service.UserService.login(..))) && " +
            "!within(com.web.service.AppLogService)")
    public void loggableMethods() {}

    @AfterReturning(pointcut = "loggableMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String actionDescription = getActionDescription(className, methodName);
        
        appLogService.addLog(actionDescription, LogLevel.INFO);
    }

    @AfterThrowing(pointcut = "execution(* com.web.service.*.*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String errorMessage = exception.getMessage();
        if ((errorMessage == null || errorMessage.trim().isEmpty()) && exception instanceof MessageException) {
            errorMessage = ((MessageException) exception).getDefaultMessage();
        }
        appLogService.addLog("Lỗi tại " + className + "." + methodName + ": " + errorMessage, LogLevel.ERROR);
    }

    private String getActionDescription(String className, String methodName) {
        // Tùy biến thông báo dựa trên tên class và method
        if (methodName.equals("login")) return "Đăng nhập hệ thống";
        
        String action = "";
        if (methodName.startsWith("save") || methodName.startsWith("add")) action = "Thêm mới/Cập nhật";
        else if (methodName.startsWith("delete")) action = "Xóa";
        else if (methodName.startsWith("update")) action = "Cập nhật";
        else if (methodName.startsWith("import")) action = "Nhập dữ liệu từ Excel";
        else action = "Thực hiện " + methodName;

        return action + " tại " + className;
    }
}
