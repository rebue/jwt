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
    private String             msg;

    /**
     * 用户ID
     */
    private String             userId;
    /**
     * 用户的组织ID
     */
    private String             orgId;
    /**
     * 系统ID
     */
    private String             sysId;

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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    @Override
    public String toString() {
        return "JwtVerifyRo [result=" + result + ", msg=" + msg + ", userId=" + userId + ", orgId=" + orgId + ", sysId=" + sysId + "]";
    }

}
