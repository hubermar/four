package com.bsisoftware.mhu.four.model;

public class Board {

	private String id;
	
	private int width;
	
	private int height;
	
	private Player player;
	
	private Player[][] coins;

	public Board(String id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.coins = new Player[width][height];
	}
	
	public String getId() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getCoin(int column, int row) {
		return coins[column][row];
	}

	public void setCoin(int column, int row, Player coin) {
		coins[column][row] = coin;
	}
}
