package edu.umich.quarto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class BoardSquare extends View{
	QuartoPiece piece = null;
	Paint borderPaint = new Paint();
	Paint piecePaint = new Paint();
	int widthScale;
	
	public BoardSquare(Context context){
		this(context, 4);
	}
	public BoardSquare(Context context, int widthScale) {
		super(context);
		borderPaint.setColor(Color.GRAY);
		borderPaint.setStyle(Paint.Style.STROKE);
		piecePaint.setStrokeWidth(5);
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
		}else{
			borderPaint.setColor(Color.GRAY);
			borderPaint.setStrokeWidth(1);
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
		c.drawColor(Color.BLACK);
		c.drawRect(4, 4, getWidth()-5, getHeight()-5, borderPaint);
		
		if(piece == null) return;
		int width = getWidth();
		int height = getHeight();
		
		int renderWidth = getWidth();
        int renderHeight = getHeight();
        int left;
        int top;
        
		if(piece.hasAttribute(QuartoPiece.Attribute.color)){
			piecePaint.setColor(Color.RED);
		}else{
			piecePaint.setColor(Color.BLUE);
		}
		
		if(piece.hasAttribute(QuartoPiece.Attribute.type)){
            renderWidth *= .4;
            renderHeight *= .4;
            left = (int)(width*.3);
            top = (int)(height*.3);
        }else{
            renderWidth *= .75;
            renderHeight *= .75;
            left = width/8;
            top = height/8;
        }
		
		if(piece.hasAttribute(QuartoPiece.Attribute.height)){
			piecePaint.setStyle(Paint.Style.FILL);
        }else{
            piecePaint.setStyle(Paint.Style.STROKE);
        }
		RectF rect = new RectF(left,  top, left+renderWidth, top+renderHeight);
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
