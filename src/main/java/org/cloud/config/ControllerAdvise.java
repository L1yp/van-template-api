package org.cloud.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.cloud.exception.BusinessException;
import org.cloud.model.common.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class ControllerAdvise {

    private static final Logger log = LoggerFactory.getLogger(ControllerAdvise.class);

    /**
     * 参数类型不匹配异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResultData<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        log.error("handleMethodArgumentTypeMismatchException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultData.err(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    /**
     * 字段校验失败异常， 如@Min @Max
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResultData<Void>> handleConstraintViolationException(ConstraintViolationException e){
        log.error("handleConstraintViolationException", e);
        StringBuilder sb = new StringBuilder(64);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            Object invalidValue = constraintViolation.getInvalidValue();
            String path = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            sb.append(path).append(": ").append(message).append(", 当前传入：").append(invalidValue).append(";\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultData.err(HttpStatus.BAD_REQUEST.value(), sb.toString()));
    }

    /**
     * 参数校验失败
     * @param e
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ResultData<Void>> handle(BindException e){
//        log.error("handleMethodArgumentNotValidException/BindException", e);
        List<String> messages = new ArrayList<>();
        String message = "";
        for (ObjectError objectError : e.getBindingResult().getAllErrors()) {
            if (FieldError.class.isAssignableFrom(objectError.getClass())){
                FieldError fieldError = (FieldError) objectError;
                message = String.format("%s.%s %s, val: %s",
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()
                );
            }else {
                message = String.format("object: %s, message: %s",
                        objectError.getObjectName(),
                        objectError.getDefaultMessage()
                );
            }
            messages.add(message);
        }

        log.error("handleMethodArgumentNotValidException/BindException: {}", String.join(",", messages));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultData.err(HttpStatus.BAD_REQUEST.value(), messages.toString()));
    }

    /**
     * 缺少参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResultData<Void>> handleMissingParamException(MissingServletRequestParameterException e){
        log.error("handleMissingParamException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResultData.err(HttpStatus.BAD_REQUEST.value(),
                        String.format(
                                "缺少参数，类型：%s, 名称：%s",
                                e.getParameterType(),
                                e.getParameterName()
                        )
                )
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultData<Void>> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getCode()).body(ResultData.err(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultData<Void>> handlerRuntimeException(RuntimeException e){
        log.error("handlerRuntimeException", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultData.err(HttpStatus.INTERNAL_SERVER_ERROR.value(), "请求有误，请稍后重试，或联系管理员！", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResultData<Void>> handlerException(IOException e){
        log.error("handlerIOException", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultData.err(HttpStatus.INTERNAL_SERVER_ERROR.value(), "链接超时，请稍后重试，或联系管理员！", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<ResultData<Void>> handlerException(NotLoginException e){
        return ResponseEntity.ok(ResultData.err(HttpStatus.UNAUTHORIZED.value(), "请求有误，请稍后重试，或联系管理员！", 500, e.getMessage()));
    }

    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<ResultData<Void>> handlerException(NotPermissionException e){
        return ResponseEntity.ok(ResultData.err(HttpStatus.BAD_REQUEST.value(), "请求有误，请稍后重试，或联系管理员！", 500, e.getMessage()));
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultData<Void>> handlerException(Exception e){
        log.error("handlerException", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultData.err(HttpStatus.INTERNAL_SERVER_ERROR.value(), "请求有误，请稍后重试，或联系管理员！", 500, e.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResultData<Void>> handlerException(Throwable e){
        log.error("handlerThrowable", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultData.err(HttpStatus.INTERNAL_SERVER_ERROR.value(), "请求有误，请稍后重试，或联系管理员！", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }


}
