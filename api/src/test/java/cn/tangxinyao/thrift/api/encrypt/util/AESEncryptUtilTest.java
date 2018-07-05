package cn.tangxinyao.thrift.api.encrypt.util;

import org.junit.Assert;
import org.junit.Test;

public class AESEncryptUtilTest {

    private String encryptKey = "587301c3c2087f7742bbfd64652f8b73";

    @Test
    public void assertEncryptHaveNoException() throws Exception {
        String data = AESEncryptUtil.aesEncrypt("hello", encryptKey);
        System.out.println(data);
        Assert.assertEquals(AESEncryptUtil.aesDecrypt(data, encryptKey), "hello");
    }

    @Test
    public void assertJSDataEncryptHaveNoException() throws Exception {
        String str = "rW1nfjtpkWSN541jty1cMEFivMiwFX0VJGDOm4usbALU5XS4tNfH844+A4hdUBLeySnLTm+ZiOyWzNvyuwNnIS+RVu2Ne0h/dU22Chf8qY7Dmh4l6cgPVc+1jkusjCyahkl4kouSCKnI3yhVIpcu8kj/LYQx1CGGnxB6+p5L+SVwK1shSs06csP1IvdrhVtMVd54AIYosDpRq4g5WOLbEWud09fsQSAwlIw45BkFzl8EwW43dpgRdk0atFaEODhH70A1sPXgCsfdjN2JzLyOr7B5OGyp8dMXgTOV5Xuiu10=";
        String key = "481d01e39dbd31ebc2894e1a471bdbfa";
        String result = AESEncryptUtil.aesDecrypt(str, key);
        System.out.println(result);
    }

}
