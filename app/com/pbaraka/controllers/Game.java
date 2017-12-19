package com.pbaraka.controllers;

import java.util.Random;

import play.mvc.*;

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

		// Computer start playing if user input is Empty
		if(board.trim().isEmpty())
			gameGrid[randomNumber(0,2)][randomNumber(0,2)] = COMPUTER;
    }
	
	private static void playGame() {
		while(true) {			
			// Check if there's a winner or it's a draw
			if(isWinner())
				break;
			else if(isDraw())
				break;
			else {
				// Computer checks where to move
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
				// To be improved from here ...
				return;
			}
		}
    }
	
	
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
	
	private static boolean isDraw() {
		for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
            	if(gameGrid[i][j] == EMPTY)
            		return false;
            }
		}		
		return true;
    }
    
	private static boolean isComputerMoved(int row, int col) {
		// Check all cases on position [0,0] or [2,2]
		if(row == col && (row == 0 || row == 2)) {
			if (gameGrid[row][1] == gameGrid[row][2 - col] && gameGrid[row][1] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
			if (gameGrid[1][col] == gameGrid[2 - row][col] && gameGrid[1][col] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
			if (gameGrid[1][1] == gameGrid[2 - row][2 - col] && gameGrid[1][1] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
	    }
		
		// Check all cases on position [0,2] or [2,0]
		if((row == 0 && col == 2) || (row == 2 && row == 0)) {
			if (gameGrid[row][1] == gameGrid[row][2 - col] && gameGrid[row][1] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
			if (gameGrid[1][col] == gameGrid[2 - row][col] && gameGrid[1][col] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
			if (gameGrid[1][1] == gameGrid[2 - row][2 - col] && gameGrid[1][1] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
	    }		
		
		// Check all cases on position [1,1]
		if(row == col && row == 1) {
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
		
		// Check all cases on position [0,1], [2,1]
		if((row == 0 && col == 1) || (row == 2 && col == 1)) {
			if (gameGrid[row][col - 1] == gameGrid[row][col + 1] && gameGrid[row][col - 1] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
			if (gameGrid[1][1] == gameGrid[2 - row][col] && gameGrid[1][1] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
	    }
		
		// Check all cases on position [1,0], [1,2]
		if((row == 1 && col == 0) || (row == 1 && col == 2)) {
			if (gameGrid[1][1] == gameGrid[row][2 - col] && gameGrid[1][1] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
			}
			if (gameGrid[row - 1][col] == gameGrid[row + 1][col] && gameGrid[row - 1][col] != EMPTY) {
				gameGrid[row][col] = COMPUTER;
				return true;
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
