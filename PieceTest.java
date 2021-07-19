import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {

		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	//checks getWidth and getHeight methods on various pieces before and after rotation
	public void testSameSize1(){
		Piece myPiece = new Piece(Piece.STICK_STR);
		assertTrue(myPiece.getWidth() == 1);
		assertTrue(myPiece.getHeight() == 4);
		myPiece = myPiece.computeNextRotation();
		assertTrue(myPiece.getWidth() == 4);
		assertTrue(myPiece.getHeight() == 1);
		myPiece = new Piece(Piece.L1_STR);
		assertTrue(myPiece.getWidth() == 2);
		assertTrue(myPiece.getHeight() == 3);
		myPiece = myPiece.computeNextRotation();
		assertTrue(myPiece.getWidth() == 3);
		assertTrue(myPiece.getHeight() == 2);
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}
	//testing next rotation foreach
	public void testNextRotation1(){
		Piece [] pieces = new Piece[]{
					new Piece("0 0  0 1  0 2  0 3"),
				 	new Piece("0 0  0 1  0 2  1 0"),
					new Piece("0 0  1 0 1 1  1 2"),
					new Piece("0 0  1 0  1 1  2 1"),
					new Piece("0 1  1 1  1 0  2 0"),
					new Piece("0 0  0 1  1 0  1 1"),
					new Piece("0 0  1 0  1 1  2 0")
		};
		Piece [] results = new Piece[]{
				new Piece("0 0  1 0  2 0  3 0"),
				new Piece("0 0  1 0  2 0  2 1"),
				new Piece("0 1  1 1  2 1  2 0"),
				new Piece("0 1  0 2  1 0  1 1"),
				new Piece("0 0  0 1  1 1  1 2"),
				new Piece("0 0  0 1  1 0  1 1"),
				new Piece("0 1  1 0  1 1  1 2")

		};
		for(int i = 0;i < 7;++i){
			assertTrue(pieces[i].computeNextRotation().equals(results[i]));
		}
	}
	//testing rotations for piece back to its original position
	public void testNextRotation2(){
		Piece [] pieces = new Piece[]{
				new Piece("0 0  0 1  0 2  0 3"),
				new Piece("0 0  0 1  0 2  1 0"),
				new Piece("0 0  1 0 1 1  1 2"),
				new Piece("0 0  1 0  1 1  2 1"),
				new Piece("0 1  1 1  1 0  2 0"),
				new Piece("0 0  0 1  1 0  1 1"),
				new Piece("0 0  1 0  1 1  2 0")
		};
		for(int i = 0;i < 4;i++){
			pieces[0] = pieces[0].computeNextRotation();
			pieces[1] = pieces[1].computeNextRotation();
			pieces[2] = pieces[2].computeNextRotation();
			pieces[6] = pieces[6].computeNextRotation();
		}

		for(int i = 0;i < 2;i++){
			pieces[3] = pieces[3].computeNextRotation();
			pieces[4] = pieces[4].computeNextRotation();
		}
		pieces[5] = pieces[5].computeNextRotation();

		Piece [] res = new Piece[]{
				new Piece("0 0  0 1  0 2  0 3"),
				new Piece("0 0  0 1  0 2  1 0"),
				new Piece("0 0  1 0 1 1  1 2"),
				new Piece("0 0  1 0  1 1  2 1"),
				new Piece("0 1  1 1  1 0  2 0"),
				new Piece("0 0  0 1  1 0  1 1"),
				new Piece("0 0  1 0  1 1  2 0")
		};

		for(int i = 0;i < 7;++i){
			assertTrue(pieces[i].equals(res[i]));
		}
	}
	public void testGetPieces(){
		Piece.getPieces();
		Piece [] results = new Piece[]{
				new Piece("0 0  1 0  2 0  3 0"),
				new Piece("0 0  1 0  2 0  2 1"),
				new Piece("0 1  1 1  2 1  2 0"),
				new Piece("0 1  0 2  1 0  1 1"),
				new Piece("0 0  0 1  1 1  1 2"),
				new Piece("0 0  0 1  1 0  1 1"),
				new Piece("0 1  1 0  1 1  1 2")

		};
		for(int i = 0;i < Piece.getPieces().length;i++){
			assertTrue(Piece.getPieces()[i].fastRotation().equals(results[i]));
		}

	}
	
	
}
