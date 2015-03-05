package com.github.sd4324530.firePusher.config;

/**
 * 推送参数接口
 *
 * @author peiyu
 */
public interface PushParam {

    /**
     * 参数类型枚举，目前包括openfire推送服务器以及苹果推送服务器
     */
    public static enum ParamType {
        OPENFIRE, IOS
    }

    /**
     * 获取参数类型
     *
     * @return 参数类型
     */
    ParamType getType();
}
