package com.bsisoftware.mhu.four.data;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.bsisoftware.mhu.four.model.Board;

public class BoardDAO {
	
	private static final Logger LOG = Logger.getLogger(BoardDAO.class.getName());

	public static final BoardDAO INSTANCE = new BoardDAO();
	
	private Map<String, Board> boards = new HashMap<>();

	private BoardDAO() {}
	
	public Board getBoardById(String id) {
		return boards.get(id);
	}
	
	public Board saveBoard(Board b) {
		LOG.info("saved board with id=" + b.getId());
		return boards.put(b.getId(), b);
	}

	public void deleteBoard(String id) {
		boards.remove(id);
	}
}
