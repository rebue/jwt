package rebue.scx.jwt.ro;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.scx.jwt.dic.JwtSignResultDic;

/**
 * 签名的返回结果
 */
@JsonInclude(Include.NON_NULL)
public class JwtSignRo {
    /**
     * 签名返回结果的代码
     */
    private JwtSignResultDic result;
    /**
     * 签名返回结果的代码信息
     */
    private String           msg;

    /**
     * 签名
     */
    private String           sign;

    /**
     * 超时时间
     */
    private Date             expirationTime;

    public JwtSignResultDic getResult() {
        return result;
    }

    public void setResult(JwtSignResultDic result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String toString() {
        return "JwtSignRo [result=" + result + ", msg=" + msg + ", sign=" + sign + ", expirationTime=" + expirationTime + "]";
    }

}
