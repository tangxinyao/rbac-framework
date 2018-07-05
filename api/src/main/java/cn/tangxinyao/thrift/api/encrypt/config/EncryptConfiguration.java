package cn.tangxinyao.thrift.api.encrypt.config;

import cn.tangxinyao.thrift.api.encrypt.annotation.DecryptedRequestBodyAdvice;
import cn.tangxinyao.thrift.api.encrypt.annotation.EncryptedResponseBodyAdvice;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class EncryptConfiguration {

    @Bean
    public DecryptedRequestBodyAdvice decryptedRequestBodyAdvice() {
        return new DecryptedRequestBodyAdvice();
    }

    @Bean
    public EncryptedResponseBodyAdvice encryptedResponseBodyAdvice() {
        return new EncryptedResponseBodyAdvice();
    }

}
