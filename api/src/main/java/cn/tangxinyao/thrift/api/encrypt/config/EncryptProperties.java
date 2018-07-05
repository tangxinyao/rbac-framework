package cn.tangxinyao.thrift.api.encrypt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EncryptProperties {
    private String charset = "utf-8";
    private boolean debug = false;
}
