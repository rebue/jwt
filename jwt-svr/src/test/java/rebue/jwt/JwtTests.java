package rebue.jwt;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import rebue.scx.jwt.dic.JwtSignResultDic;
import rebue.scx.jwt.dic.JwtVerifyResultDic;
import rebue.scx.jwt.ro.JwtSignRo;
import rebue.scx.jwt.ro.JwtVerifyRo;
import rebue.scx.jwt.to.JwtUserInfoTo;
import rebue.wheel.OkhttpUtils;

public class JwtTests {

//    private String       _hostUrl      = "http://localhost:9500";
//    private String _hostUrl = "http://192.168.1.201/afc-svr";
    private String       _hostUrl      = "http://www.duamai.com:34443";
    private String       _userId       = "1";

    private String       _wxOpenId     = "oqTsm0gdD148UcBzibH4JTm2d9q4";
    private String       _wxUnionId    = "oqTsm0gdD148UcBzibH4JTm2d9q4";
    private String       _orgId        = "517928358546243584";

    private ObjectMapper _objectMapper = new ObjectMapper();

    @Test
    public void test01() throws IOException {
        // JWT签名
        String url = _hostUrl + "/jwt/sign";
        JwtUserInfoTo to = new JwtUserInfoTo();
        to.setUserId(_userId);
        to.setSysId("damai-admin");
        Map<String, Object> addition = new LinkedHashMap<>();
        addition.put("wxOpenId", _wxOpenId);
        addition.put("wxUnionId", _wxUnionId);
        addition.put("orgId", _orgId);
        to.setAddition(addition);
        String jsonParams = _objectMapper.writeValueAsString(to);
        JwtSignRo signRo = _objectMapper.readValue(OkhttpUtils.postByJsonParams(url, jsonParams), JwtSignRo.class);
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
