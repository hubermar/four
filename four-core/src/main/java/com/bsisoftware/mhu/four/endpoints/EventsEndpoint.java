package com.bsisoftware.mhu.four.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bsisoftware.mhu.four.model.Board;
import com.bsisoftware.mhu.four.model.Event;
import com.bsisoftware.mhu.four.model.Event.Payload;
import com.bsisoftware.mhu.four.model.Player;
import com.bsisoftware.mhu.four.service.BoardService;
import com.google.gson.Gson;

@ServerEndpoint(
		value = "/events",
		decoders = EventDecoder.class,
		encoders = EventEncoder.class)
public class EventsEndpoint {
	
	private static final Logger LOG = Logger.getLogger(EventsEndpoint.class.getName());
	
	private BoardService service = new BoardService();

    @OnOpen
    public void onOpen(Session session) throws IOException {
    	service.create(session.getId());
    }
 
    @OnMessage
    public void onMessage(Session session, Event incoming) throws IOException {
		LOG.info("got incoming event=" + incoming);
		List<Event> outgoing = handleEvent(session.getId(), incoming);
		broadcast(session, outgoing);
    }
 
    private void broadcast(Session session, List<Event> events) {
    	events.stream().forEach(e -> broadcast(session, e));
	}

	private void broadcast(Session session, Event event) {
		Gson gson = new Gson();
		Set<Session> allSessions = session.getOpenSessions();
		for (Session s : allSessions) {
			try {
				String json = gson.toJson(event);
				s.getBasicRemote().sendObject(json);
			} catch (IOException|EncodeException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@OnClose
    public void onClose(Session session) throws IOException {
		service.remove(session.getId());
        // WebSocket connection closes
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
    	throwable.printStackTrace();
    	LOG.severe("error in session " + session.getId() + ": " + throwable.getMessage());
    }
    
	private List<Event> handleEvent(String id, Event event) {
		List<Event> outgoing = new ArrayList<>();
		switch (event.getType()) {
		case REGISTER:
			outgoing.addAll(handleRegister(id));
		case INSERT_COIN:
			outgoing.addAll(handleInsert(id, event));
		case GET_BOARD:
			outgoing.addAll(handleGetBoard(id));
		default:
			LOG.warning("Unknown event=" + event);
		}
		return outgoing;
	}

	private List<Event> handleRegister(String id) {
		return Arrays.asList(Event.newRegister());
	}

	private List<Event> handleGetBoard(String id) {
		Board board = service.getById(id);
		if (board == null) {
			board = service.create(id);
		}
		return Arrays.asList(Event.newLoaded(board.getWidth(), board.getHeight(), board.getPlayer()));
	}

	private List<Event> handleInsert(String id, Event incoming) {
		Board board = service.getById(id);
		Player p = Player.valueOf(incoming.getPayload(Payload.PLAYER));
		Integer column = Integer.valueOf(incoming.getPayload(Payload.COLUMN));
		int rowInserted = service.insertCoin(board, p, column);
		Player winner = service.evalWinner(board, column, rowInserted, p);
		return Arrays.asList(
			Event.newInsert(column, rowInserted, p),
			winner == Player.UNDEF ? Event.newNextPlayer(p.next()) : Event.newWinner(winner)
		);
	}
}