package com.github.sd4324530.firePusher.config;

/**
 * 基于openfire服务器作为推送服务器参数配置类
 *
 * @author peiyu
 */
public class OpenfireParam implements PushParam {

    /**
     * 服务器IP
     */
    private String openfireIP;

    /**
     * 服务器端口
     */
    private int openfirePort;

    /**
     * 登录帐号
     */
    private String userName;

    /**
     * 登录密码
     */
    private String password;

    public String getOpenfireIP() {
        return openfireIP;
    }

    public void setOpenfireIP(String openfireIP) {
        this.openfireIP = openfireIP;
    }

    public int getOpenfirePort() {
        return openfirePort;
    }

    public void setOpenfirePort(int openfirePort) {
        this.openfirePort = openfirePort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取参数类型
     *
     * @return 参数类型
     */
    @Override
    public ParamType getType() {
        return ParamType.OPENFIRE;
    }
}
