package cn.tangxinyao.thrift.business.domain.authority;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Role {
    private Integer id;
    private String name;
    public Role(String name) {
        this.name = name;
    }
}
