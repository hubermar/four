package com.bsisoftware.mhu.four.model;

public enum Player {
	RED,
	YELLOW,
	UNDEF;

	public Player next() {
		switch (this) {
		case RED:
			return YELLOW;
		case YELLOW:
			return RED;
		default:
			return Player.UNDEF;
		}
	}
}
