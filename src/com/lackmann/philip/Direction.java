/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lackmann.philip;

/**
 *
 * @author Philip
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT;
    
    private Direction opposite;
    
    static {
        UP.opposite = DOWN;
        DOWN.opposite = UP;
        LEFT.opposite = RIGHT;
        RIGHT.opposite = LEFT;
    }
    
    public Direction inverse()
    {
        return opposite;
    }
    
}
