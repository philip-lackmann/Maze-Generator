/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lackmann.philip;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Philip
 */
public class MainViewController implements Initializable {
    
    private double x;
    private double y;
    
    private Stage stage;
    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnGenerate;

    private Grid maze;
    
    private int columns = 10;
    private int rows = 10;
    private int cellSize = 40;
    private double intervalInMillis = 100;
    private double wallsWidth = 4;
    private boolean visualize = true;
    private final Color strokeColor = Color.BLACK;
    
    private final Color currentCellColor = Color.CHARTREUSE;
    private final Color visitedCellsColor = Color.CRIMSON;
    private final Color finishedCellsColor = Color.KHAKI;
    
    private double posX;
    private double posY;

    @FXML
    private Slider sliderColumns;
    @FXML
    private Slider sliderRows;
    @FXML
    private Slider sliderCellSize;
    @FXML
    private Slider sliderWallsWidth;
    
    @FXML
    private Label lblColumnsValue;
    @FXML
    private Label lblRowsValue;
    @FXML
    private Label lblCellSizeValue;
    @FXML
    private Label lblWallsWidthValue;
    
    @FXML
    private TextField txtFieldInterval;
    @FXML
    private TextField txtFieldTotalTime;
    
    @FXML
    private CheckBox checkBoxVisualize;
    
    private boolean initializing = true;
    
    //boolean to not trigger the listener of the other textfield when setting the text of it
    private boolean intervalChanged;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        stage = Main.getPrimaryStage();     
        
        //Add all listeners to the nodes for the control of the maze's properties
        sliderColumns.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) ->
        {
            columns = (int) Math.round(newVal.doubleValue());
            lblColumnsValue.setText(String.valueOf(columns));
            if (800 / columns > 500 / rows)
            {
                sliderCellSize.setMax(800 / columns);
                sliderCellSize.setValue(cellSize);
            }     
            if (!initializing)
            {
                updateGrid();
                intervalChanged = true;
                txtFieldTotalTime.setText(String.valueOf(rows * columns * 2));
            }
        });

        sliderRows.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) ->
        {
            rows = (int) Math.round(newVal.doubleValue());
            lblRowsValue.setText(String.valueOf(rows));
            if (800 / columns < 500 / rows)
            {
                sliderCellSize.setMax(500 / rows);
                sliderCellSize.setValue(cellSize);
            }      
            if (!initializing)
            {
                updateGrid();
                intervalChanged = true;
                txtFieldTotalTime.setText(String.valueOf(rows * columns * 2));
            }
        });

        sliderCellSize.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) ->
        {
            cellSize = (int) Math.round(newVal.doubleValue());
            
            lblCellSizeValue.setText(String.valueOf(cellSize));

            sliderColumns.setMax(800 / cellSize);
            sliderColumns.setValue(columns);

            sliderRows.setMax(500 / cellSize);
            sliderRows.setValue(rows);
            
            sliderWallsWidth.setMax(Math.round(cellSize*0.5));
            if (!initializing)
                updateGrid();
        });
        
        sliderWallsWidth.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) ->
        {
            wallsWidth = newVal.doubleValue();
            lblWallsWidthValue.setText(String.format("%.1f", wallsWidth));
            if (!initializing)
                updateGrid();
        });
        
        txtFieldInterval.textProperty().addListener((ObservableValue<? extends String> ov, String oldVal, String newVal) ->
        {
            if (intervalChanged)
            {
                if (!newVal.matches("\\d{0,5}(\\.(\\d{0,1}))?"))
                {
                  txtFieldInterval.setText(oldVal);
                }
                else if (!newVal.equals("") && !newVal.equals("0") && !newVal.equals("0."))
                {
                   txtFieldTotalTime.setText(String.format("%.1f", Double.parseDouble(newVal) * rows * columns * 2));
                   intervalInMillis = Double.parseDouble(newVal);
                }
            }
            intervalChanged = true;
        });
        
        txtFieldTotalTime.textProperty().addListener((ObservableValue<? extends String> ov, String oldVal, String newVal) ->
        {
            if (!intervalChanged)
            {
                if (!newVal.matches("\\d{0,6}(\\.(\\d{0,1}))?"))
                {
                    txtFieldTotalTime.setText(oldVal);
                }
                else if (!newVal.equals("") && !newVal.equals("0") && !newVal.equals("0."))
                {
                    intervalInMillis = Double.parseDouble(newVal) / (rows*columns*2);
                    txtFieldInterval.setText(String.format("%.1f", intervalInMillis));
                }
            }
            intervalChanged = false;
        });
        
        //set the values of the nodes to control the maze's properties
        sliderColumns.setValue(columns);
        sliderRows.setValue(rows);
        sliderCellSize.setValue(cellSize);
        sliderWallsWidth.setValue(wallsWidth);
        txtFieldInterval.setText(String.valueOf(intervalInMillis));
        txtFieldTotalTime.setText(String.format("%.1f", intervalInMillis * Math.round(rows*columns*2)));
        checkBoxVisualize.setSelected(visualize);
        
        //calculate the start positions of the maze
        posX = (anchorPane.getPrefWidth() + 430 - 40)/2 - cellSize*columns/2 ;
        posY = anchorPane.getPrefHeight()/2 - cellSize*rows/2 - 10;  
        
        maze = new Maze(columns, rows, posX, posY, cellSize, strokeColor, wallsWidth, Color.TRANSPARENT);
        maze.setChosenCell(maze.getCell(0, 0));
        anchorPane.getChildren().add(maze);
        
        initializing = false;
    }
 
    
    @FXML
    private void generate()
    {
        updateGrid();       
        visualize = checkBoxVisualize.isSelected();
        
        ((Maze) maze).depthFirstSearchMaze(intervalInMillis, visualize, currentCellColor, visitedCellsColor, finishedCellsColor);
    } 
    
    private void updateGrid()
    {
        posX = (stage.getWidth() + 430 - 40)/2 - cellSize*columns/2 ;
        posY = stage.getHeight()/2 - cellSize*rows/2 - 10;  
        //delete the current maze
        anchorPane.getChildren().remove(maze);
        
        //save the coordinates of the chosen cell into these variables
        int chosenCellX;
        int chosenCellY;
        //get the new coordinates of the chosenCell if the maze gets set too small
        if (maze.getChosenCell().getX() >= columns)
            chosenCellX = columns - 1;
        else
            chosenCellX = maze.getChosenCell().getX();
            
        if (maze.getChosenCell().getY() >= rows)
            chosenCellY = rows - 1;
        else
            chosenCellY = maze.getChosenCell().getY();
        
        //generate a new maze
        maze = new Maze(columns, rows, posX, posY, cellSize, strokeColor, wallsWidth, Color.TRANSPARENT);
        //Transfer the chosenCell from the old maze to the new maze with the coordinates from the old chosenCell
        maze.setChosenCell(maze.getCell(chosenCellX, chosenCellY));
        
        anchorPane.getChildren().add(maze);
    }
    
    @FXML
    private void close()
    {
        stage.close();
    }
    
    @FXML
    private void minimize()
    {
        stage.setIconified(true);
    }
    
    @FXML
    private void onPanePressed(MouseEvent event)
    {
        x = stage.getX() - event.getScreenX();
        y = stage.getY() - event.getScreenY();
    }

    @FXML
    private void onPaneDragged(MouseEvent event)
    {
        stage.setX(event.getScreenX() + x);
        stage.setY(event.getScreenY() + y);
    }
    
}
