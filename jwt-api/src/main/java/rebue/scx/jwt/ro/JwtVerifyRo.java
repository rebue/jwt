package rebue.scx.jwt.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import rebue.scx.jwt.dic.JwtVerifyResultDic;

/**
 * 验证签名的返回结果
 */
@JsonInclude(Include.NON_NULL)
@Data
public class JwtVerifyRo {
    /**
     * 验证签名返回结果的代码
     */
    private JwtVerifyResultDic result;
    /**
     * 验证签名返回结果的代码信息
     */
    private String             msg;

    /**
     * 用户ID
     */
    private String             userId;
    /**
     * 用户的组织ID
     */
    private Long               orgId;
    /**
     * 系统ID
     */
    private String             sysId;

}
