package cn.tangxinyao.thrift.api.security.service;

import cn.tangxinyao.thrift.api.security.domain.JwtUser;
import cn.tangxinyao.thrift.api.service.AccountManager;
import cn.tangxinyao.thrift.sdk.auth.TAccountService;
import cn.tangxinyao.thrift.sdk.auth.TAuthResponse;
import cn.tangxinyao.thrift.sdk.common.TRole;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtUserService implements UserDetailsService {
    @Autowired
    AccountManager accountManager;

    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        TTransport accountTransport = null;
        try {
            accountTransport = accountManager.getConnection();
            TAccountService.Client accountClient = accountManager.getClient(accountTransport);
            TAuthResponse tAuthResponse = accountClient.loadByUsername(UUID.randomUUID().toString(), username);
            return new JwtUser(tAuthResponse.getId(), username, "", roleNames(tAuthResponse.getRoles()));
        } catch (Exception e) {
            throw new UsernameNotFoundException("");
        } finally {
            accountManager.returnConnection(accountTransport);
        }
    }

    private List<String> roleNames(List<TRole> tRoles) {
        return tRoles.stream().map(role -> role.getName()).collect(Collectors.toList());
    }
}
