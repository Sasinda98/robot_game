/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.List;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import robotsgame.RobotInfo;

/**
 *
 * @author sasinda
 */
public class State {
    
    private volatile RobotInfo[] robotsArray;
    private int index = 0;
    private volatile LinkedBlockingQueue<Line> linesQueue = new LinkedBlockingQueue<>(100); 
    private volatile  LinkedBlockingQueue<Hit> hitQueue = new LinkedBlockingQueue<>(100); 

    private volatile SynchronousQueue<Object> guiSync = new SynchronousQueue<>();
    
    public State(int size){
        robotsArray = new RobotInfo[size];
    }
    
    public void addRobot(RobotInfo robot){
        if(robotsArray.length > index){
            robotsArray[index] = robot;
            index++;
        }
    }
      
    public RobotInfo getRobot(int index){
       
        return robotsArray[index];
    }
    
    public RobotInfo[] getRobotArray(){
    
        return this.robotsArray;
    }
    
    //LINES QUEUE...............................................................
    public void addLine(Line l){
        boolean isAdded = linesQueue.offer(l); //returns status of add, use if req'd.
        if(isAdded){
          //  addSyncPayload();
        }
    }
    
    public Line removeLine(){
          // addSyncPayload();
        return linesQueue.poll(); //returns null if empty.
    }
    
    public void removeLine(Line l){
          // addSyncPayload();
         linesQueue.remove(l); //returns null if empty.
    }
   
    public LinkedBlockingQueue<Line> getLineQueue(){
        return linesQueue;
    }
   
    //HIT QUEUE.................................................................
    public void addHit(RobotInfo attacker, RobotInfo victim){
        boolean isAdded = hitQueue.offer(new Hit(attacker, victim)); //returns status of add, use if req'd.
        if(isAdded){
              // addSyncPayload();
        }
    }
    
    public Hit removeHit(){
     //   addSyncPayload();
        return hitQueue.poll(); //returns null if empty.
    }
    
    //GUI SYNC
    public void addSyncPayload(){
        guiSync.offer(new Object());
    }
    
    public void renderSync() throws InterruptedException{
        guiSync.take(); //Block the ui thread
    }
    
}
