package rebue.jwt.svr.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.sbs.feign.FeignConfig;
import rebue.scx.jwt.ro.JwtSignRo;
import rebue.scx.jwt.ro.JwtVerifyRo;
import rebue.scx.jwt.to.JwtUserInfoTo;

@FeignClient(name = "jwt-svr", configuration = FeignConfig.class)
public interface JwtSvc {

    /**
     * JWT签名
     * 
     * @param to
     *            用户信息
     */
    @PostMapping("/jwt/sign")
    JwtSignRo sign(@RequestBody JwtUserInfoTo to);

    /**
     * 验证JWT签名
     * 
     * @param toVerifySign
     *            要验证的签名
     */
    @GetMapping("/jwt/verify")
    JwtVerifyRo verify(@RequestParam("toVerifySign") String toVerifySign);

}
