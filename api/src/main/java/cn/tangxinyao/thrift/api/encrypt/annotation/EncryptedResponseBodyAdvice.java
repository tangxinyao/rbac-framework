package cn.tangxinyao.thrift.api.encrypt.annotation;

import cn.tangxinyao.thrift.api.encrypt.config.EncryptProperties;
import cn.tangxinyao.thrift.api.encrypt.util.AESEncryptUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
public class EncryptedResponseBodyAdvice implements ResponseBodyAdvice {

    @Autowired
    private EncryptProperties properties;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    private static ThreadLocal<Boolean> encryptedStatus = new ThreadLocal<>();

    public static void setEncryptedStatus(Boolean status) {
        EncryptedResponseBodyAdvice.encryptedStatus.set(status);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class selectedConverterType,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // EncryptedResponseBodyAdvice.setEncryptedStatus(true);

        Boolean status = encryptedStatus.get();
        if (status != null && !status) {
            encryptedStatus.remove();
            return body;
        }
        long startedAt = System.currentTimeMillis();

        if (methodParameter.getMethod().isAnnotationPresent(Encrypt.class) && !properties.isDebug()) {

            String token = serverHttpRequest.getHeaders().getFirst("X-Access-Token");
            String key = (String) redisTemplate.opsForValue().get(token);

            if (StringUtils.isEmpty(key)) {
                throw new NullPointerException("AES Key not existed in redis");
            }

            try {
                String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                String result = AESEncryptUtil.aesEncrypt(content, key);
                long endedAt = System.currentTimeMillis();
                log.info("encrypted within: " + (endedAt - startedAt));
                return result;
            } catch (Exception e) {
                log.error("encrypted with error", e);
            }
        }
        return body;
    }
}
