package cn.tangxinyao.thrift.stream.receiver;

import cn.tangxinyao.thrift.stream.config.RabbitMQConfig;
import cn.tangxinyao.thrift.stream.domain.Count;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CallbackReceiver {

    @RabbitListener(queues = RabbitMQConfig.QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void receiveCallbackSender(Count count) {
        System.out.println(count.toString());
    }
}
