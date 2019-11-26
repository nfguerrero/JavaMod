package edu.iastate.cs228.hw5.rendering;

public interface BoardCell extends HexGridCell, Cloneable {
    public int getHexX();
    public int getHexY();
    public void setHexX(int x);
    public void setHexY(int y);
    public Object clone();
}
