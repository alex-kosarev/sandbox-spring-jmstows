package org.acruxsource.sandbox.spring.jmstows.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.acruxsource.sandbox.spring.jmstows.jms.JmsMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GreetingsHandler extends TextWebSocketHandler {
    
    private final static Logger logger = LoggerFactory.getLogger(GreetingsHandler.class);
    
    private final static Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>());
    
    private JmsMessageSender jmsMessageSender;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info(message.getPayload());
        jmsMessageSender.sendMessage("websocket.in", message.getPayload());
    }
    
    public void sendMessage(final String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            } else {
                sessions.remove(session);
            }
        }
    }

    public void setJmsMessageSender(JmsMessageSender jmsMessageSender) {
        this.jmsMessageSender = jmsMessageSender;
    }
    
}
