package rebue.scx.jwt.to;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 签名中储存的用户信息
 */
@Data
@JsonInclude(Include.NON_NULL)
public class JwtUserInfoTo {
    /**
     * 用户ID
     */
    private String              userId;
    /**
     * 系统ID
     */
    private String              sysId;
    /**
     * 用户的附加信息
     */
    private Map<String, Object> addition;

}
