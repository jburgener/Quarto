package edu.umich.quarto;

public class QuartoPiece {
    protected int attributes;
    
    public QuartoPiece(int attributes){
        this.attributes = attributes & 0xf;
    }
    
    public int getAttributes(){
        return attributes;
    }
    
    public void setAttributes(int attributes){
        this.attributes = attributes & 0xf;
    }
    
    public boolean hasAttribute(Attribute attr){
    	return (attributes & attr.mask()) > 0;
    }
    
    public static enum Attribute{
    	shape,
    	type,
    	height,
    	color;
    	public int mask(){
    		return 1<<this.ordinal();
    	}
    }
}
