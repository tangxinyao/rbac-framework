package cn.tangxinyao.thrift.api.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PublicKeyResponse {
    String uuid;
    String publicKey;
}
