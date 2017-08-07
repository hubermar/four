package com.bsisoftware.mhu.four.endpoints;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.bsisoftware.mhu.four.model.Event;
import com.google.gson.Gson;

public class EventDecoder implements Decoder.Text<Event> {
 
    private static Gson gson = new Gson();
 
    @Override
    public Event decode(String s) throws DecodeException {
        return gson.fromJson(s, Event.class);
    }
 
    @Override
    public boolean willDecode(String s) {
        return (s != null);
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