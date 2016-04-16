/*
 * MineField.java
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.*;

/**
 * Represents a minesweeper board
 * @author Kyle
 *
 */
public class MineField implements MouseListener {
	private int width;
	private int height;
	ArrayList<ArrayList<Integer>> board = new ArrayList<ArrayList<Integer>>();
	
	final ArrayList<JButton> buttons = new ArrayList<JButton>();
	ArrayList<Coordinate> clear = new ArrayList<Coordinate>();
	ArrayList<Coordinate> cleared = new ArrayList<Coordinate>();
	ArrayList<Boolean> flagged = new ArrayList<Boolean>();
	
	int bombCount;
	boolean firstTurn = true;
	
	/**
	 * Constructor for a MineField object. Sets the field to the specified
	 * dimensions
	 */
	public MineField(int h, int w) {
		width = w;
		height = h;
		for(int i = 0; i < width * height; i++) {
			flagged.add(false);
		}
	}
	
	/**
	 * Checks to see if the player has won the game
	 */
	public boolean hasWon() {
		if(cleared.size() == width * height - bombCount) {
			return true;
		} return false;
	}
	
	/**
	 * ends the game with a win or loss (true or false)
	 */
	public void endGame(boolean won) {
		System.out.println("end");
		if(won) JOptionPane.showMessageDialog(null, "YOU WIN!!!");
		else JOptionPane.showMessageDialog(null, "You have lost.");
		System.exit(0);
	}
	
	/**
	 * Takes the current board and generates the bomb locations. 
	 */
	public void genBombs() {
		Random rand = new Random();
		for(int r = 0; r<height; r++) {
			board.add(new ArrayList<Integer>());
			for(int c = 0; c<width; c++) {
				int result = rand.nextInt(11);
				if (result == 10) {
					result = -1; // bomb
				    bombCount++;
				}
				else
					result = 0; // empty space
				board.get(r).add(new Integer(result));
			}
		}
	} // genBombs()
	
	/**
	 * Checks to see if the identified spot has been cleared
	 */
	public boolean isClear(Coordinate coord, ArrayList<Coordinate> clearList) {
		for(Coordinate co : clearList) {
			if(co.getRow() == coord.getRow() && co.getCol() == coord.getCol())
				return true;
		}
		return false;
	}
	
	/**
	 * Clicks a button from another button
	 */
	public void click(int row, int col) {
		JButton button = buttons.get(row * width + col);
		if(board.get(row).get(col) != 0)
        	button.setText(Integer.toString(board.get(row).get(col)));
		button.setBackground(Color.gray);
		clear.add(new Coordinate(row, col));
	}
	
    public void setColor(ArrayList<JButton> buttons, int total, int r, int c) {
    	if(total == 1)
		    buttons.get((width * r) + c).setForeground(Color.blue);
		else if(total == 2)
			buttons.get((width * r) + c).setForeground(Color.green);
		else if(total == 3) 
			buttons.get((width * r) + c).setForeground(Color.red);
		else if(total == 4)
			buttons.get((width * r) + c).setForeground(Color.darkGray);
	}
    
    public boolean contains(Coordinate c, ArrayList<Coordinate> arr) {
    	for(Coordinate coor : arr) {
    		if(c.getRow() == coor.getRow() && c.getCol() == coor.getCol())
    			return true;
    	} return false;
    }
	
	/**
	 * Draws out the board on a GUI
	 */
	public void drawBoard() {
		JFrame f = new JFrame("Minesweeper");
		f.setSize(width * 50, height * 50);
		f.setLayout(new BorderLayout());
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(height, width));
		
		// add the buttons
		
