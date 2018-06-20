package cn.tangxinyao.thrift.stream.sender;

import cn.tangxinyao.thrift.stream.config.RabbitMQConfig;
import cn.tangxinyao.thrift.stream.domain.Count;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class CallbackSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void countDown() {
        Count count = Count.builder().value(1).createdAt(new Date()).build();
        this.amqpTemplate.convertAndSend(RabbitMQConfig.QUEUE, count);
    }

}
