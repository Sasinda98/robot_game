/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dependencies;

import main.Line;
import main.Hit;
import java.awt.List;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import dependencies.RobotInfo;
import main.Hit;
import main.Line;

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
    
    private State(){

    }
    
    private static State instance;
    
    public static State getInstance(){
        if(instance == null){
            instance = new State();
        }
        return instance;
    }
    
    
    public void setRobotArraySize(int size){
            robotsArray = new RobotInfo[size];
    }
    
    public void addRobot(RobotInfo robot){
        if(robotsArray == null){
            throw new IllegalStateException("Robot array is not initialized, set its value using setRobotArraySize(int size)");
        }
        
        if(robotsArray.length > index){
            robotsArray[index] = robot;
            index++;
        }
    }
      
    public RobotInfo getRobot(int index){
        if(robotsArray == null){
            throw new IllegalStateException("Robot array is not initialized, set its value using setRobotArraySize(int size)");
        }
        return robotsArray[index];
    }
    
    public RobotInfo[] getRobotArray(){
        if(robotsArray == null){
            throw new IllegalStateException("Robot array is not initialized, set its value using setRobotArraySize(int size)");
        }
        return this.robotsArray;
    }
    
    //LINES QUEUE...............................................................
    public void addLine(Line l){
        boolean isAdded = linesQueue.offer(l); //returns status of add, use if req'd.
        if(isAdded){
           addSyncPayload();
        }
    }
    
    public Line removeLine(){
        addSyncPayload();
        return linesQueue.poll(); //returns null if empty.
    }
    
    public boolean removeLine(Line l){
         addSyncPayload();
         return linesQueue.remove(l); 
    }
   
    public LinkedBlockingQueue<Line> getLineQueue(){
        return linesQueue;
    }
   
    //HIT QUEUE.................................................................
    public void addHit(RobotInfo attacker, RobotInfo victim){
        boolean isAdded = hitQueue.offer(new Hit(attacker, victim)); //returns status of add, use if req'd.
        if(isAdded){
            addSyncPayload();
        }
    }
    
    public boolean removeHit(Hit h){
        addSyncPayload();
        return hitQueue.remove(h);
    }
    
    public Hit removeHit(){
        addSyncPayload();
        return hitQueue.poll();
    }
    
    public LinkedBlockingQueue<Hit> getHitQueue(){
     //   addSyncPayload();
        return hitQueue; //returns null if empty.
    }
    
    //GUI SYNC
    public void addSyncPayload(){
        guiSync.offer(new Object());
    }
    
    public void renderSync() throws InterruptedException{
        guiSync.take(); //Block the ui thread
    }
    
}
