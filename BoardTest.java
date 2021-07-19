import junit.framework.TestCase;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Make  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.

	public void testBoardPlace1(){
		Board board = new Board(3,6);
		int tester = board.place(new Piece(Piece.PYRAMID_STR),0,0);
		board.commit();
		board.clearRows();
		System.out.println(board.toString());
		assertEquals(tester,1);
		assertEquals(board.getMaxHeight(),1);
		assertEquals(board.getColumnHeight(1),1);
		board.undo();
		assertEquals(board.getRowWidth(0),3);
		assertEquals(board.getMaxHeight(),2);
		assertEquals(board.getColumnHeight(1),2);
		System.out.println(board.toString());
	}

	public void testBoardPlace2(){
		Board board = new Board(3,6);
		board.place(new Piece(Piece.PYRAMID_STR),0,0);
		board.commit();
		int tester = board.place(new Piece(Piece.STICK_STR),0,0);
		assertEquals(tester,Board.PLACE_BAD);
		board.undo();
		tester = board.place(new Piece(Piece.STICK_STR),0,3);
		assertEquals(tester,Board.PLACE_OUT_BOUNDS);
		tester = board.place(new Piece(Piece.L1_STR),0,2);
		assertEquals(tester,0);
		board.commit();
		//System.out.println(board.toString());
	}
	public void testBoardPlace3(){
		Board board = new Board(3,6);
		Piece [] myPieces = Piece.getPieces();
		Piece testPiece1 = new Piece(Piece.L2_STR);
		testPiece1 = testPiece1.computeNextRotation();
		int tester = board.place(testPiece1,0,0);
		assertEquals(tester,1);
		board.undo();
		tester = board.place(new Piece(Piece.SQUARE_STR),0,0);
		board.commit();
		assertEquals(tester,0);
		tester = board.place(new Piece(Piece.SQUARE_STR),1,2);
		board.commit();
		assertEquals(tester,0);
		System.out.println(board.toString());
		assertEquals(board.getMaxHeight(),4);
		Piece testPiece2 = myPieces[1];
		testPiece2 = testPiece2.fastRotation();
		testPiece2 = testPiece2.fastRotation();
		testPiece2 = testPiece2.fastRotation();
		board.commit();
		tester = board.place(testPiece2,0,3);
		assertEquals(tester,1);
		board.clearRows();
		board.commit();
		tester = board.place(testPiece2,0,2);
		assertEquals(tester,1);
		board.clearRows();
		board.commit();
		assertEquals(board.getMaxHeight(),2);
		assertEquals(board.getColumnHeight(0),2);
		assertEquals(board.getColumnHeight(1),2);
	}
	public void testBoardPlace4(){
		Board board = new Board(3,6);
		int tester = board.place(new Piece(Piece.SQUARE_STR),6,3);
		assertEquals(tester,Board.PLACE_OUT_BOUNDS);
	}


	public void testClearTetris(){
		Board board = new Board(3,6);
		for(int i = 0;i < board.getWidth();i++){
			board.place(new Piece(Piece.STICK_STR),i,0);
			board.commit();
		}
		board.clearRows();
		assertEquals(board.getMaxHeight(),0);
		assertEquals(board.getRowWidth(0),0);
		assertEquals(board.getRowWidth(1),0);
		assertEquals(board.getRowWidth(2),0);
		assertEquals(board.getRowWidth(3),0);
		board.commit();
		board.place(new Piece(Piece.SQUARE_STR),0,0);
		board.commit();
		board.place(new Piece(Piece.STICK_STR),2,0);
		board.commit();
		board.clearRows();
		board.commit();
		assertEquals(true,board.getGrid(2,0));
		assertEquals(true,board.getGrid(2,1));
		assertEquals(board.getMaxHeight(),2);
		board.clearRows();
		assertEquals(true,board.getGrid(2,0));
		assertEquals(true,board.getGrid(2,1));
		assertEquals(board.getMaxHeight(),2);
	}

	public void testClearTetris1(){
		Board board = new Board(3,6);
		board.place(new Piece(Piece.L1_STR),0,0);
		board.commit();
		board.place(new Piece(Piece.STICK_STR),2,0);
		board.commit();
		assertEquals(board.getMaxHeight(),4);
		assertEquals(board.getColumnHeight(0),3);
		assertEquals(board.getColumnHeight(2),4);
		assertEquals(board.getColumnHeight(1),1);
		board.clearRows();
		assertEquals(board.getMaxHeight(),3);
		assertEquals(board.getColumnHeight(0),2);
		assertEquals(board.getColumnHeight(2),3);
		System.out.println(board.toString());
	}



	public void testClearTetris2(){
		Board board = new Board(3,8);
		for(int i = 0;i < board.getWidth();i++){
			board.place(new Piece(Piece.STICK_STR),i,0);
			board.commit();
			board.place(new Piece(Piece.STICK_STR),i,4);
			board.commit();
		}
		board.clearRows();
		board.commit();
		for(int i = 0;i < board.getHeight();i++){
			assertEquals(board.getRowWidth(i),0);
		}
		for(int i = 0;i < board.getWidth();i++){
			board.place(new Piece(Piece.STICK_STR),i,4);
			board.commit();
		}
		for(int i = 0;i < board.getWidth();i++){
			assertEquals(board.getColumnHeight(i),8);
		}
		board.clearRows();
		board.commit();
		for(int i = 4;i < board.getHeight();i++){
			assertEquals(board.getRowWidth(i),0);
		}
		board.place(new Piece(Piece.PYRAMID_STR),0,0);
		board.commit();
		board.place(new Piece(Piece.PYRAMID_STR),0,2);
		board.commit();
		board.place(new Piece(Piece.PYRAMID_STR),0,4);
		board.commit();
		board.clearRows();
		assertEquals(board.getColumnHeight(1),3);
	}


	public void testDropHeight1(){
		Board board = new Board(3,9);
		assertEquals(board.dropHeight(new Piece(Piece.PYRAMID_STR),0),0);
		board.place(new Piece(Piece.PYRAMID_STR),0,0);
		board.commit();
		assertEquals(board.dropHeight(new Piece(Piece.PYRAMID_STR),0),2);
		assertEquals(board.dropHeight(new Piece(Piece.STICK_STR),0),1);
		assertEquals(board.dropHeight(new Piece(Piece.SQUARE_STR),1),2);
	}

	public void testDropHeight2(){
		Board board = new Board(3,9);
		assertEquals(board.dropHeight(new Piece(Piece.PYRAMID_STR),0),0);
		board.place(new Piece(Piece.PYRAMID_STR),0,0);
		board.commit();
		assertEquals(board.dropHeight(new Piece(Piece.PYRAMID_STR),0),2);
		assertEquals(board.dropHeight(new Piece(Piece.STICK_STR),0),1);
		assertEquals(board.dropHeight(new Piece(Piece.SQUARE_STR),1),2);
	}

	public void testDropHeight3(){
		Board board = new Board(4,9);
		assertEquals(board.dropHeight(new Piece(Piece.STICK_STR).computeNextRotation(),0),0);
		board.place(new Piece(Piece.STICK_STR).computeNextRotation(),0,0);
		assertEquals(board.getRowWidth(0),4);
		board.undo();
		assertEquals(board.getRowWidth(0),0);
		board.commit();
		board.place(new Piece(Piece.PYRAMID_STR),1,0);
		board.commit();
		board.undo();
		assertEquals(board.getRowWidth(0),3);
	}
	public void testUndoAndCommit(){
		Board board = new Board(4,12);
		assertEquals(board.getMaxHeight(),0);
		board.place(new Piece(Piece.STICK_STR),0,0);
		assertEquals(board.getMaxHeight(),4);
		board.undo();
		assertEquals(board.getMaxHeight(),0);
		board.place(new Piece(Piece.STICK_STR),0,0);
		board.commit();
		board.undo();
		assertEquals(board.getMaxHeight(),4);
		board.clearRows();
		board.commit();
		board.place(new Piece(Piece.PYRAMID_STR),1,0);
		assertEquals(board.getRowWidth(0),4);
		assertEquals(board.getMaxHeight(),4);
		board.clearRows();
		assertEquals(board.getRowWidth(0),2);
		assertEquals(board.getMaxHeight(),3);
		board.undo();
		assertEquals(board.getRowWidth(0),1);
		assertEquals(board.getMaxHeight(),4);
		board.commit();
		board.place(new Piece(Piece.SQUARE_STR),2,2);
		board.commit();
	}
	public void testUndoAndCommit2(){
		Board board = new Board(3,6);
		assertEquals(board.getMaxHeight(),0);
		board.place(new Piece(Piece.STICK_STR),0,0);
		board.clearRows();
		board.undo();
		assertEquals(board.getMaxHeight(),0);
		board.clearRows();
		board.commit();
		board.place(new Piece(Piece.L1_STR).computeNextRotation(),0,0);
		assertEquals(board.getRowWidth(0),3);
		board.undo();
		assertEquals(board.getRowWidth(0),0);
		board.place(new Piece(Piece.L1_STR).computeNextRotation(),0,0);
		board.commit();
		board.commit();
		board.undo();
		board.clearRows();
		assertEquals(board.getRowWidth(0),1);
		board.undo();
		assertEquals(board.getRowWidth(0),3);
		board.commit();
	}


	
}
