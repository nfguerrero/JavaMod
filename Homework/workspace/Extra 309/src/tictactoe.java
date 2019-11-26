import java.util.Scanner;

public class tictactoe 
{

	public static boolean detection(boolean iswinning, char[][] board) {
	
	    if (("" + board[0][0] + board[1][0] + board[2][0]).equals("xxx")) {
	        iswinning = true;
	    } else if (("" + board[1][0] + board[1][1] + board[2][1]).equals("xxx")) {
	        iswinning = true;
	    } else if (("" + board[0][2] + board[1][2] + board[2][2]).equals("xxx")) {
	        iswinning = true;
	    } else if (("" + board[0][0] + board[1][1] + board[2][2]).equals("xxx")) {
	        iswinning = true;
	    } else if (("" + board[2][0] + board[1][1] + board[0][2]).equals("xxx")) {
	        iswinning = true;
	    } else if (("" + board[0][0] + board[0][1] + board[0][2]).equals("xxx")) {
	        iswinning = true;
	    } else if (("" + board[1][0] + board[1][1] + board[1][2]).equals("xxx")) {
	        iswinning = true;
	    } else if (("" + board[2][0] + board[2][1] + board[2][2]).equals("xxx")) {
	        iswinning = true;
	    }
	    return iswinning;
	}
	
	public static boolean detection1(boolean iswinning1, char[][] board) {
	
	    if (("" + board[0][0] + board[1][0] + board[2][0]).equals("ooo")) {
	        iswinning1 = true;
	    } else if (("" + board[1][0] + board[1][1] + board[2][1]).equals("ooo")) {
	        iswinning1 = true;
	    } else if (("" + board[0][2] + board[1][2] + board[2][2]).equals("ooo")) {
	        iswinning1 = true;
	    } else if (("" + board[0][0] + board[1][1] + board[2][2]).equals("ooo")) {
	        iswinning1 = true;
	    } else if (("" + board[2][0] + board[1][1] + board[0][2]).equals("ooo")) {
	        iswinning1 = true;
	    } else if (("" + board[0][0] + board[0][1] + board[0][2]).equals("ooo")) {
	        iswinning1 = true;
	    } else if (("" + board[1][0] + board[1][1] + board[1][2]).equals("ooo")) {
	        iswinning1 = true;
	    } else if (("" + board[2][0] + board[2][1] + board[2][2]).equals("ooo")) {
	        iswinning1 = true;
	    }
	    return iswinning1;
	}

	public static void main(String[] args) {
	
	    Scanner keyboard = new Scanner(System.in);
	    char board[][] = new char[3][3];
	    int turn = 1;
	    int y = 0;
	    boolean validinput;
	    boolean iswinning = false;
	    boolean iswinning1 = false;
	
	    while (true) {
	        if (turn == 1) {
	            System.out.println("player 1 it is your turn. your spot you wou"
	                    + "ld like to enter.\n Ex: top,botton,middle left,right,middle");
	            String input = keyboard.nextLine();
	
	            validinput = true;
	            if (input.equals("top left") && board[0][0] == '\u0000') {
	                board[0][0] = 'x';
	            } else if (input.equals("top middle") && board[1][0] == '\u0000') {
	                board[1][0] = 'x';
	            } else if (input.equals("top right") && board[2][0] == '\u0000') {
	                board[2][0] = 'x';
	            } else if (input.equals("middle left") && board[0][1] == '\u0000') {
	                board[0][1] = 'x';
	            } else if (input.equals("middle middle") && board[1][1] == '\u0000') {
	                board[1][1] = 'x';
	            } else if (input.equals("middle right") && board[2][1] == '\u0000') {
	                board[2][1] = 'x';
	            } else if (input.equals("bottom left") && board[0][2] == '\u0000') {
	                board[0][2] = 'x';
	            } else if (input.equals("bottom middle") && board[1][2] == '\u0000') {
	                board[1][2] = 'x';
	            } else if (input.equals("bottom right") && board[2][2] == '\u0000') {
	                board[2][2] = 'x';
	            } else {
	                System.out.println("You have entered an invalid space "
	                        + "please try again.");
	                validinput = false;
	            }
	            if (validinput == true) {
	                y++;
	                turn++;
	            }
	            System.out.printf(" %c | %c | %c \n", board[0][0], board[1][0], board[2][0]);
	            System.out.printf("____________\n");
	            System.out.printf(" %c | %c | %c \n", board[0][1], board[1][1], board[2][1]);
	            System.out.printf("____________\n");
	            System.out.printf(" %c | %c | %c \n", board[0][2], board[1][2], board[2][2]);
	
	           iswinning=detection(iswinning, board); 
	            if (iswinning == true) {
	                System.out.println("Player 1 wins!");
	                System.exit(1);
	            }
	
	            if (y == 9) {
	                System.out.println("the game ends in a draw.");
	                System.exit(1);
	            }
	
	        }
	        if (turn == 2) {
	            System.out.println("player 2 it is your turn. your spot you"
	                    + " would like to enter.\n Ex: top,botton,middle left,right,middle");
	            String input = keyboard.nextLine();
	
	            validinput = true;
	            if (input.equals("top left") && board[0][0] == '\u0000') {
	                board[0][0] = 'o';
	            } else if (input.equals("top middle") && board[1][0] == '\u0000') {
	                board[1][0] = 'o';
	            } else if (input.equals("top right") && board[2][0] == '\u0000') {
	                board[2][0] = 'o';
	            } else if (input.equals("middle left") && board[0][1] == '\u0000') {
	                board[0][1] = 'o';
	            } else if (input.equals("middle middle") && board[1][1] == '\u0000') {
	                board[1][1] = 'o';
	            } else if (input.equals("middle right") && board[2][1] == '\u0000') {
	                board[2][1] = 'o';
	            } else if (input.equals("bottom left") && board[0][2] == '\u0000') {
	                board[0][2] = 'o';
	            } else if (input.equals("bottom middle") && board[1][2] == '\u0000') {
	                board[1][2] = 'o';
	            } else if (input.equals("bottom right") && board[2][2] == '\u0000') {
	                board[2][2] = 'o';
	            } else {
	                System.out.println("You have entered an invalid space "
	                        + "please try again.");
	                validinput = false;
	            }
	            if (validinput) {
	                turn--;
	                y++;
	            }
	            System.out.printf(" %c | %c | %c \n", board[0][0], board[1][0], board[2][0]);
	            System.out.printf("____________\n");
	            System.out.printf(" %c | %c | %c \n", board[0][1], board[1][1], board[2][1]);
	            System.out.printf("____________\n");
	            System.out.printf(" %c | %c | %c \n", board[0][2], board[1][2], board[2][2]);
	            iswinning1=detection(iswinning1, board); 
	            if (iswinning1 == true) {
	                System.out.println("Player 2 wins!");
	                System.exit(1);
	            }
	
	        }
	    }	    
	}
}

