package rebue.scx.jwt.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.scx.jwt.dic.JwtVerifyResultDic;

/**
 * 验证签名的返回结果
 */
@JsonInclude(Include.NON_NULL)
public class JwtVerifyRo {
    /**
     * 验证签名返回结果的代码
     */
    private JwtVerifyResultDic result;
    /**
     * 验证签名返回结果的代码信息
     */
    private String          msg;

    /**
     * 用户ID
     */
    private String          userId;

    public JwtVerifyResultDic getResult() {
        return result;
    }

    public void setResult(JwtVerifyResultDic result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "JwtVerifyRo [result=" + result + ", msg=" + msg + ", userId=" + userId + "]";
    }

}
