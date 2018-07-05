package cn.tangxinyao.thrift.api.encrypt.annotation;

import cn.tangxinyao.thrift.api.encrypt.config.EncryptConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EncryptConfiguration.class})
public @interface EnableEncrypt {
}
