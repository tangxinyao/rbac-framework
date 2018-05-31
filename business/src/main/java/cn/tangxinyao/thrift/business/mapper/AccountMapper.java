package cn.tangxinyao.thrift.business.mapper;

import cn.tangxinyao.thrift.business.domain.authority.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    void insertUser(User user);
    User selectUserById(int id);
    User selectUserByUsername(String username);

    void insertRole(Role role);
    List<Role> selectAllRoles();

    void insertUserRole(UserRole userRole);
    Role selectRoleByName(String roleName);
    List<Role> selectRoleByUserId(int userId);

    void insertModule(Module module);
    List<Module> selectAllModules();

    void insertPermission(Permission permission);
    List<Module> selectModuleByRoleId(int roleId);
}
