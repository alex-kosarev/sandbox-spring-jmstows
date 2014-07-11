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
package org.acruxsource.sandbox.spring.jmstows.websocket;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.acruxsource.sandbox.spring.jmstows.domain.ChatMessage;
import org.acruxsource.sandbox.spring.jmstows.jms.JmsMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatHandler extends TextWebSocketHandler {

    private final static Logger logger = LoggerFactory.getLogger(ChatHandler.class);
    private final static Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>());
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    private JmsMessageSender jmsMessageSender;
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("User has left the chat");

        super.afterConnectionClosed(session, status);
        sessions.remove(session);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setName("server");
        chatMessage.setMessage("User left the chat. Users in chat: " + sessions.size());
        jmsMessageSender.sendMessage("websocket.out", chatMessage);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("New user has joined the chat");

        super.afterConnectionEstablished(session);
        sessions.add(session);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setName("server");
        chatMessage.setMessage("New user joined the chat. Users in chat: " + sessions.size());
        jmsMessageSender.sendMessage("websocket.out", chatMessage);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("New incoming message: " + message.getPayload());

        StringReader stringReader = new StringReader(message.getPayload());
        JsonReader jsonReader = Json.createReader(stringReader);
        JsonObject chatMessageObject = jsonReader.readObject();

        if (chatMessageObject.getJsonString("name") != null && chatMessageObject.getJsonString("message") != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setName(chatMessageObject.getJsonString("name").getString());
            chatMessage.setMessage(chatMessageObject.getJsonString("message").getString());
            jmsMessageSender.sendMessage("websocket.out", chatMessage);
        } else {
            session.sendMessage(new TextMessage("Empty name or message"));
        }
    }

    public void sendMessage(final ChatMessage message) throws IOException {
        logger.info("Sending message to all participants");

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("name", message.getName());
                jsonObjectBuilder.add("message", message.getMessage());
                jsonObjectBuilder.add("date", dateFormat.format(message.getDate()));
                session.sendMessage(new TextMessage(jsonObjectBuilder.build().toString()));
            } else {
                sessions.remove(session);
            }
        }
    }

    public void setJmsMessageSender(JmsMessageSender jmsMessageSender) {
        this.jmsMessageSender = jmsMessageSender;
    }

}
