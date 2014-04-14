package edu.umich.quarto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class BoardSquare extends View {
	QuartoPiece piece = null;
	private Paint borderPaint = new Paint();
	private Paint piecePaint = new Paint();
	private boolean boardPiece;
	private int widthScale;
	
	public BoardSquare(Context context){
		this(context, 4, false);
	}
	public BoardSquare(Context context, int widthScale, boolean board) {
		super(context);
		borderPaint.setColor(Color.argb(255, 243, 170, 125));
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(10);
		if (board)
			piecePaint.setStrokeWidth(20);
		else
			piecePaint.setStrokeWidth(4);
		boardPiece = board;
		this.widthScale = widthScale;
	}
	
	public void clearAttribute(){
		piece = null;
		invalidate();
	}
	public void setAttribute(int attr){
		piece = new QuartoPiece(attr);
		invalidate();
	}
	
	public boolean hasPiece(){
		return piece != null;
	}
	
	public int getAttribute(){
		return piece == null ? -1 : piece.getAttributes();
	}
	public void setSelected(boolean selected){
		if(selected){
			borderPaint.setColor(Color.BLUE);
			borderPaint.setStrokeWidth(4);
		}
		invalidate();
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	   super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	   int parentDimension = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	   this.setMeasuredDimension(parentDimension/widthScale, parentDimension/widthScale);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas c){
		c.drawColor(Color.argb(255, 73, 90, 97)); // background
		
		if (boardPiece) {	// draw circles on the board pieces
			float cx = getWidth() / 2, cy = getHeight() / 2, radius = cx - 4;
			c.drawCircle(cx, cy, radius, borderPaint);
		}
		
		if (piece == null) return;
        
		if(piece.hasAttribute(QuartoPiece.Attribute.color)){
			piecePaint.setColor(Color.RED);
		}else{
			piecePaint.setColor(Color.BLUE);
		}
		
		float width = getWidth(), height = getHeight();
		float renderWidth = getWidth(), renderHeight = getHeight(), left, top;
		if (boardPiece){
			if(piece.hasAttribute(QuartoPiece.Attribute.type)) {		// what in the world does this mean?
				left = (float) (width * .3);
				top = (float) (height * .3);
	            renderWidth *= .4;
	            renderHeight *= .4;
	        } else {
	        	left = (int)(width * .225);
				top = (int)(height * .225);
	            renderWidth *= .56;
	            renderHeight *= .56;
	        }
		} else {
			if(piece.hasAttribute(QuartoPiece.Attribute.type)){		// what in the world does this mean?
	            renderWidth *= .4;
	            renderHeight *= .4;
	            left = (int)(width * .3);
	            top = (int)(height * .3);
	        }else{
	            renderWidth *= .75;
	            renderHeight *= .75;
	            left = width/8;
	            top = height/8;
	        }
		}
		
		if(piece.hasAttribute(QuartoPiece.Attribute.height)){
			piecePaint.setStyle(Paint.Style.FILL);
        }else{
            piecePaint.setStyle(Paint.Style.STROKE);
        }
		RectF rect = new RectF(left, top, left+renderWidth, top+renderHeight);
		if(piece.hasAttribute(QuartoPiece.Attribute.shape)){
			c.drawRect(rect, piecePaint);
        } else{
        	c.drawOval(rect, piecePaint);
        }
	}
	public int matchMask(int attr) {
		if(piece == null) return 0;
        int toMatch = piece.getAttributes();
        int result = 0;
        for(int i = 0; i<4; i++){
            int bit = 1<<i;
            if((toMatch&bit) == (attr&bit)) result = result | bit;
        }
        
        return result;
	}
}
