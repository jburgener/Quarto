package edu.umich.quarto;

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

	public enum PLAYER
	{	
		AI{
			@Override
			public String str() { return "AI"; }
		},
		PLAYER_1{
			@Override
			public String str() { return "Player one"; }
		},
		PLAYER_2{
			@Override
			public String str() { return "Player two"; }
		};

		public abstract String str();
	};
	
	BoardSquare[][] board = new BoardSquare[4][4];
	BoardSquare[][] selector = new BoardSquare[8][2];
	BoardSquare selectedSquare = null;
	
	private TextView prompt;
	private boolean placePiece = false;
	private boolean aiOn = false;
	PLAYER currentPlayer = PLAYER.PLAYER_1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quarto_game);
		prompt = (TextView)findViewById(R.id.prompt);
		
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
							swapTurn();
						}
					}
				});
				grid.addView(square);
			}
		}
		
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
							if (aiOn)
							{
								currentPlayer = PLAYER.AI;
								aiTurn();
							}
							else
							{
								currentPlayer = (currentPlayer == PLAYER.PLAYER_1)? PLAYER.PLAYER_2 : PLAYER.PLAYER_2;
							}
							swapTurn();
						}
					}
				});
				selector[x][y] = square;
				square.setAttribute(x+y*8);
				grid.addView(square);
			}
		}
		
		Button quartoButton = (Button)findViewById(R.id.QuartoButton);
		quartoButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				announceWin();
			}
		});
	}
	
	private void announceWin()
	{
		if(isWin()){
			String winnerText = "Quarto! " + currentPlayer.str() + " wins!";
			Toast.makeText(getApplicationContext(), winnerText, Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getApplicationContext(), "No Quarto! Try again!", Toast.LENGTH_LONG).show();
		}
	}
	
	protected void aiTurn(){
		for (int i = 0; i < 16; i++){
			if (!board[(int)i/4][i%4].hasPiece()){
				placePiece(board[(int)i/4][i%4]);
				if (isWin())
					announceWin();
				
				for (int j = 0; j < 16; j++){
					BoardSquare bs = selector[(int)i%8][i/8];
					if(bs.hasPiece()){
						if(selectedSquare != null) selectedSquare.setSelected(false);
						bs.setSelected(true);
						selectedSquare = bs;
						currentPlayer = PLAYER.PLAYER_1;
						return;
					}
				}
			}
		}
		
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
		selectedSquare.setSelected(false);
		selectedSquare.clearAttribute();
		selectedSquare = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quarto_game, menu);
		return true;
	}
	
	private void swapTurn(){
		Log.d("Stupid Debugging", "PlacePiece is: " + (placePiece ? "true" : "false"));
		placePiece = !placePiece;
		if (placePiece) {			
			String placePieceString = currentPlayer.str() + ", place the chosen piece!";
			prompt.setText(placePieceString);
		} else {
			String pickPieceString = currentPlayer.str() + ", pick your opponent's piece!";
			prompt.setText(pickPieceString);
		}
	}

}
