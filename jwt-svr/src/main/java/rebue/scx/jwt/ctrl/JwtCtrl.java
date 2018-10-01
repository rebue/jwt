package rebue.scx.jwt.ctrl;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import rebue.scx.jwt.dic.JwtSignResultDic;
import rebue.scx.jwt.dic.JwtVerifyResultDic;
import rebue.scx.jwt.ro.JwtSignRo;
import rebue.scx.jwt.ro.JwtVerifyRo;
import rebue.wheel.turing.JwtUtils;

@RestController
public class JwtCtrl {
    private final static Logger _log = LoggerFactory.getLogger(JwtCtrl.class);

    /**
     * 签名的key
     */
    @Value("#{'${jwt.key}'.getBytes('utf-8')}")
    private byte[]              key;
    /**
     * 签发者
     */
    @Value("${jwt.iss}")
    private String              iss;
    /**
     * 过期分钟数（默认是30分钟）
     * 传入分钟数，自动计算为毫秒数
     */
    @Value("#{${jwt.expirationMinutes:30}*60*1000}")
    private Long                expirationMs;

    /**
     * JWT签名
     * 
     * @param userId
     *            用户ID
     * @param sysId
     *            系统ID
     * @param orgId
     *            用户的组织ID
     */
    @PostMapping("/jwt/sign")
    public JwtSignRo sign(@RequestParam("userId") String userId, @RequestParam("sysId") String sysId, @RequestParam(value = "orgId", required = false) Long orgId) {
        _log.info("\r\n============================= 开始JWT签名 =============================\r\n");
        try {
            _log.info("JWT签名参数: userId={}", userId);
            JwtSignRo ro = new JwtSignRo();

            if (StringUtils.isBlank(userId)) {
                ro.setResult(JwtSignResultDic.PARAM_ERROR);
                ro.setMsg("参数不正确-没有填写用户ID");
                return ro;
            }
            if (StringUtils.isBlank(sysId)) {
                ro.setResult(JwtSignResultDic.PARAM_ERROR);
                ro.setMsg("参数不正确-没有填写系统ID");
                return ro;
            }

            try {
                // Prepare JWT with claims set
                long now = System.currentTimeMillis();
                Date expirationTime = new Date(now + expirationMs);
                JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()//
                        .issuer(iss)                                                // 签发者
                        .issueTime(new Date(now))                                   // 签发时间
                        .notBeforeTime(new Date(now))                               // 不接受当前时间在此之前
                        .expirationTime(expirationTime)                             // 过期时间
                        .claim("sysId", sysId)                                      // 放入系统ID
                        .claim("userId", userId)                                    // 放入用户ID
                        .claim("orgId", orgId)                                      // 放入用户的组织ID
                        .build();

                // 计算签名
                String sign = JwtUtils.sign(key, claimsSet);

                String msg = "JWT签名成功";
                _log.info("{}: {}", msg, sign);
                ro.setResult(JwtSignResultDic.SUCCESS);
                ro.setMsg(msg);
                ro.setSign(sign);
                ro.setExpirationTime(expirationTime);
                return ro;
            } catch (JOSEException e) {
                String msg = "JWT签名失败: userId=" + userId;
                _log.error(msg, e);
                ro.setResult(JwtSignResultDic.FAIL);
                ro.setMsg(msg);
                return ro;
            }
        } finally {
            _log.info("\r\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 结束JWT签名 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n");
        }

    }

