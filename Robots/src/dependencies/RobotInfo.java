/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dependencies;

/**
 *
 * @author sasinda
 */
public class RobotInfo {
    private String name;
    private int x;
    private int y;
    private double health;
    
    public RobotInfo(String name, int x, int y, double health){
        this.name =  name;
        this.x = x;
        this.y = y;
        this.health = health;
    }
    
    public String getName(){
        return name;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHealth(double health) {
        this.health = health;
    }
    
    public double getHealth() {
        return health;
    }
}
