/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lackmann.philip;

import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author Philip
 */
public class Cell extends Group{
    
    private final HashMap<Direction, Cell> neighbours = new HashMap<>();  
    private final HashMap<Direction, Line> walls = new HashMap<>();    
    private final Rectangle background;
    
    private int x;
    private int y;
    
    private boolean isFillLocked;
    
    public Cell(Grid grid, double posX, double posY, double width, double height, Color strokeColor, double strokeWidth)
    {
        //draw the walls as lines
        Line wallU = new Line(posX, posY, posX + width, posY);
        Line wallD = new Line(posX, posY + height, posX + width, posY + height);
        Line wallL = new Line(posX, posY, posX, posY + height);
        Line wallR = new Line(posX + width, posY, posX + width, posY + height);
        
        //put the walls into the hashmap
        walls.put(Direction.LEFT, wallL);
        walls.put(Direction.RIGHT, wallR);
        walls.put(Direction.UP, wallU);
        walls.put(Direction.DOWN, wallD);
        
        //adjust all the walls
        for (HashMap.Entry<Direction, Line> entry : walls.entrySet())
        {
            Line value = entry.getValue();
            value.setStrokeWidth(strokeWidth);
            value.setStroke(strokeColor);
            //pane.getChildren().add(value);
            value.setStrokeType(StrokeType.CENTERED);
            super.getChildren().add(value);
        }     

        background = new Rectangle();
            background.setWidth(width);
            background.setHeight(height);
            background.setLayoutX(posX);
            background.setLayoutY(posY);
            background.setStroke(Color.TRANSPARENT);
            background.setFill(Color.TRANSPARENT);
        super.getChildren().add(background);
        background.toBack();
        
        background.setOnMouseClicked((MouseEvent arg0) ->
        {
            grid.setChosenCell(this);
        });
    }
    
    public void deleteWall(Direction dir)
    {
        getChildren().remove(walls.get(dir));
        walls.remove(dir);
    }
    
    public void setFill(Color c)
    {
        if (!isFillLocked)
        {
            background.setFill(c);
        }
    }
    
    public void lockFill(boolean isFillLocked)
    {
        this.isFillLocked = isFillLocked;
    }
    
    public void putNeighbour(Direction dir, Cell cell)
    {
        neighbours.put(dir, cell);
    }
    
    public HashMap<Direction, Cell> getNeighbours()
    {
        return neighbours;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
}
