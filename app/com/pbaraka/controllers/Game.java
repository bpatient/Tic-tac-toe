package com.pbaraka.controllers;

import java.util.Random;

import play.mvc.*;

/**
 * @author Patient Baraka
 * 
 * This the Game controller that contains all necessary functions to run the game.
 */
public class Game extends Controller {
	
	private static final char EMPTY = ' ';
	private static final char HUMAN = 'x';
	private static final char COMPUTER = 'o';
	private static final int SIDE_LENGTH = 3; // board length
	private static char[][] gameGrid = new char[SIDE_LENGTH][SIDE_LENGTH];
	private static String newBoard;

    /**
     * This function allows user to initiate the board through API url
     * @param board
     * @return
     */
	public static Result getBoard(String board) {  
		// Check if the board is valid or not
    	if(board == null || (board != null && board.length() != 9))
    		return status(400, "Bad Request");
    	else {
    		// Check if the board contains valid inputs
    		for (int i = 0; i < board.length(); i++) {
    			if (board.charAt(i) != HUMAN && board.charAt(i) != COMPUTER && board.charAt(i) != EMPTY)
    				return status(400, "Invalid Input");
    		}
    		
    		// Initialize the board with provided inputs
    		initializeGrid(board);
    		
    		// Start play
    		playGame();
    		
    		// Return the new board
    		for (int i = 0; i < SIDE_LENGTH; i++)
                for (int j = 0; j < SIDE_LENGTH; j++)
                	newBoard += gameGrid[i][j];
    		
    		return ok(newBoard);
    	}
    }
	
