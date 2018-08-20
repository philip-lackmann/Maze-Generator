/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lackmann.philip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Philip
 */
public final class Maze extends Grid{
    
    private final Stack<Cell> visited = new Stack<>();  
    private final Stack<Cell> finished = new Stack<>();
    private Cell currentCell;
    private boolean isGenerating;
    
    //Local timeline doesnt work because I access it in the code before the timeline is initialized,
    //but in the end it will only get accessed after initializing anyways.
    //I would get a compiling error with a local timeline.
    private Timeline timeline;
    
    public Maze(int columns, int rows, double posX, double posY, int cellSize, Color strokeColor, double strokeWidth, Color cellsColor)
    {
        super(columns, rows, posX, posY, cellSize, strokeColor, strokeWidth, cellsColor);
    }
    
    public void depthFirstSearchMaze(double intervalInMillis, final boolean visualize, Color currentCellColor, Color visitedCellsColor, Color finishedCellsColor)
    {
        currentCell = chosenCell;     
        currentCell.setFill(Color.RED);
        currentCell.lockFill(true);
        
        if (visualize)
        {
            isGenerating = true;
            timeline = new Timeline(new KeyFrame(Duration.millis(intervalInMillis), ev ->
            {
                depthFirstSearch(currentCellColor, visitedCellsColor, finishedCellsColor);
                if (finished.contains(chosenCell))
                {
                    timeline.stop();
                    System.out.println("done");
                    isGenerating = false;
                    
                    for (Cell c : finished)
                        c.setFill(cellsColor);
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        else
        {
            while (!finished.contains(chosenCell))
            {
                depthFirstSearch(currentCellColor, visitedCellsColor, finishedCellsColor);
                for (Cell c : finished)
                        c.setFill(cellsColor);
            }
        }
    }
 
    private void depthFirstSearch(Color currentCellColor, Color visitedCellsColor, Color finishedCellsColor)
    {
        //get an arraylist of all unvisited neighbour cell's directions
        ArrayList<Direction> directions = new ArrayList<>();
        for (HashMap.Entry<Direction, Cell> entry : currentCell.getNeighbours().entrySet())
        {
            if (!visited.contains(entry.getValue()) && !finished.contains(entry.getValue()))
            {
                directions.add(entry.getKey());
            }
        }

        //if there are no free neighbours pop off the the visited cell and mark it as finished
        if (directions.isEmpty())
        {
            finished.push(currentCell);
            currentCell.setFill(finishedCellsColor);
            if (visited.size() > 0)
            {
                currentCell = visited.peek();
                visited.pop();           
            }
        }
        else
        {
            //mark the currentCell cell as visited
            visited.push(currentCell);
            currentCell.setFill(visitedCellsColor);    
            //generate random index
            Random rn = new Random();
            int i = rn.nextInt(((directions.size() - 1) - 0) + 1) + 0;
            //delete the wall facing in the randomly chosen direction
            currentCell.deleteWall(directions.get(i));       
            //decide the next cell in a random direction
            currentCell = currentCell.getNeighbours().get(directions.get(i));         
            //delete the remaining wall of the new currentCell
            currentCell.deleteWall(directions.get(i).inverse());
        }
        currentCell.setFill(currentCellColor);
    }  
    
    @Override
    public void setChosenCell(Cell cell)
    {
        if (!isGenerating)
        {
            super.setChosenCell(cell);
        }
    }
}
