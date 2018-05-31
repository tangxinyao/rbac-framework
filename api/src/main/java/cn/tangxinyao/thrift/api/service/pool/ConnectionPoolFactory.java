package cn.tangxinyao.thrift.api.service.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ConnectionPoolFactory {
    private GenericObjectPool<TTransport> pool;

    public ConnectionPoolFactory(GenericObjectPoolConfig config, String ip, int port) {
        ConnectionFactory factory = new ConnectionFactory(ip, port, 3000);
        pool = new GenericObjectPool<TTransport>(factory, config);
    }

    public synchronized TTransport getConnection() throws Exception {
        return pool.borrowObject();
    }

    public void returnConnection(TTransport transport) {
        if (transport != null) {
            pool.returnObject(transport);
        }
    }

    class ConnectionFactory extends BasePooledObjectFactory<TTransport> {

        private String ip;
        private int port;
        private int socketTimeout;

        public ConnectionFactory(String ip, int port, int socketTimeout) {
            this.ip = ip;
            this.port = port;
            this.socketTimeout = socketTimeout;
        }

        @Override
        public TTransport create() throws Exception {
            TSocket socket = new TSocket(ip, port);
            socket.setTimeout(socketTimeout);
            TTransport transport = socket;
            transport = new TFastFramedTransport(transport);
            if (!transport.isOpen()) {
                transport.open();
            }
            return transport;
        }

        @Override
        public PooledObject<TTransport> wrap(TTransport transport) {
            return new DefaultPooledObject<>(transport);
        }

        @Override
        public void activateObject(PooledObject<TTransport> pooledObject) throws TTransportException {
            if (!pooledObject.getObject().isOpen()) {
                pooledObject.getObject().open();
            }
        }

        @Override
        public void destroyObject(PooledObject<TTransport> p) throws Exception {
            passivateObject(p);
            p.markAbandoned();
        }

        @Override
        public boolean validateObject(PooledObject<TTransport> p) {
            if (p.getObject() != null) {
                if (p.getObject().isOpen()) {
                    return true;
                }
                try {
                    p.getObject().open();
                    return true;
                } catch (TTransportException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }
}
