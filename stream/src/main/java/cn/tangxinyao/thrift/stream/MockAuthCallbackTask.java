package cn.tangxinyao.thrift.stream;

import cn.tangxinyao.thrift.stream.sender.CallbackSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MockAuthCallbackTask {
    @Autowired
    private CallbackSender sender;

    @Scheduled(fixedRate = 100)
    public void sendCountDown() {
        sender.countDown();
    }
}
