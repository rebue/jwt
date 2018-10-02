package rebue.scx.jwt.ro;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import rebue.scx.jwt.dic.JwtSignResultDic;

/**
 * 签名的返回结果
 */
@Data
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

}
