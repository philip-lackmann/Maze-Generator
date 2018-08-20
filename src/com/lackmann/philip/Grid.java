/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lackmann.philip;

import javafx.scene.Group;
import javafx.scene.paint.Color;

/**
 *
 * @author Philip
 */
public abstract class Grid extends Group{
    private final Cell[][] cells;
    protected Cell chosenCell;
    protected final Color cellsColor;
    
    public Grid(int columns, int rows, double posX, double posY, int cellSize, Color strokeColor, double strokeWidth, Color cellsColor)
    {
        cells = new Cell[columns][rows];
        this.cellsColor = cellsColor;
        
        for (int i=0; i<columns; i++)
        {
            for (int j=0; j<rows; j++)
            {
                Cell cell = new Cell(this, posX + i*cellSize, posY + j*cellSize, cellSize, cellSize, strokeColor, strokeWidth);           
                cells[i][j] = cell;  
                cell.setX(i);
                cell.setY(j);
                cell.setFill(cellsColor);
                super.getChildren().add(cell);
            }
        }
        
        findCellNeighbours(columns, rows);
    }
    
    public Cell getCell(int x, int y)
    {
        try
        {
            return cells[x][y];
        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.println("The cell at the coordinates (" + x + ", " + y +") does not exist.");
            return null;
        }
    }
    
    private void findCellNeighbours(int columns, int rows)
    {
        for (int i=0; i<columns; i++)
        {
            for (int j=0; j<rows; j++)
            {
                if (i > 0)
                    cells[i][j].putNeighbour(Direction.LEFT, cells[i-1][j]);
                if (i < columns-1)
                    cells[i][j].putNeighbour(Direction.RIGHT, cells[i+1][j]);
                if (j > 0)
                    cells[i][j].putNeighbour(Direction.UP, cells[i][j-1]);
                if (j < rows-1)
                    cells[i][j].putNeighbour(Direction.DOWN, cells[i][j+1]);
            }
        }
    }
    
    public void setChosenCell(Cell cell)
    {
        if (chosenCell != null)
            chosenCell.setFill(cellsColor);
        chosenCell = cell;
        chosenCell.setFill(Color.TOMATO);
    }
    
    public Cell getChosenCell()
    {
        return chosenCell;
    }
}
