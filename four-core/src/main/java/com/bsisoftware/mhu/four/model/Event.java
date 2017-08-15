package com.bsisoftware.mhu.four.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public final class Event {

	public enum Payload {
		PLAYER,
		COLUMN,
		ROW
	}
	
	public enum Type {
		// client -> server
		GET_BOARD,
		INSERT_COIN,
		// server -> client
		LOADED_BOARD,
		NEXT_PLAYER, 
		WINNER
	}
	
	@XmlElement(name="type")
	private Type type;
	
	public Type getType() {
		return type;
	}
	
	@XmlElement(name = "payload")
	private Map<Payload, String> payload = new HashMap<>();
	
	public String getPayload(Payload name) {
		return payload.get(name);
	}
	
	@Override
	public String toString() {
		return Event.class.getSimpleName() + "[type=" + type + " payload=" + payload + "]";
	}

	public static Event newDraw(int column, int row, Player player) {
		return create(Type.INSERT_COIN, column, row, player);
	}

	public static Event newNextPlayer(Player player) {
		return create(Type.NEXT_PLAYER, null, null, player);
	}

	public static Event newLoaded(int width, int height, Player currentPlayer) {
		return create(Type.LOADED_BOARD, width, height, currentPlayer);
	}

	public static Event newWinner(Player winner) {
		return create(Type.WINNER, null, null, winner);
	}

	private static Event create(Type type, Integer column, Integer row, Player player) {
		Event e = new Event();
		if (type == null) {
			throw new IllegalArgumentException("type must not be null");
		}
		e.type = type;
		if (column != null) {
			e.payload.put(Payload.COLUMN, Integer.toString(column));
		}
		if (row != null) {
			e.payload.put(Payload.ROW, Integer.toString(row));
		}
		if (player != null) {
			e.payload.put(Payload.PLAYER, player.name());
		}
		return e;
	}
}
