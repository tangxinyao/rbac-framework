package cn.tangxinyao.thrift.business.domain.authority;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserRole {
    private int id;
    private int userId;
    private int roleId;

    public UserRole(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
