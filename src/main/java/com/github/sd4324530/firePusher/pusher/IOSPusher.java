package com.github.sd4324530.firePusher.pusher;

import com.github.sd4324530.firePusher.FMessage;
import com.github.sd4324530.firePusher.Pusher;
import com.github.sd4324530.firePusher.config.IOSParam;
import com.github.sd4324530.firePusher.exception.FirePusherException;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 苹果推送服务器消息推送器
 *
 * @author peiyu
 */
public class IOSPusher implements Pusher {

    private static final Logger LOG = LoggerFactory.getLogger(IOSPusher.class);

    private final PushNotificationManager pushNotificationManager;

    private final PushNotificationPayload payLoad;

    public IOSPusher(final IOSParam iosParam) {
        this.pushNotificationManager = new PushNotificationManager();
        this.payLoad = new PushNotificationPayload();
        try {
            this.payLoad.addBadge(1);
            this.payLoad.addSound("default");
            //true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
            this.pushNotificationManager.initializeConnection(new AppleNotificationServerBasicImpl(iosParam.getP12Path(), iosParam.getPassword(), true));
        } catch (Exception e) {
            LOG.warn("连接苹果服务器失败", e);
            throw new FirePusherException(e);
        }
    }

    @Override
    public void push(final FMessage message) throws FirePusherException {
        this.push(Arrays.asList(message));
    }

    @Override
    public void push(final List<FMessage> messages) throws FirePusherException {
        if (null != messages && !messages.isEmpty()) {
            for (FMessage message : messages) {
                try {
                    this.payLoad.addAlert(message.getContext());
                    this.pushNotificationManager.sendNotification(new BasicDevice(message.getTo()), payLoad, false);
                } catch (Exception e) {
                    LOG.warn("发送出现异常:{}", e.toString());
                    LOG.warn("继续发送其余消息");
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.pushNotificationManager.stopConnection();
            PusherManager.me().releasePusher(this);
        } catch (CommunicationException e) {
            LOG.error("断开苹果推送服务器异常", e);
        } catch (KeystoreException e) {
            LOG.error("断开苹果推送服务器异常", e);
        }
    }
}
