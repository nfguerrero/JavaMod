package edu.iastate.cs228.hw5.rendering;

import edu.iastate.cs228.hw5.mapStructures.Coordinate;

public interface HexGridCell extends Cloneable {
    public Coordinate getCoordinates();
    public void setCoordinate(Coordinate ycx);
    public void setCoordinate(int index);
    public Object clone();

}
