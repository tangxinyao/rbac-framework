package cn.tangxinyao.thrift.business.domain.authority;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Permission {
    private int id;
    private int roleId;
    private int moduleId;

    public Permission(int roleId, int moduleId) {
        this.roleId = roleId;
        this.moduleId = moduleId;
    }
}
