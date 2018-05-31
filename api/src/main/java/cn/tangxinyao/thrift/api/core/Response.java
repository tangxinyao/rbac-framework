package cn.tangxinyao.thrift.api.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response<T> {
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Response(int status) {
        this.status = status;
    }

    public Response(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public Response(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static Response success() {
        return new Response(200);
    }

    public static <T> Response success(T data) {
        return new Response(200, data);
    }

    public static Response usernameOrPasswordError() {
        return new Response(100404);
    }

    public static Response error() {
        return new Response(100400);
    }
}
