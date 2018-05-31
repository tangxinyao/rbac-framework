package cn.tangxinyao.thrift.business.controller;

import cn.tangxinyao.thrift.business.domain.authority.Module;
import cn.tangxinyao.thrift.business.domain.authority.Role;
import cn.tangxinyao.thrift.business.domain.authority.User;
import cn.tangxinyao.thrift.business.domain.authority.UserRole;
import cn.tangxinyao.thrift.business.mapper.AccountMapper;
import cn.tangxinyao.thrift.sdk.auth.TAccountService;
import cn.tangxinyao.thrift.sdk.auth.TAuthResponse;
import cn.tangxinyao.thrift.sdk.auth.TLoginRequest;
import cn.tangxinyao.thrift.sdk.auth.TSignupRequest;
import cn.tangxinyao.thrift.sdk.common.TModule;
import cn.tangxinyao.thrift.sdk.common.TRole;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("accountController")
public class AccountController implements TAccountService.Iface {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public TAuthResponse signup(String traceId, TSignupRequest signupRequest) throws TException {
        User user = new User(signupRequest.getUsername(), signupRequest.getPassword());

        accountMapper.insertUser(user);

        List<Role> roles = signupRequest.getRoles().stream()
                .map(role -> accountMapper.selectRoleByName(role)).collect(Collectors.toList());

        roles.stream().forEach(role -> accountMapper.insertUserRole(new UserRole(user.getId(), role.getId())));

        List<TRole> tRoles = roles.stream().map(role -> new TRole(role.getId(), role.getName())).collect(Collectors.toList());
        return new TAuthResponse(user.getId(), user.getUsername(), tRoles);
    }

    @Override
    public TAuthResponse login(String traceId, TLoginRequest loginRequest) throws TException {
        User user = accountMapper.selectUserByUsername(loginRequest.getUsername());
        if (user != null && user.getPassword() != loginRequest.getPassword()) {
            return getTAuthResponseByUser(null);
        }
        return getTAuthResponseByUser(user);
    }

    @Override
    public TAuthResponse loadByUsername(String traceId, String username) throws TException {
        User user = accountMapper.selectUserByUsername(username);
        return getTAuthResponseByUser(user);
    }

    @Override
    public List<TModule> getModulesByRoleNames(String traceId, List<String> roleNames) throws TException {
        return roleNames.stream()
                .map(roleName -> accountMapper.selectRoleByName(roleName))
                .flatMap(role -> accountMapper.selectModuleByRoleId(role.getId()).stream())
                .distinct()
                .map(Module::convertToTModule)
                .collect(Collectors.toList());
    }

    private TAuthResponse getTAuthResponseByUser(User user) {
        if (user == null) {
            return new TAuthResponse();
        }
        return new TAuthResponse(user.getId(), user.getUsername(), accountMapper.selectRoleByUserId(user.getId())
                .stream().map(role -> new TRole(role.getId(), role.getName()))
                .collect(Collectors.toList()));
    }
}