    /**
     * 验证JWT签名
     * 
     * @param toVerifySign
     *            要验证的签名
     */
    @GetMapping("/jwt/verify")
    JwtVerifyRo verify(@RequestParam("toVerifySign") String toVerifySign) {
        _log.info("\r\n============================= 开始验证JWT签名 =============================\r\n");
        try {
            _log.info("验证JWT签名参数: toVerifySign={}", toVerifySign);

            JwtVerifyRo ro = new JwtVerifyRo();

            if (StringUtils.isBlank(toVerifySign)) {
                ro.setResult(JwtVerifyResultDic.PARAM_ERROR);
                ro.setMsg("参数不正确-没有传入要验证的签名");
                return ro;
            }

            try {
                // 解析JWT签名
                SignedJWT signedJWT = JwtUtils.parse(toVerifySign);

                _log.debug("解析后检查head和payload部分是否正确");
                JOSEObjectType joseObjectType = signedJWT.getHeader().getType();
                if (joseObjectType != null && !JOSEObjectType.JWT.equals(joseObjectType)) {
                    String msg = "验证JWT签名失败-不是JWT的签名";
                    _log.error("{}: {}", msg, joseObjectType);
                    ro.setResult(JwtVerifyResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }
                JWSAlgorithm algorithm = signedJWT.getHeader().getAlgorithm();
                if (!JWSAlgorithm.HS512.equals(algorithm)) {
                    String msg = "验证JWT签名失败-算法不正确";
                    _log.error("{}: {}", msg, algorithm);
                    ro.setResult(JwtVerifyResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }
                String issuer = signedJWT.getJWTClaimsSet().getIssuer();
                if (!iss.equals(issuer)) {
                    String msg = "验证JWT签名失败-签发者不正确";
                    _log.error("{}: {}", msg, issuer);
                    ro.setResult(JwtVerifyResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }
                Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
                if (new Date().after(expirationTime)) {
                    String msg = "验证JWT签名失败-签名过期";
                    _log.error("{}: {}", msg, expirationTime);
                    ro.setResult(JwtVerifyResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }
                Date notBeforeTime = signedJWT.getJWTClaimsSet().getNotBeforeTime();
                if (new Date().before(notBeforeTime)) {
                    String msg = "验证JWT签名失败-当前时间早于签名时间";
                    _log.error("{}: {}", msg, notBeforeTime);
                    ro.setResult(JwtVerifyResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }
                String sysId = (String) signedJWT.getJWTClaimsSet().getClaim("sysId");
                if (StringUtils.isBlank(sysId)) {
                    String msg = "验证JWT签名失败-系统ID为空";
                    _log.error("{}: {}", msg, sysId);
                    ro.setResult(JwtVerifyResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }
                String userId = (String) signedJWT.getJWTClaimsSet().getClaim("userId");
                if (StringUtils.isBlank(userId)) {
                    String msg = "验证JWT签名失败-用户ID为空";
                    _log.error("{}: {}", msg, userId);
                    ro.setResult(JwtVerifyResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }
                Long orgId = (Long) signedJWT.getJWTClaimsSet().getClaim("orgId");
//                if (StringUtils.isBlank(orgId)) {
//                    String msg = "验证JWT签名失败-用户的组织ID为空";
//                    _log.error("{}: {}", msg, orgId);
//                    ro.setResult(JwtVerifyResultDic.FAIL);
//                    ro.setMsg(msg);
//                    return ro;
//                }

                if (!JwtUtils.verify(key, signedJWT)) {
                    String msg = "验证JWT签名失败-签名不正确";
                    _log.error("{}: {}", msg, signedJWT.getSignature());
                    ro.setResult(JwtVerifyResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }

                String msg = "验证JWT签名成功";
                _log.info("{}: userId={}", msg, orgId);
                ro.setResult(JwtVerifyResultDic.SUCCESS);
                ro.setMsg(msg);
                ro.setSysId(sysId);
                ro.setUserId(userId);
                ro.setOrgId(orgId);
                return ro;
            } catch (ParseException e) {
                String msg = "验证JWT签名失败-解析不正确";
                _log.error(msg + ": " + toVerifySign, e);
                ro.setResult(JwtVerifyResultDic.FAIL);
                ro.setMsg(msg);
                return ro;
            } catch (JOSEException e) {
                String msg = "验证JWT签名失败-验证出现异常";
                _log.error(msg + ": " + toVerifySign, e);
                ro.setResult(JwtVerifyResultDic.FAIL);
                ro.setMsg(msg);
                return ro;
            }
        } finally {
            _log.info("\r\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 结束验证JWT签名 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n");
        }
    }
}
