package org.acruxsource.sandbox.spring.jmstows.jms;

import java.io.IOException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.acruxsource.sandbox.spring.jmstows.websocket.GreetingsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketOutMessageListener implements MessageListener {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketOutMessageListener.class);

    private GreetingsHandler webSocketHandler;

    public void setWebSocketHandler(GreetingsHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                webSocketHandler.sendMessage(textMessage.getText());
            } catch (JMSException | IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

}
