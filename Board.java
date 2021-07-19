// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;

	private int [] heights;
	private int [] widths;

	private int [] heightsBackup;
	private int [] widthsBackup;

	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private boolean [][] backup;

	private int MaxHeight;
	private int HeightBackup;
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		MaxHeight = 0;
		HeightBackup = 0;

		this.width = width;
		this.height = height;

		widths = new int[height];
		heights = new int[width];

		widthsBackup = new int[height];
		heightsBackup = new int[width];

		grid = new boolean[height][width];
		backup = new boolean[height][width];

		committed = true;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return MaxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			for(int i = 0;i < height;i++){
				int counter = 0;
				for(int j = 0;j < width;j++){
					if(grid[i][j]){
						counter++;
					}
				}
				if(counter != widths[i])throw new RuntimeException("widths structure is incorrect on " + i + " Row");
			}
			for(int i = 0;i < width;i++){
				int max = 0;
				for(int j = 0;j < height;j++){
					if(grid[j][i]){
						if(j + 1 > max)max = j + 1;
					}
				}
				if(heights[i] != max)throw new RuntimeException("heights structure is incorect on " + i + " Column");
				if(max > MaxHeight)throw new RuntimeException("MaxHeight is incorrect");
			}

		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/

	public int dropHeight(Piece piece, int x) {
		int [] points = piece.getSkirt();
		int ret = 0;
		for(int i = 0;i < points.length;++i){
			ret = Math.max(heights[x + i] - points[i],ret);
		}
		return ret;
	}

	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x >= 0 && x < width && y >= 0 && y < height){
			if(!grid[y][x])return false;
		}
		return true;
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/

	private void updateHeights(){
		MaxHeight = 0;
		for(int i = 0;i < width;++i)heights[i] = 0;
		for(int i = 0;i < width;++i){
			int max = 0;
			for(int j = 0;j < height;++j){
				if(grid[j][i]){
					max = j + 1;
				}
			}
			if(max > heights[i])heights[i] = max;
			if(MaxHeight < max)MaxHeight = max;
		}
	}

	private void updateWidths(){
		for(int i = 0;i < height;i++){
			int counter = 0;
			for(int j = 0;j < width;++j){
				if(grid[i][j] == true){
					counter++;
				}
			}
			widths[i] = counter;
		}
	}

	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed ) throw new RuntimeException("place commit problem");
		int result = PLACE_OK;
		TPoint [] points = piece.getBody();
		for(int i = 0;i < points.length;++i){
			if(x + points[i].x < 0 || x + points[i].x >= this.width || y + points[i].y < 0 || y + points[i].y >= this.height)return PLACE_OUT_BOUNDS;
			if(this.grid[y + points[i].y][x + points[i].x])return PLACE_BAD;
		}
		for(int i = 0;i < points.length;++i){
			this.grid[y + points[i].y][x + points[i].x] = true;
		}
		updateWidths();
		updateHeights();
		for(int i = 0;i < points.length;i++){
			if(widths[y + points[i].y] == width)result = PLACE_ROW_FILLED;
		}
		committed = false;
		sanityCheck();
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		committed = false;
		System.out.println(widths[0]);
		for(int i = 0;i < height;i++){
			if(widths[i] == width){
				rowsCleared++;
				for(int j = 0;j < width;++j){
					if(getColumnHeight(j) < height){
						grid[getColumnHeight(j)][j] = false;
						for(int y = i;y < heights[j];++y){
							grid[y][j] = grid[y + 1][j];
						}
					}else{
						grid[getColumnHeight(j) - 1][j] = false;
						for(int y = i;y < heights[j] - 1;++y){
							grid[y][j] = grid[y + 1][j];
						}
					}

				}
				i--;
			}
			updateHeights();
			updateWidths();
		}
		sanityCheck();
		return rowsCleared;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed)return;
		committed = true;
		boolean [][] temp = grid;
		grid = backup;
		backup = temp;
		int [] temp1 = heights;
		heights = heightsBackup;
		heightsBackup = temp1;
		temp1 = widths;
		widths = widthsBackup;
		widthsBackup = temp1;
		MaxHeight = HeightBackup;
		for(int i = 0;i < height;i++){
			System.arraycopy(grid[i],0,backup[i],0,width);
		}
		System.arraycopy(widths,0,widthsBackup,0,height);
		System.arraycopy(heights,0,heightsBackup,0,width);
		sanityCheck();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		if(committed)return;
		System.arraycopy(heights,0,heightsBackup,0,width);
		System.arraycopy(widths,0,widthsBackup,0,height);
		for(int i = 0;i < grid.length;i++){
			System.arraycopy(grid[i],0,backup[i],0,width);
		}
		HeightBackup = MaxHeight;
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


