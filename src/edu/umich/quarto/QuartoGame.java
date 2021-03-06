package edu.umich.quarto;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuartoGame extends Activity {

	boolean userWinner = false;
	boolean aiWinner = false;
	BoardSquare[][] board = new BoardSquare[4][4];
	BoardSquare[][] selector = new BoardSquare[8][2];
	BoardSquare selectedSquare = null;
	
	private TextView prompt;
	private boolean placePiece = false;
	private final String placePieceString = "Place the chosen tile!";
	private final String pickPieceString = "Pick your opponent's piece!";

	Button button;
	OnClickListener quartoAction;
	OnClickListener resetAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quarto_game);
		prompt = (TextView)findViewById(R.id.prompt);
		
		initBoard();
		initSelectors();
		initButton();
	}

	private void initButton() {
		resetAction = new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearBoard();
				GridLayout selectorGrid = (GridLayout)findViewById(R.id.PieceSelector);
				selectorGrid.removeAllViews();
				GridLayout quartoBoard = (GridLayout)findViewById(R.id.QuartoBoard);
				quartoBoard.removeAllViews();
				initSelectors();
				initBoard();
				initButton();
			}
		};
		quartoAction = new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isWin()){
					userWinner = true;
					endGame();
				} else {
					Toast.makeText(getApplicationContext(), "NO!!! You're Dumb!", Toast.LENGTH_LONG).show();
				}
			}
		};
		
		button = (Button)findViewById(R.id.QuartoButton);
		button.setText("Quarto!");
		button.setOnClickListener(quartoAction);
	}

	private void initBoard() {
		// quarto board grid
		GridLayout grid = (GridLayout)findViewById(R.id.QuartoBoard);
		grid.setRowCount(4);
		grid.setColumnCount(4);
		for(int x = 0; x < 4; x++){
			for(int y = 0; y < 4; y++){
				BoardSquare square = new BoardSquare(this, 4, true);
				board[x][y] = square;
				square.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if (placePiece){
							placePiece((BoardSquare)v);
							swapPlacePick();
						}
					}
				});
				grid.addView(square);
			}
		}
	}

	private void initSelectors() {
		GridLayout grid;
		// grid of pieces to choose
		grid = (GridLayout)findViewById(R.id.PieceSelector);
		grid.setRowCount(2);
		grid.setColumnCount(8);
		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 2; y++){
				BoardSquare square = new BoardSquare(this, 8, false);
				square.setClickable(true);
				square.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if (placePiece)
							return;
						
						BoardSquare bs = (BoardSquare)v;
						if(bs.hasPiece()){
							if(selectedSquare != null) selectedSquare.setSelected(false);
							bs.setSelected(true);
							selectedSquare = bs;
							
							swapTurn();
							if (gameOver())
								return;
							
							aiTurn();
							swapPlacePick();
						}
					}
				});
				selector[x][y] = square;
				square.setAttribute(x+y*8);
				grid.addView(square);
			}
		}
	}
	
	protected void aiTurn(){
		List<Integer> freeSpots = new ArrayList<Integer>();
		for (int i = 0; i < 16; i++)
			if (!board[(int)i/4][i%4].hasPiece())
				freeSpots.add(i);
				
		int randi = (int) Math.rint(Math.random() * (freeSpots.size() - 1));
		placePiece(board[(int)randi/4][randi%4]);
		if (isWin()){
			aiWinner = true;
			endGame();
			return;
		}
		
		for (int j = 0; j < 16; j++){
			BoardSquare bs = selector[(int)j%8][j/8];
			if(bs.hasPiece()){
				if(selectedSquare != null){
					selectedSquare.piece = null;
					selectedSquare.setSelected(false);
					selectedSquare.clearAttribute();
				}
				bs.setSelected(true);
				selectedSquare = bs;
				break;
			}
		}

		swapTurn();
		return;
	}
	protected boolean isWin(){
		int diagMask = board[0][0].hasPiece() ? ~0 : 0;
        int rDiagMask = board[3][0].hasPiece() ? ~0 : 0;
        int toMatchD1 = board[0][0].getAttribute();
        int toMatchD2 = board[3][0].getAttribute();
        for(int i = 0; i < 4; i++){
            int rowMask = board[i][0].hasPiece() ? ~0 : 0;
            int colMask = board[0][i].hasPiece() ? ~0 : 0;
            int toMatchRow = board[i][0].getAttribute();
            int toMatchCol = board[0][i].getAttribute();
            for(int j = 1; j < 4; j++){
                rowMask &= board[i][j].matchMask(toMatchRow);
                colMask &= board[j][i].matchMask(toMatchCol);
            }
            if(rowMask > 0 || colMask > 0) return true;
            
            diagMask &= board[i][i].matchMask(toMatchD1);
            rDiagMask &= board[3-i][i].matchMask(toMatchD2);
        }
        
        return diagMask > 0 || rDiagMask > 0;
	}
	
	protected void placePiece(BoardSquare target){
		if(selectedSquare == null || target.hasPiece()) return;
		target.setAttribute(selectedSquare.getAttribute());
		selectedSquare.clearAttribute();
		selectedSquare.setSelected(false);
		selectedSquare = null;
	}
	
	/* CLear the board when the game is over */
	protected void clearBoard()
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				board[i][j].clearAttribute();
			}
		}	
				
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 2; j++){
				selector[i][j].setSelected(false);
				selector[i][j].clearAttribute();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quarto_game, menu);
		return true;
	}
	
	private void swapPlacePick(){
		placePiece = !placePiece;
		if (placePiece) {			
			prompt.setText(placePieceString);
		} else {
			prompt.setText(pickPieceString);
		}
	}
	
	private void swapTurn(){
		if (gameOver())
			endGame();
	}

	private void endGame() {
		if (aiWinner)
			Toast.makeText(getApplicationContext(), "Quarto! AI wins!", Toast.LENGTH_LONG).show();
		else if (userWinner)
			Toast.makeText(getApplicationContext(), "Quarto! You Win!", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(getApplicationContext(), "Look at that, nobody wins", Toast.LENGTH_LONG).show();
		
		prompt.setText("");
		button.setText("Play again?");
		button.setOnClickListener(resetAction);
	}
	
	private boolean gameOver(){
		if (isWin())
			return true;
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 2; j++)
				if (selector[i][j].hasPiece())
					return false;
		
		return true;
	}

}
