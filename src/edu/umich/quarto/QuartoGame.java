package edu.umich.quarto;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class QuartoGame extends Activity {

	BoardSquare[][] board = new BoardSquare[4][4];
	BoardSquare[][] selector = new BoardSquare[8][2];
	BoardSquare selectedSquare = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quarto_game);
		GridLayout grid = (GridLayout)findViewById(R.id.QuartoBoard);
		grid.setRowCount(4);
		grid.setColumnCount(4);
		for(int x = 0; x < 4; x++){
			for(int y = 0; y < 4; y++){
				BoardSquare square = new BoardSquare(this, 4);
				board[x][y] = square;
				square.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						placePiece((BoardSquare)v);
					}
				});
				grid.addView(square);
			}
		}
		
		grid = (GridLayout)findViewById(R.id.PieceSelector);
		grid.setRowCount(2);
		grid.setColumnCount(8);
		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 2; y++){
				BoardSquare square = new BoardSquare(this, 8);
				square.setClickable(true);
				square.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						BoardSquare bs = (BoardSquare)v;
						if(bs.hasPiece()){
							if(selectedSquare != null) selectedSquare.setSelected(false);
							bs.setSelected(true);
							selectedSquare = bs;
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
				if(isWin()){
					Toast.makeText(getApplicationContext(), "Quarto! You Win!", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "NO!!! You're Dumb!", Toast.LENGTH_LONG).show();
				}
			}
		});
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

}
