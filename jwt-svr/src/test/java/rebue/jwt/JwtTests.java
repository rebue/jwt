package rebue.jwt;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import rebue.scx.jwt.dic.JwtSignResultDic;
import rebue.scx.jwt.dic.JwtVerifyResultDic;
import rebue.scx.jwt.ro.JwtSignRo;
import rebue.scx.jwt.ro.JwtVerifyRo;
import rebue.wheel.OkhttpUtils;

public class JwtTests {

    private String _hostUrl = "http://localhost:9500";
//    private String _hostUrl = "http://192.168.1.201/afc-svr";

    private ObjectMapper _objectMapper = new ObjectMapper();

    @Test
    public void test01() throws IOException {
        // JWT签名
        String url = _hostUrl + "/jwt/sign?userId=" + 472572157856186368L;
        JwtSignRo signRo = _objectMapper.readValue(OkhttpUtils.post(url), JwtSignRo.class);
        Assert.assertNotNull(signRo);
        System.out.println(signRo);
        Assert.assertEquals(JwtSignResultDic.SUCCESS, signRo.getResult());
        // 验证JWT签名
        url = _hostUrl + "/jwt/verify?toVerifySign=" + signRo.getSign();
        JwtVerifyRo veryfyRo = _objectMapper.readValue(OkhttpUtils.get(url), JwtVerifyRo.class);
        Assert.assertNotNull(veryfyRo);
        System.out.println(veryfyRo);
        Assert.assertEquals(JwtVerifyResultDic.SUCCESS, veryfyRo.getResult());
    }

}
