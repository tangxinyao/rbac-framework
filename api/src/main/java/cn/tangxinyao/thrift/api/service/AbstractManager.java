package cn.tangxinyao.thrift.api.service;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.transport.TTransport;

public interface AbstractManager {
    TServiceClient getClient(TTransport transport);
    TTransport getConnection() throws Exception;
    void returnConnection(TTransport transport);
}
