package com.kkbbs.controller.base;

import com.kkbbs.entity.constants.Constants;
import com.kkbbs.entity.enums.ResponseCodeEnum;
import com.kkbbs.entity.vo.ResponseVO;
import com.kkbbs.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandlerController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request) {
        logger.error("请求错误，请求地址{},错误信息:", request.getRequestURL(), e);
        ResponseVO response = new ResponseVO();
        //404
        if (e instanceof NoHandlerFoundException) {
            response.setCode(ResponseCodeEnum.CODE_404.getCode());
            response.setInfo(ResponseCodeEnum.CODE_404.getMsg());
            response.setStatus(Constants.STATUC_ERROR);
        } else if (e instanceof BusinessException) {
            //业务错误
            BusinessException biz = (BusinessException) e;
            response.setCode(biz.getCode());
            if (biz.getCode() == null) {
                response.setCode(ResponseCodeEnum.CODE_600.getCode());
            }
            response.setInfo(biz.getMessage());
            response.setStatus(Constants.STATUC_ERROR);
        } else if (e instanceof BindException) {
            //参数类型错误
            response.setCode(ResponseCodeEnum.CODE_600.getCode());
            response.setInfo(ResponseCodeEnum.CODE_600.getMsg());
            response.setStatus(Constants.STATUC_ERROR);
        } else if (e instanceof DuplicateKeyException) {
            //主键冲突
            response.setCode(ResponseCodeEnum.CODE_601.getCode());
            response.setInfo(ResponseCodeEnum.CODE_601.getMsg());
            response.setStatus(Constants.STATUC_ERROR);
        } else {
            response.setCode(ResponseCodeEnum.CODE_500.getCode());
            response.setInfo(ResponseCodeEnum.CODE_500.getMsg());
            response.setStatus(Constants.STATUC_ERROR);
        }
        return response;
    }
}
