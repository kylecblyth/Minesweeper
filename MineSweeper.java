/*
 * MineSweeper.java
 */

import java.util.Scanner;

/**
 * 
 * @author Kyle
 *
 */
public class MineSweeper {
	static MineField board;
	
    public static void main(String args[]) {
    	
    	Scanner in = new Scanner(System.in);
    	System.out.println("Enter the size board you would like\n1. Small\n2. Medium\n3. Large\n");
    	int size = in.nextInt();
    	if (size == 1) {
    		board = new MineField(16, 12);
    	} else if (size == 2) {
    	    board = new MineField(16, 24);
    	} else if (size == 3) {
    		board = new MineField(20, 32);
    	} else {
    		System.out.println("That is not a valid size. Exiting...");
    		System.exit(0);
    	}
    	in.close();
    	
    	//draw out the board
    	board.genBombs();
    	board.drawBoard();
    	board.genNumbers();
    	board.dumpBoardState();
    }
}
