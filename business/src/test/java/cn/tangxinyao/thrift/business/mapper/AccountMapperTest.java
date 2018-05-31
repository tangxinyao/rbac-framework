package cn.tangxinyao.thrift.business.mapper;

import cn.tangxinyao.thrift.business.domain.authority.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountMapperTest {
    @Autowired
    private AccountMapper accountMapper;

    private User user;
    private Role role;
    private Module module;
    private UserRole userRole;
    private Permission permission;

    @Before
    public void initialize() {
        user = new User("shaytang@163.com", "test123456");
        role = new Role("root");
        module = new Module(null, 1, "audit_kyc", "button");
    }

    @Test
    public void assertInsertUserHaveNoException() {
        accountMapper.insertUser(user);
    }

    @Test
    public void assertInsertRoleHaveNoException() {
        accountMapper.insertRole(role);
    }

    @Test
    public void assertSelectRoleByRoleNameHaveNoException() {
        accountMapper.insertRole(role);
        Assert.assertEquals(role.getId(), accountMapper.selectRoleByName(role.getName()).getId());
    }

    @Test
    public void assertSelectAllRolesHaveNoException() {
        List<Role> roles = accountMapper.selectAllRoles();
        Assert.assertEquals(roles.size(), 0);
    }

    @Test
    public void assertInsertModuleHaveNoException() {
        accountMapper.insertModule(module);
    }

    @Test
    public void assertSelectAllModulesHaveNoException() {
        List<Module> modules = accountMapper.selectAllModules();
        Assert.assertEquals(modules.size(), 0);
    }

    @Test
    public void assertSelectRoleByUserIdHaveNoException() {
        accountMapper.insertUser(user);
        accountMapper.insertRole(role);
        userRole = new UserRole(user.getId(), role.getId());

        accountMapper.insertUserRole(userRole);
        List<Role> roles = accountMapper.selectRoleByUserId(user.getId());

        Assert.assertEquals(roles.get(0).getId(), role.getId());
    }

    @Test
    public void assertSelectModuleByRoleIdHaveNoException() {
        accountMapper.insertRole(role);
        accountMapper.insertModule(module);
        permission = new Permission(role.getId(), module.getId());

        accountMapper.insertPermission(permission);
        List<Module> modules = accountMapper.selectModuleByRoleId(role.getId());

        Assert.assertEquals(modules.get(0).getId(), module.getId());

    }

}