	/**
	 * This function initialize the Game Grid/Board with the provided inputs through API url
	 * @param board
	 */
	private static void initializeGrid(String board) {
		newBoard = "";
		int boardCounter = 0;
			
        for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
            	gameGrid[i][j] = board.charAt(boardCounter);
            	boardCounter++;
            }
        }        
    }
	
	/**
	 * This function is the engine of this game.
	 * It determines when & where the COMPUTER should move and
	 * returns the output to the API request for every new move
	 */
	private static void playGame() {
		while(true) {			
			// Check if there's a winner or it's a draw
			if(isWinner())
				break;
			else if(isDraw())
				break;
			else {
				// Computer checks where to move in order to WIN
				for (int i = 0; i < SIDE_LENGTH; i++) {
		            for (int j = 0; j < SIDE_LENGTH; j++) {
		            	if(gameGrid[i][j] == COMPUTER) {
		            		if(isComputerMoved(i, j)) {
		            			// Break the next move to wait for input from HUMAN
		    		            return;
		            		}
		            	}
		            }		            
		        }
				
				// Computer checks where to move in order to prevent HUMAN to win
				for (int i = 0; i < SIDE_LENGTH; i++) {
		            for (int j = 0; j < SIDE_LENGTH; j++) {
		            	if(gameGrid[i][j] == EMPTY) {
		            		if(isComputerMoved(i, j)) {
		            			// Break the next move to wait for input from HUMAN
		    		            return;
		            		}
		            	}
		            }		            
		        }
				
				
				/* Computer moves to a random empty position 
				 * if it was unable to move for WIN or prevent HUMAN to win
				 */
				while(true) {
					int randRow = randomNumber(0,2);
					int randCol = randomNumber(0,2);
					if(gameGrid[randRow][randCol] == EMPTY) {
						gameGrid[randRow][randCol] = COMPUTER;
						return;
					}
				}
			}
		}
    }
	
	
	/**
	 * This function return true if there is a winner (COMPUTER or HUMAN) for 
	 * all different cases by: row crossing, column crossing or diagonal crossing
	 * @return
	 */
	private static boolean isWinner() {
		if (gameGrid[0][0] == gameGrid[1][0] && gameGrid[1][0] == gameGrid[2][0] && (gameGrid[0][0] == HUMAN || gameGrid[0][0] == COMPUTER))
            return true;
        else if (gameGrid[0][1] == gameGrid[1][1] && gameGrid[1][1] == gameGrid[2][1] && (gameGrid[0][1] == HUMAN || gameGrid[0][1] == COMPUTER))
            return true;
        else if (gameGrid[0][2] == gameGrid[1][2] && gameGrid[1][2] == gameGrid[2][2] && (gameGrid[0][2] == HUMAN || gameGrid[0][2] == COMPUTER))
            return true;
        else if (gameGrid[0][0] == gameGrid[0][1] && gameGrid[0][1] == gameGrid[0][2] && (gameGrid[0][0] == HUMAN || gameGrid[0][0] == COMPUTER))
            return true;
        else if (gameGrid[1][0] == gameGrid[1][1] && gameGrid[1][1] == gameGrid[1][2] && (gameGrid[1][0] == HUMAN || gameGrid[1][0] == COMPUTER))
            return true;
        else if (gameGrid[2][0] == gameGrid[2][1] && gameGrid[2][1] == gameGrid[2][2] && (gameGrid[2][0] == HUMAN || gameGrid[2][0] == COMPUTER))
            return true;
        else if (gameGrid[0][0] == gameGrid[1][1] && gameGrid[1][1] == gameGrid[2][2] && (gameGrid[0][0] == HUMAN || gameGrid[0][0] == COMPUTER))
            return true;
       else if (gameGrid[2][0] == gameGrid[1][1] && gameGrid[1][1] == gameGrid[0][2] && (gameGrid[2][0] == HUMAN || gameGrid[2][0] == COMPUTER))
            return true;
        else
            return false;
    }
	
	/**
	 * This function detect if it's tie/draw
	 * and prevent any new move for both players.
	 * It return true if there is no empty space in the board.
	 * @return
	 */
	private static boolean isDraw() {
		for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
            	if(gameGrid[i][j] == EMPTY)
            		return false;
            }
		}		
		return true;
    }
    
	/**
	 * This function is used to detect all new moves that can help the COMPUTER to win or 
	 * help the computer to prevent the HUMAN to win by moving to that empty space.
	 * It's applicable on a grid of 3x3 sides
	 * @param row
	 * @param col
	 * @return
	 */
	private static boolean isComputerMoved(int row, int col) {
		/*
		 *  Check all cases on position [0,0], [2,2], [0,2] or [2,0]
		 */
		if((row == col && (row == 0 || row == 2)) || (row == 0 && col == 2) || (row == 2 && row == 0)) {
			/*
			 *  Computer seeks to win
			 */
			if(gameGrid[row][col] == COMPUTER) {
				// Row checking
				if (gameGrid[row][col] == gameGrid[row][1] && gameGrid[row][2 - col] == EMPTY) {
					gameGrid[row][2 - col] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[row][2 - col] && gameGrid[row][1] == EMPTY) {
					gameGrid[row][1] = COMPUTER;
					return true;
				}
				
				// Column checking
				if (gameGrid[row][col] == gameGrid[1][col] && gameGrid[2 - row][col] == EMPTY) {
					gameGrid[2 - row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[2 - row][col] && gameGrid[1][col] == EMPTY) {
					gameGrid[1][col] = COMPUTER;
					return true;
				}
				
				// Diagonal checking
				if (gameGrid[row][col] == gameGrid[1][1] && gameGrid[2 - row][2 - col] == EMPTY) {
					gameGrid[2 - row][2 - col] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[2 - row][2 - col] && gameGrid[1][1] == EMPTY) {
					gameGrid[1][1] = COMPUTER;
					return true;
				}
			}
			else {
				/*
				 *  Computer prevents HUMAN to win
				 */
				if (gameGrid[row][1] == gameGrid[row][2 - col] && gameGrid[row][1]!= EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[1][col] == gameGrid[2 - row][col] && gameGrid[1][col]!= EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[1][1] == gameGrid[2 - row][2 - col] && gameGrid[1][1]!= EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
			}
	    }		
		
		/*
		 *  Check all cases on position [1,1]
		 */
		if(row == col && row == 1) {
			/*
			 *  Computer seeks to win
			 */
			if(gameGrid[row][col] == COMPUTER) {
				// Row checking
				if (gameGrid[row][col] == gameGrid[row][0] && gameGrid[row][2] == EMPTY) {
					gameGrid[row][2] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[row][2] && gameGrid[row][0] == EMPTY) {
					gameGrid[row][0] = COMPUTER;
					return true;
				}
				
				// Column checking
				if (gameGrid[row][col] == gameGrid[0][col] && gameGrid[2][col] == EMPTY) {
					gameGrid[2][col] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[2][col] && gameGrid[0][col] == EMPTY) {
					gameGrid[0][col] = COMPUTER;
					return true;
				}
				
				// Diagonal checking
				if (gameGrid[row][col] == gameGrid[0][0] && gameGrid[2][2] == EMPTY) {
					gameGrid[2][2] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[2][2] && gameGrid[0][0] == EMPTY) {
					gameGrid[0][0] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[0][2] && gameGrid[2][0] == EMPTY) {
					gameGrid[2][0] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[2][0] && gameGrid[0][2] == EMPTY) {
					gameGrid[0][2] = COMPUTER;
					return true;
				}
			}
			else {
				/*
				 *  Computer prevents HUMAN to win
				 */
				if (gameGrid[row][0] == gameGrid[row][2] && gameGrid[row][0] != EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[0][col] == gameGrid[2][col] && gameGrid[0][col] != EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[0][0] == gameGrid[2][2] && gameGrid[0][0] != EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[0][2] == gameGrid[2][0] && gameGrid[0][2] != EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
			}
	    }		
		
		/*
		 *  Check all cases on position [0,1], [2,1]
		 */
		if((row == 0 && col == 1) || (row == 2 && col == 1)) {
			/*
			 *  Computer seeks to win
			 */
			if(gameGrid[row][col] == COMPUTER) {
				// Row checking
				if (gameGrid[row][col] == gameGrid[row][col - 1] && gameGrid[row][col + 1] == EMPTY) {
					gameGrid[row][col + 1] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[row][col + 1] && gameGrid[row][col - 1] == EMPTY) {
					gameGrid[row][col - 1] = COMPUTER;
					return true;
				}
				
				// Column checking
				if (gameGrid[row][col] == gameGrid[1][1] && gameGrid[2 - row][col] == EMPTY) {
					gameGrid[2 - row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[2 - row][col] && gameGrid[1][1] == EMPTY) {
					gameGrid[1][1] = COMPUTER;
					return true;
				}
			}
			else {
				/*
				 *  Computer prevents HUMAN to win
				 */
				if (gameGrid[row][col - 1] == gameGrid[row][col + 1] && gameGrid[row][col - 1] != EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[1][1] == gameGrid[2 - row][col] && gameGrid[1][1] != EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
			}
	    }
		
		/*
		 *  Check all cases on position [1,0], [1,2]
		 */
		if((row == 1 && col == 0) || (row == 1 && col == 2)) {
			/*
			 *  Computer seeks to win
			 */
			if(gameGrid[row][col] == COMPUTER) {
				// Row checking
				if (gameGrid[row][col] == gameGrid[1][1] && gameGrid[row][2 - col] == EMPTY) {
					gameGrid[row][2 - col] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[row][2 - col] && gameGrid[1][1] == EMPTY) {
					gameGrid[1][1] = COMPUTER;
					return true;
				}
				
				// Column checking
				if (gameGrid[row][col] == gameGrid[row - 1][col] && gameGrid[row + 1][col] == EMPTY) {
					gameGrid[row + 1][col] = COMPUTER;
					return true;
				}
				if (gameGrid[row][col] == gameGrid[row + 1][col] && gameGrid[row - 1][col] == EMPTY) {
					gameGrid[row - 1][col] = COMPUTER;
					return true;
				}
			}
			else {
				/*
				 *  Computer prevents HUMAN to win
				 */
				if (gameGrid[1][1] == gameGrid[row][2 - col] && gameGrid[1][1] != EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
				if (gameGrid[row - 1][col] == gameGrid[row + 1][col] && gameGrid[row - 1][col] != EMPTY) {
					gameGrid[row][col] = COMPUTER;
					return true;
				}
			}
	    }
		
		return false;
	}
    
	/**
	 * randomNumber method generates a random number between specified min and max 
	 * @param min
	 * @param max
	 * @return
	 */
	private static int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
    
    
    

}
