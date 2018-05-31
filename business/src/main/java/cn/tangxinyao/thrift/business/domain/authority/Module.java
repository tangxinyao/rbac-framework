package cn.tangxinyao.thrift.business.domain.authority;

import cn.tangxinyao.thrift.sdk.common.TModule;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Module {
    private Integer id;
    private Integer parentId;
    private Integer level;
    private String name;
    private String type;

    public Module(Integer parentId, Integer level, String name, String type) {
        this.parentId = parentId;
        this.level = level;
        this.name = name;
        this.type = type;
    }

    public static TModule convertToTModule(Module module) {
        return new TModule(module.getId(), module.getParentId(), module.getLevel(), module.getName(), module.getType());
    }
}
