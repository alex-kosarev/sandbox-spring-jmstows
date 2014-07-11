/*
 *
 * Copyright 2014 Alexander Kosarev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.acruxsource.sandbox.spring.jmstows.jms;

import java.io.IOException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.acruxsource.sandbox.spring.jmstows.domain.ChatMessage;
import org.acruxsource.sandbox.spring.jmstows.websocket.ChatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketOutMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketOutMessageListener.class);

    private ChatHandler webSocketHandler;

    public void setWebSocketHandler(ChatHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                webSocketHandler.sendMessage((ChatMessage) objectMessage.getObject());
            } catch (JMSException | IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

}
