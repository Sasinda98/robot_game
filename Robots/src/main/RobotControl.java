/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import gui.ExampleSwingApp;
import static gui.ExampleSwingApp.state;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotai.GreyAI;

import robotsgame.RobotInfo;
import utils.Line;

/**
 *
 * @author sasinda
 */



public class RobotControl {
    
    private int gridX = 12, gridY = 8;
    
    RobotInfo robotToControl = null;
    int select;
    
    public RobotControl(RobotInfo robot){
        this.robotToControl = robot;
    }
    
    public RobotInfo getRobot(){
        return robotToControl;
    }
    
    public RobotInfo[] getRobots(){
        return state.getRobotArray();
    }
    
    public boolean moveNorth(){
        int newY = robotToControl.getY() - 1;
        boolean isAlive = robotToControl.getHealth() > 0;
        
        if(newY >= 0 && !isAlreadyOccupied(robotToControl.getX(), newY) && isAlive){
            robotToControl.setY(newY);
            state.addSyncPayload();
            System.out.println("Moving North, " + robotToControl.getName() + " X = " + robotToControl.getX() + " Y = " + robotToControl.getY());
            return true;
        }else{
            return false;
        } 
    }
    
    public boolean moveSouth(){
        int newY = robotToControl.getY() + 1;
        boolean isAlive = robotToControl.getHealth() > 0;
        
        if(newY < gridY && !isAlreadyOccupied(robotToControl.getX(), newY) && isAlive){
            robotToControl.setY(newY);
            state.addSyncPayload();
            System.out.println("Moving South, " + robotToControl.getName() + " X = " + robotToControl.getX() + " Y = " + robotToControl.getY());
            return true;
        }else{
            return false;
        } 
    }
    
    public boolean moveWest(){
        int newX = robotToControl.getX() - 1;
        boolean isAlive = robotToControl.getHealth() > 0;
        
        if(newX >= 0 && !isAlreadyOccupied(newX, robotToControl.getY()) && isAlive){
            robotToControl.setX(newX);
            state.addSyncPayload();
            System.out.println("Moving West, " + robotToControl.getName() + " X = " + robotToControl.getX() + " Y = " + robotToControl.getY());
            return true;
        }else{
            return false;
        }
    }
    
    public boolean moveEast(){
        int newX = robotToControl.getX() + 1;
        boolean isAlive = robotToControl.getHealth() > 0;
        
        if(newX < gridX && !isAlreadyOccupied(newX, robotToControl.getY()) && isAlive){
            robotToControl.setX(newX);
            state.addSyncPayload();
            System.out.println("Moving East, " + robotToControl.getName() + " X = " + robotToControl.getX() + " Y = " + robotToControl.getY());
            return true;
        }else{
            return false;
        }
    }
    
    /***
     * Returns true if the fire is legal and should not indicate if it hit anything according to assignment spec.
     * @param targetX
     * @param targetY
     * @return 
     */
    public boolean fire(int targetX, int targetY) throws InterruptedException{
         boolean isXDiffOK = (Math.abs(robotToControl.getX() - targetX) <= 2);
         boolean isYDiffOK = (Math.abs(robotToControl.getY() - targetY) <= 2);              
        
        if((robotToControl.getHealth() > 0) && isXDiffOK && isYDiffOK){
            
            Thread.sleep(500);  //delay prior to firing
            
            //FIRE THE LASER!
                //Code to draw line
            Line l = new Line(robotToControl.getX(), robotToControl.getY(), targetX, targetY);
            state.addLine(l);
            
            Thread.sleep(250);  //line delay, show for 250ms
           
            state.removeLine(l);  
            return true;
        }
        return false;
    }
    
    public boolean isAlreadyOccupied(int x, int y){
        
        for(RobotInfo robot: getRobots()){
            if(robot.getX() == x && robot.getY() == y){
                return true;
            }
        }

        return false;
    }
}
