package cn.tangxinyao.thrift.api.service;

import cn.tangxinyao.thrift.api.service.pool.ConnectionPoolFactory;
import cn.tangxinyao.thrift.sdk.auth.TAccountService;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountManager implements AbstractManager {
    private ConnectionPoolFactory factory;

    public AccountManager(@Value("${custom.account.host}")String host, @Value("${custom.account.port}") int port) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        this.factory = new ConnectionPoolFactory(config, host, port);
    }

    @Override
    public TAccountService.Client getClient(TTransport transport) {
        TProtocol protocol = new TMultiplexedProtocol(new TBinaryProtocol(transport), "account");
        return new TAccountService.Client(protocol);
    }

    @Override
    public TTransport getConnection() throws Exception {
        return this.factory.getConnection();
    }

    @Override
    public void returnConnection(TTransport transport) {
        this.factory.returnConnection(transport);
    }
}
