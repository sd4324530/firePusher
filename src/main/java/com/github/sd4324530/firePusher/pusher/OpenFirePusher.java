package com.github.sd4324530.firePusher.pusher;

import com.github.sd4324530.firePusher.FMessage;
import com.github.sd4324530.firePusher.Pusher;
import com.github.sd4324530.firePusher.config.OpenfireParam;
import com.github.sd4324530.firePusher.exception.FirePusherException;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 基于XMPP协议，openfire推送服务器的消息推送器
 *
 * @author peiyu
 */
public class OpenFirePusher implements Pusher {

    private static final Logger LOG = LoggerFactory.getLogger(OpenFirePusher.class);

    private final XMPPConnection connection;

    private final String serverName;

    public OpenFirePusher(final OpenfireParam openfireParam) {
        ConnectionConfiguration configuration = new ConnectionConfiguration(openfireParam.getOpenfireIP(), openfireParam.getOpenfirePort());
        SASLAuthentication.supportSASLMechanism("PLAIN", 0);
        configuration.setCompressionEnabled(true);
        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        configuration.setDebuggerEnabled(false);
        configuration.setReconnectionAllowed(true);
        configuration.setSendPresence(true);
        this.connection = new XMPPTCPConnection(configuration);
        try {
            connection.connect();
            connection.login(openfireParam.getUserName(), openfireParam.getPassword());
            this.serverName = connection.getServiceName();
        } catch (Exception e) {
            LOG.warn("连接openfire服务器异常", e);
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
                LOG.debug("通过XMPP协议推送消息:{}", message.toString());
                Message msg = new Message(message.getTo() + "@" + this.serverName);
                msg.setBody(message.getContext());
                msg.setSubject(message.getTitle());
                try {
                    this.connection.sendPacket(msg);
                } catch (SmackException.NotConnectedException e) {
                    throw new FirePusherException(e);
                } catch (Exception e) {
                    LOG.warn("发送出现异常:{}", e.toString());
                    LOG.warn("继续发送其余消息");
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (null != this.connection) {
            try {
                if (this.connection.isConnected()) {
                    this.connection.disconnect();
                }
            } catch (SmackException.NotConnectedException e) {
                LOG.warn("断开openfire服务器异常", e);
            } finally {
                PusherManager.me().releasePusher(this);
            }
        }
    }
}
