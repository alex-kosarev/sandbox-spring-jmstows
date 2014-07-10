package org.acruxsource.sandbox.spring.jmstows.controllers;

import org.acruxsource.sandbox.spring.jmstows.domain.GreetingRequest;
import org.acruxsource.sandbox.spring.jmstows.domain.GreetingResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/websocket")
public class WebSocketController {
    
    @MessageMapping("/greetings")
    @SendTo("/greetings")
    public GreetingResponse greeting(GreetingRequest greetingRequest) {
        return new GreetingResponse("G'day, " + greetingRequest.getName());
    }
    
}
