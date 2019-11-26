package assign0;

public class assign0 
{
	public static void main(String[] args)
	{
		String solution = ""; int count = 0;
		for (int col1 = 0; col1 < 8; col1++){
			boolean[][] board1 = new boolean[8][8];
			markQueen(0,col1,board1);
			String row1 = ((char) (col1+65))+"8";
			for (int col2 = 0; col2 < 8; col2++){
				if (!board1[1][col2]){
					boolean[][] board2 = copyBoard(board1);
					markQueen(1,col2,board2);
					String row2 = ((char) (col2+65))+"7";
					for (int col3 = 0; col3 < 8; col3++){
						if (!board2[2][col3]){
							boolean[][] board3 = copyBoard(board2);
							markQueen(2,col3,board3);
							String row3 = ((char) (col3+65))+"6";
							for (int col4 = 0; col4 < 8; col4++){
								if (!board3[3][col4]){
									boolean[][] board4 = copyBoard(board3);
									markQueen(3,col4,board4);
									String row4 = ((char) (col4+65))+"5";
									for (int col5 = 0; col5 < 8; col5++){
										if (!board4[4][col5]){
											boolean[][] board5 = copyBoard(board4);
											markQueen(4,col5,board5);
											String row5 = ((char) (col5+65))+"4";
											for (int col6 = 0; col6 < 8; col6++){
												if (!board5[5][col6]){
													boolean[][] board6 = copyBoard(board5);
													markQueen(5,col6,board6);
													String row6 = ((char) (col6+65))+"3";
													for (int col7 = 0; col7 < 8; col7++){
														if (!board6[6][col7]){
															boolean[][] board7 = copyBoard(board6);
															markQueen(6,col7,board7);
															String row7 = ((char) (col7+65))+"2";
															for (int col8 = 0; col8 < 8; col8++){
																if (!board7[7][col7]){
																	String row8 = ((char) (col8+65))+"1";
																	solution += (row1+row2+row3+row4+row5+row6+row7+row8+"\n");
																	count++;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println(count);
		System.out.println(solution);
	}
	
	public static void markQueen(int row, int col, boolean[][] board)
	{
		for (int i = 1; i < 8; i++){
			board[row][col] = true;
			if (row-i >= 0){board[row-i][col] = true; if (col-i >= 0){board[row-i][col-i] = true;} if (col+i <= 7){board[row-i][col+i] = true;}}
			if (row+i <= 7){board[row+i][col] = true; if (col-i >= 0){board[row+i][col-i] = true;} if (col+i <= 7){board[row+i][col+i] = true;}}
			if (col-i >= 0){board[row][col-i] = true;} if (col+i <= 7){board[row][col+i] = true;}
		}
	}
	
	public static boolean[][] copyBoard(boolean[][] board)
	{
		boolean[][] copyBoard = new boolean[8][8];
		for (int i=0; i<8; i++){
			for (int j=0; j<8; j++){
				copyBoard[i][j] = board[i][j];
			}
		}
		return copyBoard;
	}
}
