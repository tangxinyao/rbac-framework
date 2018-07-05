package cn.tangxinyao.thrift.api.domain.request;

import lombok.Data;

@Data
public class AesKeyRequest {
    private String uuid;
    private String aesKey;
}
