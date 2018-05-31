package cn.tangxinyao.thrift.api.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String key;
    private String code;
    private String captchaKey;
    private String captchaCode;
    private List<String> authorities;
}
