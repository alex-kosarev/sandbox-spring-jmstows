package org.acruxsource.sandbox.spring.jmstows.domain;

public class GreetingResponse {

    public GreetingResponse() {
    }

    public GreetingResponse(String greeting) {
        this.greeting = greeting;
    }

    private String greeting;

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
