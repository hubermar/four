package com.bsisoftware.mhu.four.model;

public enum Player {
	ONE,
	TWO,
	UNDEF;

	public Player next() {
		switch (this) {
		case ONE:
			return TWO;
		case TWO:
			return ONE;
		default:
			return Player.UNDEF;
		}
	}
}
