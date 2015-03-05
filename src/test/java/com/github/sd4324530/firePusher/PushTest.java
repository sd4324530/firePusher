package com.github.sd4324530.firePusher;

import com.github.sd4324530.firePusher.config.IOSParam;
import com.github.sd4324530.firePusher.config.OpenfireParam;
import com.github.sd4324530.firePusher.message.SimpleFMessage;
import com.github.sd4324530.firePusher.pusher.PusherManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author peiyu
 */
public class PushTest {

    private static final Logger LOG = LoggerFactory.getLogger(PushTest.class);

    @Test
    public void test() {
//        testOpenfire();
        testIOS();
    }

    private void testIOS() {
        PusherManager pusherManager = PusherManager.me();
        IOSParam iosParam = new IOSParam();
        iosParam.setP12Path("E:/Certificates.p12");
        iosParam.setPassword("123456");
        Pusher pusher = pusherManager.getPusher(iosParam);
        SimpleFMessage simpleFMessage = new SimpleFMessage();
        simpleFMessage.setContext("test message!");
        simpleFMessage.setTitle("hello test");
        simpleFMessage.setTo("1ad18d84a40437f7a1b949c95cd2686d0bbb21645b5d996e335920b64b1f4f38");
        pusher.push(simpleFMessage);
        try {
            pusher.close();
        } catch (IOException e) {
            LOG.error("关闭连接异常");
        }
        LOG.debug("通过苹果推送服务器发送消息成功......");
    }

    private void testOpenfire() {
        PusherManager pusherManager = PusherManager.me();
        OpenfireParam openfireParam = new OpenfireParam();
        openfireParam.setOpenfireIP("10.20.16.74");
        openfireParam.setOpenfirePort(5222);
        openfireParam.setUserName("admin");
        openfireParam.setPassword("123456");
        Pusher pusher = pusherManager.getPusher(openfireParam);

        SimpleFMessage simpleFMessage = new SimpleFMessage();
        simpleFMessage.setContext("test message!");
        simpleFMessage.setTitle("hello test");
        simpleFMessage.setTo("test1");
        pusher.push(simpleFMessage);
        try {
            pusher.close();
        } catch (IOException e) {
            LOG.error("关闭连接异常");
        }
        LOG.debug("通过xmpp服务器发送消息成功......");
    }
}
