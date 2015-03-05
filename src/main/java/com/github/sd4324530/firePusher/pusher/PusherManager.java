package com.github.sd4324530.firePusher.pusher;

import com.github.sd4324530.firePusher.Pusher;
import com.github.sd4324530.firePusher.config.IOSParam;
import com.github.sd4324530.firePusher.config.OpenfireParam;
import com.github.sd4324530.firePusher.config.PushParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送器管理类
 *
 * @author peiyu
 */
public final class PusherManager {

    private static final Logger LOG = LoggerFactory.getLogger(PusherManager.class);

    /**
     * 用于管理所有已经生成的推送器
     */
    private final Map<String, Pusher> paramCache = new HashMap<String, Pusher>();

    private PusherManager() {
    }

    /**
     * 获取管理类单例
     *
     * @return 管理类单例
     */
    public static PusherManager me() {
        return PusherManagerHandler.ME;
    }

    /**
     * 获取消息推送器
     *
     * @param param 推送配置参数
     * @return 推送器
     */
    public Pusher getPusher(final PushParam param) {
        Pusher pusher = null;
        switch (param.getType()) {
            case IOS:
                IOSParam iosParam = (IOSParam) param;
                pusher = this.paramCache.get(iosParam.getP12Path());
                if (null == pusher) {
                    pusher = new IOSPusher(iosParam);
                    this.paramCache.put(iosParam.getP12Path(), pusher);
                }
                break;
            case OPENFIRE:
                OpenfireParam openfireParam = (OpenfireParam) param;
                pusher = this.paramCache.get(openfireParam.getOpenfireIP());
                if (null == pusher) {
                    pusher = new OpenFirePusher(openfireParam);
                    this.paramCache.put(openfireParam.getOpenfireIP(), pusher);
                }
                break;
            default:
                break;
        }
        return pusher;
    }

    /**
     * 回收释放消息推送器，逻辑还不太好，需要加锁以防止数据错误
     *
     * @param pusher 需要回收的推送器
     */
    public synchronized void releasePusher(final Pusher pusher) {
        String key = "";
        for (Map.Entry<String, Pusher> e : this.paramCache.entrySet()) {
            if (e.getValue().equals(pusher)) {
                key = e.getKey();
                break;
            }
        }
        if (!"".equals(key.trim())) {
            this.paramCache.remove(key);
        }
    }

    private static class PusherManagerHandler {
        private static PusherManager ME = new PusherManager();
    }
}
