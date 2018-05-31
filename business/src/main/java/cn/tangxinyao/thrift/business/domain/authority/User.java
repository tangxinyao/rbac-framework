package cn.tangxinyao.thrift.business.domain.authority;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
    private Integer id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
