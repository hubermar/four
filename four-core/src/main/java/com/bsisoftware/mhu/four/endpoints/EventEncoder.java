package com.bsisoftware.mhu.four.endpoints;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.bsisoftware.mhu.four.model.Event;
import com.google.gson.Gson;

public class EventEncoder implements Encoder.Text<Event> {
 
    private static Gson gson = new Gson();
 
    @Override
    public String encode(Event in) throws EncodeException {
        return gson.toJson(in);
    }
 
    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }
 
    @Override
    public void destroy() {
        // Close resources
    }
}