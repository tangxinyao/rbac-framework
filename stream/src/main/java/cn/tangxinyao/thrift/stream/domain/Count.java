package cn.tangxinyao.thrift.stream.domain;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Count {
    private int value;
    private Date createdAt;
}
