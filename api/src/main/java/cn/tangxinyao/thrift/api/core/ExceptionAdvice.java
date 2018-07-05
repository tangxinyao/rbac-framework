package cn.tangxinyao.thrift.api.core;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Response handleException(Exception e) {
        return Response.error(e.getMessage());
    }

}
