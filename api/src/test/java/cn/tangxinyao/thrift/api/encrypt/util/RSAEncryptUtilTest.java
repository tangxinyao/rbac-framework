package cn.tangxinyao.thrift.api.encrypt.util;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAEncryptUtilTest {

    private String data = "hello world";

    @Test
    public void assertRSAEncryptHaveNoException() throws Exception {
        KeyPair keyPair = RSAEncryptUtil.getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = RSAEncryptUtil.encryptData(data.getBytes(), publicKey);
        Assert.assertEquals(StringUtils.newString(RSAEncryptUtil.decryptData(bytes, privateKey), "utf-8"), data);
    }

}
