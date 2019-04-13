package org.tio.showcase.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.WsServerAioListener;

/**
 * 消息处理，根据业务需要
 *
 * @author Chr
 *         用户根据情况来完成该类的实现
 */
public class ShowcaseServerAioListener extends WsServerAioListener {
    private static Logger log = LoggerFactory.getLogger(ShowcaseServerAioListener.class);
    public static final ShowcaseServerAioListener me = new ShowcaseServerAioListener();

    private ShowcaseServerAioListener() {
    }

    /**
     * 建链后触发本方法，注：建链不一定成功，需要关注参数isConnected
     *
     * @param channelContext
     * @param isConnected    是否连接成功,true:表示连接成功，false:表示连接失败
     * @param isReconnect    是否是重连, true: 表示这是重新连接，false: 表示这是第一次连接
     * @throws Exception
     * @author: Chr
     */
    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        super.onAfterConnected(channelContext, isConnected, isReconnect);
        if (log.isInfoEnabled()) {
            log.info("onAfterConnected\r\n{}", channelContext);
        }
    }

    /**
     * 消息包发送之后触发本方法
     *
     * @param channelContext
     * @param packet
     * @param isSentSuccess  true:发送成功，false:发送失败
     * @throws Exception
     * @author Chr
     */
    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        super.onAfterSent(channelContext, packet, isSentSuccess);
        if (log.isInfoEnabled()) {
            log.info("onAfterSent\r\n{}\r\n{}", packet.logstr(), channelContext);
        }
    }

    /**
     * 连接关闭前触发本方法
     *
     * @param channelContext the channelcontext
     * @param throwable      the throwable 有可能为空
     * @param remark         the remark 有可能为空
     * @param isRemove
     * @throws Exception
     * @author Chr
     */
    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        super.onBeforeClose(channelContext, throwable, remark, isRemove);
        if (log.isInfoEnabled()) {
            log.info("onBeforeClose\r\n{}", channelContext);
        }
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        if (wsSessionContext != null && wsSessionContext.isHandshaked()) {
            int count = Tio.getAllChannelContexts(channelContext.groupContext).getObj().size();
            String msg = channelContext.getClientNode().toString() + " 离开了，现在共有【" + count + "】人在线";
            //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
            WsResponse wsResponse = WsResponse.fromText(msg, ShowcaseServerConfig.CHARSET);
            //群发
            Tio.sendToGroup(channelContext.groupContext, Const.GROUP_ID, wsResponse);
        }
    }

    /**
     * 原方法名：onAfterDecoded
     * 解码成功后触发本方法
     *
     * @param channelContext
     * @param packet
     * @param packetSize
     * @throws Exception
     * @author: Chr
     */
    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
        super.onAfterDecoded(channelContext, packet, packetSize);
        if (log.isInfoEnabled()) {
            log.info("onAfterDecoded\r\n{}\r\n{}", packet.logstr(), channelContext);
        }
    }

    /**
     * 接收到TCP层传过来的数据后
     *
     * @param channelContext
     * @param receivedBytes  本次接收了多少字节
     * @throws Exception
     */
    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
        super.onAfterReceivedBytes(channelContext, receivedBytes);
        if (log.isInfoEnabled()) {
            log.info("onAfterReceivedBytes\r\n{}", channelContext);
        }
    }

    /**
     * 处理一个消息包后
     *
     * @param channelContext
     * @param packet
     * @param cost           本次处理消息耗时，单位：毫秒
     * @throws Exception
     */
    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
        super.onAfterHandled(channelContext, packet, cost);
        if (log.isInfoEnabled()) {
            log.info("onAfterHandled\r\n{}\r\n{}", packet.logstr(), channelContext);
        }
    }

    /**
     * 连接关闭前后触发本方法
     * 警告：走到这个里面时，很多绑定的业务都已经解绑了，所以这个方法一般是空着不实现的
     * @param channelContext the channelcontext
     * @param throwable the throwable 有可能为空
     * @param remark the remark 有可能为空
     * @param isRemove 是否是删除
     * @throws Exception
     * @author: Chr
     */
//    public void onAfterClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception;
}