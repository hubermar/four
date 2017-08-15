package com.bsisoftware.mhu.four.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bsisoftware.mhu.four.model.Board;
import com.bsisoftware.mhu.four.model.Player;

public class TestBoardService {

	@Test
	public void testEvalWinHorizontal() {
		Board board = new Board("test", 7, 6);
		board.setCoin(1, 1, Player.RED);
		board.setCoin(2, 1, Player.RED);
		board.setCoin(3, 1, Player.RED);
		board.setCoin(4, 1, Player.RED);

		boolean expected = true;
		boolean actual = BoardService.evalWinHorizontal(board, Player.RED, 1, 1);
		assertEquals(expected, actual);
	}

	@Test
	public void testEvalWinVertical() {
		Board board = new Board("test", 7, 6);
		board.setCoin(1, 1, Player.RED);
		board.setCoin(1, 2, Player.RED);
		board.setCoin(1, 3, Player.RED);
		board.setCoin(1, 4, Player.RED);

		boolean expected = true;
		boolean actual = BoardService.evalWinVertical(board, Player.RED, 1, 1);
		assertEquals(expected, actual);
	}

	@Test
	public void testEvalWinDiagonalBottomLeftToTopRight() {
		Board board = new Board("test", 7, 6);
		board.setCoin(2, 1, Player.RED);
		board.setCoin(3, 2, Player.RED);
		board.setCoin(4, 3, Player.RED);
		board.setCoin(5, 4, Player.RED);

		boolean expected = true;
		boolean actual = BoardService.evalWinDiagonal(board, Player.RED, 2, 1);
		assertEquals(expected, actual);
	}

	@Test
	public void testEvalWinDiagonalTopRightToBottomLeft() {
		Board board = new Board("test", 7, 6);
		board.setCoin(3, 4, Player.RED);
		board.setCoin(4, 3, Player.RED);
		board.setCoin(5, 2, Player.RED);
		board.setCoin(6, 1, Player.RED);

		boolean expected = true;
		boolean actual = BoardService.evalWinDiagonal(board, Player.RED, 4, 3);
		assertEquals(expected, actual);
	}
}
