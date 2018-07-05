package cn.tangxinyao.thrift.business;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BusinessApplication {

    public static void main(String[] args) throws TTransportException {
        ConfigurableApplicationContext context = SpringApplication.run(BusinessApplication.class, args);
        TNonblockingServerTransport transport = new TNonblockingServerSocket(8185, 0);
        TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer.Args(transport);
        TBinaryProtocol.Factory protocol = new TBinaryProtocol.Factory(true, true);

        serverArgs.processor((TProcessor) context.getBean("multiplexedProcessor"));
        serverArgs.protocolFactory(protocol);
        serverArgs.transportFactory(new TFramedTransport.Factory());

        TServer server = new TThreadedSelectorServer(serverArgs);
        server.serve();
    }
}
