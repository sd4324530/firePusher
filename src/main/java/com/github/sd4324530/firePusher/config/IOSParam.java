package com.github.sd4324530.firePusher.config;

/**
 * 苹果推送服务器相关参数配置类
 *
 * @author peiyu
 */
public class IOSParam implements PushParam {
    /**
     * 推送证书地址
     */
    private String p12Path;
    /**
     * 密钥
     */
    private String password;

    public String getP12Path() {
        return p12Path;
    }

    public void setP12Path(String p12Path) {
        this.p12Path = p12Path;
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
        return ParamType.IOS;
    }
}
