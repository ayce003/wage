package com.ycjd.payslip.service.websocket;

import com.ycjd.payslip.dao.pushmsg.IPushMsgDao;
import com.ycjd.payslip.pojo.pushmsg.PushMsg;
import com.ycjd.payslip.service.pushmsg.PushMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/push/{loginid}")//标记此类为服务端
public class WebSocketServer {

    /**
     * 全部在线会话  PS: 基于场景考虑 这里使用线程安全的Map存储会话对象。
     */
    public static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    @Autowired
    private static PushMsgService pushMsgService;
    private String loginid;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setPushMsgService(PushMsgService pushMsgService) {
        WebSocketServer.pushMsgService = pushMsgService;
    }

    /**
     * 当客户端打开连接：1.添加会话对象 2.更新在线人数
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("loginid") String loginid) {
        System.out.println("有新对象连接");
        this.loginid = loginid;
        onlineSessions.put(loginid, session);
    }

    /**
     * 当客户端发送消息：1.获取它的用户名和消息 2.发送消息给所有人
     * <p>
     * PS: 这里约定传递的消息为JSON字符串 方便传递更多参数！
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        System.out.println(jsonStr);
    }


    /**
     * 当关闭连接：1.移除会话对象 2.更新在线人数
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println(this.loginid);
        onlineSessions.remove(this.loginid);
    }

    /**
     * 当通信发生异常：打印错误日志
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 公共方法：发送信息给所有人
     */
    private static void sendMessageToAll(String msg) {
        onlineSessions.forEach((id, session) -> {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
