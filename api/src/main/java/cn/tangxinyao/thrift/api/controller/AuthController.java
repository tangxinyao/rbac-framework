package cn.tangxinyao.thrift.api.controller;

import cn.tangxinyao.thrift.api.core.Response;
import cn.tangxinyao.thrift.api.domain.Module;
import cn.tangxinyao.thrift.api.domain.request.LoginRequest;
import cn.tangxinyao.thrift.api.domain.request.SignupRequest;
import cn.tangxinyao.thrift.api.domain.response.AuthResponse;
import cn.tangxinyao.thrift.api.service.AccountManager;
import cn.tangxinyao.thrift.sdk.auth.TAccountService;
import cn.tangxinyao.thrift.sdk.auth.TAuthResponse;
import cn.tangxinyao.thrift.sdk.auth.TLoginRequest;
import cn.tangxinyao.thrift.sdk.auth.TSignupRequest;
import cn.tangxinyao.thrift.util.JwtUtil;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AccountManager accountManager;

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest request) {
        TTransport accountTransport = null;
        try {
            accountTransport = accountManager.getConnection();
            TAccountService.Client accountClient = accountManager.getClient(accountTransport);

            TLoginRequest loginRequest = new TLoginRequest(request.getUsername(), request.getPassword());
            TAuthResponse authResponse = accountClient.login(UUID.randomUUID().toString(), loginRequest);

            String token;
            if (authResponse.isSetId()) {
                token = JwtUtil.getToken(authResponse.getId(), authResponse.getUsername(), request.getPassword());
            } else {
                return Response.usernameOrPasswordError();
            }
            return Response.success(new AuthResponse(token));
        } catch (Exception e) {
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
                token = JwtUtil.getToken(tAuthResponse.getId(), tAuthResponse.getUsername(), request.getPassword());
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
