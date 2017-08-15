package com.bsisoftware.mhu.four.service;

import com.bsisoftware.mhu.four.data.BoardDAO;
import com.bsisoftware.mhu.four.model.Board;
import com.bsisoftware.mhu.four.model.Player;

/**
 * @author mhuber
 * 
 * 5 | # # # # # # #
 * 4 | # # # # # # #
 * 3 | # # # # # # #
 * 2 | # # # # # # #
 * 1 | # # # # # # #
 * 0 | # # # # # # #
 *     -------------
 *     0 1 2 3 4 5 6
 */
public class BoardService {

	private static final int WIN = 4;
	private static final int ROWS = 6;
	private static final int COLUMNS = 7;
	
	private BoardDAO dao = BoardDAO.INSTANCE;
	
	public Board create(String id) {
		Board board = new Board(id, COLUMNS, ROWS);
		board.setPlayer(Player.RED);
		dao.saveBoard(board);
		return board;
	}

	public Board getById(String id) {
		return dao.getBoardById(id);
	}

	public int insertCoin(Board board, Player player, int column) {
		checkColumnNo(board, column);
		Integer freeRow = findFreeRow(board, column);
		checkRowNo(board, freeRow);
		board.setCoin(column, freeRow, player);
		dao.saveBoard(board);
		return freeRow.intValue();
	}
	
	public Player evalWinner(Board board, int column, int row, Player player) {
		if (evalWinVertical(board, player, column, row) || 
				evalWinHorizontal(board, player, column, row) || 
				evalWinDiagonal(board, player, column, row)) {
			return player;
		}
		return Player.UNDEF;
	}

	static boolean evalWinDiagonal(Board board, Player player, int column, int row) {
		boolean result = evalWinDiagonalImpl(board, player, column, row, 1);
		if (!result) {
			result = evalWinDiagonalImpl(board, player, column, row, -1);
		}
		return result;
	}
	
	static boolean evalWinDiagonalImpl(Board board, Player player, int column, int row, int direction) {
		int count = 0;
		for (int i = -WIN * direction; i * direction <= WIN; i += direction) {			
			int x = column + i;
			int y = row + (i * direction);
			if (x < 0 || x > board.getWidth() - 1 || y < 0 || y > board.getHeight() - 1) {
				continue;
			}
			if (board.getCoin(x, y) == player) {
				count++;
			}
		}
		return count >= WIN;
	}

	static boolean evalWinHorizontal(Board board, Player player, int column, int row) {
		int minCol = Math.max(column - WIN + 1, 0);
		int maxCol = Math.min(column + WIN - 1, board.getWidth() - 1);
		int count = 0;
		for (int c = minCol; c <= maxCol; c++) {
			if (board.getCoin(c, row) == player) {
				count++;
			}
		}
		return count >= WIN;
	}

	static boolean evalWinVertical(Board board, Player player, int column, int row) {
		int minRow = Math.max(row - WIN + 1, 0);
		int maxRow = Math.min(row + WIN - 1, board.getHeight() - 1);
		int count = 0;
		for (int r = minRow; r <= maxRow; r++) {
			if (board.getCoin(column, r) == player) {
				count++;
			}
		}
		return count >= WIN;
	}

	static Integer findFreeRow(Board board, Integer column) {
		for (int row = 0; row < ROWS; row++) {
			if (board.getCoin(column, row) == null) {
				return row;
			}
		}
		return null;
	}

	static void checkColumnNo(Board board, Integer column) {
		if (column == null || column < 0 || column >= board.getWidth()) {
			throw new IllegalArgumentException("invalid column " + column + " (allowed 0.." + (board.getWidth() - 1) + ")");
		}
	}

	static void checkRowNo(Board board, Integer row) {
		if (row == null || row < 0 || row >= board.getHeight()) {
			throw new IllegalArgumentException("invalid row " + row + " (allowed 0.." + (board.getHeight() - 1) + ")");
		}
	}

	public static Player evalNextPlayer(Player currentPlayer) {
		switch (currentPlayer) {
		case RED:
			return Player.YELLOW;
		case YELLOW:
			return Player.RED;
		default:
			return Player.UNDEF;
		}
	}
}