		for(int r = 0; r<height; r++) {
			for(int c = 0; c<width; c++) {
				final int row = new Integer(r);
				final int col = new Integer(c);
				final JButton button = new JButton();
				button.setFont(new Font("Sans-Serif", 25, 15));
				button.addMouseListener(this);
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(flagged.get(row * width + col) == true) {
							// do nothing
						}
					    else {
					    	// works for numbers clicked initially
					    	if(board.get(row).get(col) != 0 && !firstTurn) {
					    		cleared.add(new Coordinate(row, col));
                        	    button.setText(Integer.toString(board.get(row).get(col)));
					    	}
                            else { // blank spot, check for neighbors and clear
                        	    // create a stack of neighbors
                        	    Stack<Coordinate> neighbors = new Stack<Coordinate>();
                        	
                        	    Coordinate init = new Coordinate(row, col);
                        	    neighbors.add(init);
                        	    while(neighbors.size() > 0) {
                        	    	Coordinate newCoord = neighbors.pop();
                        	    if(!isClear(newCoord, cleared)) {
                        	    	if(!contains(newCoord, cleared))
                        	    	    cleared.add(newCoord);
                        	    	// check around. If neighbor == 0 click and add to neighbors, else if number just click
                        	    	if(newCoord.getCol() > 0) { // check left
                				    	if(board.get(newCoord.getRow()).get(newCoord.getCol()-1) > -1) {
                				    		click(newCoord.getRow(), newCoord.getCol()-1);
                				    		if(board.get(newCoord.getRow()).get(newCoord.getCol()-1) == 0)
                				    			neighbors.add(new Coordinate(newCoord.getRow(), newCoord.getCol()-1));
                				    	}
                        	    	} if (newCoord.getCol() < width-1) { // check right
                        	    		if(board.get(newCoord.getRow()).get(newCoord.getCol()+1) > -1) {
                        	    			click(newCoord.getRow(), newCoord.getCol()+1);
                        	    			if(board.get(newCoord.getRow()).get(newCoord.getCol()+1) == 0)
                        	    				neighbors.add(new Coordinate(newCoord.getRow(), newCoord.getCol()+1));
                        	    		}
                        	    	}
                        	    	//vertically
                        	    	if(newCoord.getRow() > 0) {
                        	    		if(board.get(newCoord.getRow()-1).get(newCoord.getCol()) > -1) {
                        	    			click(newCoord.getRow()-1, newCoord.getCol());
                        	    			if(board.get(newCoord.getRow()-1).get(newCoord.getCol()) == 0)
                        	    				neighbors.add(new Coordinate(newCoord.getRow()-1, newCoord.getCol()));
                        	    		}
                						
                        	    	} if (newCoord.getRow() < height-1) {
                        	    		if(board.get(newCoord.getRow()+1).get(newCoord.getCol()) > -1) {
                        	    			click(newCoord.getRow()+1, newCoord.getCol());
                        	    			if(board.get(newCoord.getRow()+1).get(newCoord.getCol()) == 0)
                        	    				neighbors.add(new Coordinate(newCoord.getRow()+1, newCoord.getCol()));
                        	    		}
                        	    	}
                        	    	//diagonally (only checks for numbers to click)
                        	    	if(newCoord.getRow() > 0) {
                        	    		if(newCoord.getCol() > 0) {
                        	    			if(board.get(newCoord.getRow()-1).get(newCoord.getCol()-1) > 0) {
                        	    				click(newCoord.getRow()-1, newCoord.getCol()-1);
                        	    			}
                        	    		}
                        	    		if(newCoord.getCol() < width-1) {
                        	    			if(board.get(newCoord.getRow()-1).get(newCoord.getCol()+1) > 0) {
                        	    				click(newCoord.getRow()-1, newCoord.getCol()+1);
                        	    			}
                        	    		}
                        	    	}
                        	    	if(newCoord.getRow() < height-1) {
                        	    		if(newCoord.getCol() > 0) {
                        	    			if(board.get(newCoord.getRow()+1).get(newCoord.getCol()-1) > 0) {
                        	    				click(newCoord.getRow()+1, newCoord.getCol()-1);
                        	    			}
                        	    		}
                        	    		if(newCoord.getCol() < width -1) {
                        	    			if(board.get(newCoord.getRow()+1).get(newCoord.getCol()+1) > 0) {
                        	    				click(newCoord.getRow()+1, newCoord.getCol()+1);
                        	    			}
                        	    		}
                        	    	}
                        	    }
                        	    } // while loop
                        	    for(Coordinate coord : clear) {
                        	    	if(!contains(coord, cleared))
                        	    	    cleared.add(coord);
                        	    } clear = new ArrayList<Coordinate>();
                            }
					    	button.setBackground(Color.gray);
					    	if(board.get(row).get(col) == -1) {
					    		// YOU JUST HIT A BOMB. BOOOOOOOM
					    		for(int r = 0; r<height; r++) {
					    			for(int c = 0; c<width; c++) {
					    				if(board.get(r).get(c) == -1) {
					    					buttons.get(width * r + c).setBackground(Color.red);
					    					buttons.get(width * r + c).setText("*");
					    					button.setFont(new Font("Sans-Serif", 30, 35));
					    				}
					    			}
					    		}
					    		endGame(false);
					    	}
						}
						firstTurn = false;
						if(hasWon())
							endGame(true);
						System.out.println(cleared.size());
					}
					
				});
				buttons.add(button);
			}
		}
		for(int i = 0; i< width * height; i++) {
			p.add(buttons.get(i));
		}
		f.add(p, BorderLayout.CENTER);
		f.setVisible(true);
	} // drawBoard()
	
	/**
	 * Generates the numbers that appear next to the bombs.
	 */
	public void genNumbers() {
		for(int r = 0; r<height; r++) {
			for(int c = 0; c<width; c++) {
				int total = 0;
				// check around for bombs, add to total
				//horizontally
				if(c > 0) { // check left
					if(board.get(r).get(c-1) == -1)
						total += 1;
				} if (c < width-1) { // check right
					if(board.get(r).get(c+1) == -1)
						total += 1;
				}
				//vertically
				if(r > 0) {
					if(board.get(r-1).get(c) == -1) 
						total += 1;
				} if (r < height-1) {
					if(board.get(r+1).get(c) == -1)
						total += 1;
				}
				//diagonally
				if(r > 0) {
					if(c > 0) {
						if(board.get(r-1).get(c-1) == -1)
							total += 1;
					}
					if(c < width-1) {
						if(board.get(r-1).get(c+1) == -1)
							total += 1;
					}
				}
				if(r < height-1) {
					if(c > 0) {
						if(board.get(r+1).get(c-1) == -1)
							total += 1;
					}
					if(c < width -1) {
						if(board.get(r+1).get(c+1) == -1)
							total += 1;
					}
				}
				if(board.get(r).get(c) == -1)
					total = -1;
				
				board.get(r).set(c, total);
				board.set(r, board.get(r));
				setColor(buttons, board.get(r).get(c), r, c);
			}
		}
	} // genNumbers
	
	public void dumpBoardState()  {
		for(int r = 0; r<height; r++) {
			for(int c = 0; c<width; c++) {
				System.out.print(board.get(r).get(c) + " ");
				if(board.get(r).get(c) != -1)
					System.out.print(" ");
			}
			System.out.println("");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			Object button = e.getSource();
			for(int i = 0; i < buttons.size(); i++) {
				if(buttons.get(i) == button && !isClear(new Coordinate((i - i % width) / width, i % width), cleared)) {
					flagged.set(i, !flagged.get(i));
				    if(flagged.get(i)) {
					    buttons.get(i).setText("<|");
					    buttons.get(i).setForeground(Color.black);
				    } else {
                        buttons.get(i).setText("");
					    setColor(buttons, board.get((i - i % width) / width).get(i % width), (i-i%width)/width, i%width);
				    }
			    }
			}
		}	
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
