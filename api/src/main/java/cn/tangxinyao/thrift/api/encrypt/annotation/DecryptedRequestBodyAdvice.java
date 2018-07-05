package cn.tangxinyao.thrift.api.encrypt.annotation;

import cn.tangxinyao.thrift.api.encrypt.config.EncryptProperties;
import cn.tangxinyao.thrift.api.encrypt.util.AESEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

@ControllerAdvice
@Slf4j
public class DecryptedRequestBodyAdvice implements RequestBodyAdvice {

    @Autowired
    private EncryptProperties properties;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (parameter.getMethod().isAnnotationPresent(Decrypt.class) && !properties.isDebug()) {
            try {
                return new DecryptHttpInputMessage(inputMessage, properties.getCharset());
            } catch (Exception e) {
                log.error("decrypt with error", e);
            }
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    public class DecryptHttpInputMessage implements HttpInputMessage {

        private HttpHeaders headers;
        private InputStream body;

        public DecryptHttpInputMessage(HttpInputMessage inputMessage, String charset) throws Exception {
            this.headers = inputMessage.getHeaders();

            String token = headers.getFirst("X-Access-Token");
            String key = (String) redisTemplate.opsForValue().get(token);

            String content = IOUtils.toString(inputMessage.getBody(), charset);
            long startedAt = System.currentTimeMillis();
            String decryptedBody;
            if (content.startsWith("{")) {
                decryptedBody = content;
            } else {
                decryptedBody = AESEncryptUtil.aesDecrypt(content, key);
            }
            long endedAt = System.currentTimeMillis();
            log.debug("Decrypted within: " + (endedAt - startedAt));
            this.body = IOUtils.toInputStream(decryptedBody, charset);
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @Override
        public InputStream getBody() {
            return body;
        }
    }
}
