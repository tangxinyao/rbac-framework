package cn.tangxinyao.thrift.api.controller;

import cn.tangxinyao.thrift.api.core.Response;
import cn.tangxinyao.thrift.api.domain.Module;
import cn.tangxinyao.thrift.api.domain.request.AesKeyRequest;
import cn.tangxinyao.thrift.api.domain.request.LoginRequest;
import cn.tangxinyao.thrift.api.domain.request.SignupRequest;
import cn.tangxinyao.thrift.api.domain.response.AuthResponse;
import cn.tangxinyao.thrift.api.domain.response.PublicKeyResponse;
import cn.tangxinyao.thrift.api.encrypt.annotation.Decrypt;
import cn.tangxinyao.thrift.api.encrypt.annotation.Encrypt;
import cn.tangxinyao.thrift.api.encrypt.util.RSAEncryptUtil;
import cn.tangxinyao.thrift.api.service.AccountManager;
import cn.tangxinyao.thrift.sdk.auth.TAccountService;
import cn.tangxinyao.thrift.sdk.auth.TAuthResponse;
import cn.tangxinyao.thrift.sdk.auth.TLoginRequest;
import cn.tangxinyao.thrift.sdk.auth.TSignupRequest;
import cn.tangxinyao.thrift.api.security.util.JwtUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountManager accountManager;
    @Autowired
    private RedisTemplate redisTemplate;

    public static String RSA_PREFIX = "rsa-";
    public static String AES_PREFIX = "aes-";

    @GetMapping("/publicKey")
    public Response getPublicKey() throws NoSuchAlgorithmException {
        KeyPair keyPair = RSAEncryptUtil.getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String uuid = RSA_PREFIX +  UUID.randomUUID().toString();
        String privateKeyValue = Base64.encodeBase64String(privateKey.getEncoded());
        redisTemplate.opsForValue().set(uuid, privateKeyValue);
        redisTemplate.expire(uuid, 1, TimeUnit.MINUTES);

        String key = Base64.encodeBase64String(publicKey.getEncoded());
        return Response.success(new PublicKeyResponse(uuid, key));
    }

    @PostMapping("/aesKey")
    public Response getAesKey(@RequestBody AesKeyRequest aesKeyRequest) throws Exception {
        String rsaUuid = aesKeyRequest.getUuid();
        String privateKeyValue = (String) redisTemplate.opsForValue().get(rsaUuid);
        redisTemplate.delete(rsaUuid);

        PrivateKey privateKey = RSAEncryptUtil.getPrivateKey(Base64.decodeBase64(privateKeyValue));
        String aesKey = StringUtils.newString(RSAEncryptUtil.decryptData(Base64.decodeBase64(aesKeyRequest.getAesKey()), privateKey), "utf-8");
        String uuid = AES_PREFIX + UUID.randomUUID().toString();
        System.out.println(aesKey);

        redisTemplate.opsForValue().set(uuid, aesKey);
        return Response.success(uuid);
    }

    @PostMapping("/login")
    @Decrypt
    @Encrypt
    public Response login(@RequestBody LoginRequest request) {
        TTransport accountTransport = null;
        try {
            accountTransport = accountManager.getConnection();
            TAccountService.Client accountClient = accountManager.getClient(accountTransport);

            TLoginRequest loginRequest = new TLoginRequest(request.getUsername(), request.getPassword());
            TAuthResponse authResponse = accountClient.login(UUID.randomUUID().toString(), loginRequest);

            String token;
            if (authResponse.isSetId()) {
                token = JwtUtil.getToken(authResponse.getId(), authResponse.getUsername());
            } else {
                return Response.usernameOrPasswordError();
            }
            return Response.success(new AuthResponse(token));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error();
        } finally {
            accountManager.returnConnection(accountTransport);
        }
    }

    @PostMapping("/signup")
    public Response singup(@RequestBody SignupRequest request) {
        TTransport accountTransport = null;
        try {
            accountTransport = accountManager.getConnection();
            TAccountService.Client accountClient = accountManager.getClient(accountTransport);

            TSignupRequest signupRequest = new TSignupRequest(request.getUsername(), request.getPassword(), request.getRoles());
            TAuthResponse tAuthResponse = accountClient.signup(UUID.randomUUID().toString(), signupRequest);

            String token;
            if (tAuthResponse.isSetId()) {
                token = JwtUtil.getToken(tAuthResponse.getId(), tAuthResponse.getUsername());
            } else {
                return Response.error();
            }
            return Response.success(new AuthResponse(token));
        } catch (Exception e) {
            return Response.error();
        } finally {
            accountManager.returnConnection(accountTransport);
        }
    }

    @GetMapping("/modules")
    public Response getModulesByRoleNames(@RequestParam("roleNames") String[] roleNames) {
        TTransport accountTransport = null;
        try {
            accountTransport = accountManager.getConnection();
            TAccountService.Client accountClient = accountManager.getClient(accountTransport);

            List<Module> modules = accountClient
                    .getModulesByRoleNames(UUID.randomUUID().toString(), Arrays.asList(roleNames))
                    .stream()
                    .map(Module::convertFromTModule)
                    .collect(Collectors.toList());

            return Response.success(modules);
        } catch (Exception e) {
            return Response.error();
        } finally {
            accountManager.returnConnection(accountTransport);
        }
    }

}
