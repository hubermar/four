package com.bsisoftware.mhu.four.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public final class EventQueue {

	private static final Logger LOG = Logger.getLogger(EventQueue.class.getName());
	
	private static final BlockingQueue<Event> inQueue = new LinkedBlockingQueue<>();
	private static final List<Event> outQueue = new ArrayList<>();

	private EventQueue() { }

	public static Event pullIncoming() throws InterruptedException {
		Event event = inQueue.take();
		LOG.info("Pulled incoming, new size=" + inQueue.size() + " event=" + event);
		return event;
	}
	
	public static void pushIncoming(Event event) {
		try {
			inQueue.put(event);
			LOG.info("Pushed incoming, new size=" + inQueue.size() + " event=" + event);
		} catch (InterruptedException e) {
			LOG.warning("Pull incoming failed: " + e.getMessage());
		}
	}
	
	public static List<Event> removeOutgoing() {
		List<Event> events = new ArrayList<>(outQueue);
		outQueue.clear();
		if (events.size() > 0) {
			LOG.info("removed outgoing new size=" + outQueue.size() + " events.size=" + events.size());		
		}
		return events;
	}

	public static void addOutgoing(List<Event> events) {
		outQueue.addAll(events);
		LOG.info("added outgoing new size=" + outQueue.size() + " events=" + events);		
	}
	
}
