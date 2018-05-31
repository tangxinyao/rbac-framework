package cn.tangxinyao.thrift.api.domain;

import cn.tangxinyao.thrift.sdk.common.TModule;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Module {
    private int id;
    private int parentId;
    private int level;
    private String name;
    private String type;

    public static Module convertFromTModule(TModule tModule) {
        return new Module(tModule.getId(), tModule.getParentId(), tModule.getLevel(), tModule.getName(), tModule.getType());
    }
}
