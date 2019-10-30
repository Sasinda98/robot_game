package robotai;
import static gui.ExampleSwingApp.logger;
import static gui.ExampleSwingApp.print;
import static gui.ExampleSwingApp.state;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import main.RobotAI;
import main.RobotControl;
import static robotsgame.AI.Constants.*;
import main.RobotInfo;
import utils.Line;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
/**
 *
 * @author sasinda
 */
public class GreyAI extends Thread implements RobotAI {
    
    private RobotControl rc;
    
    
    @Override
    public void runAI(RobotControl rc) {
        this.rc = rc;
        //RC contains virtual robots that the AI wishes to control
        this.start(); 
    }//end run ai

    @Override
    public void run() {
        System.out.println("GreyAI thread started");
        //direction := north
        int direction = NORTH; 
     
        while(true){
            RobotInfo myRobot = rc.getRobot();
            
            if(this.isInterrupted() || myRobot.getHealth() <= 0){
                break;
            }
            
            for(RobotInfo temp:rc.getRobots()){

                if((!myRobot.getName().equals(temp.getName()))
                        && ( Math.abs(myRobot.getX() - temp.getX()) <= 2) 
                        && ( Math.abs(myRobot.getY() - temp.getY()) <= 2) 
                        && (!(temp.getHealth() <= 0))   //dont shoot the dead!
                    ){
                    
                    int prevX = temp.getX();
                    int prevY = temp.getY();

                    boolean isFireLegal = false;
                    try {
                        isFireLegal = rc.fire(prevX, prevY); //this func doesn't say whether the laser fire is an actual hit. and hence the code below.
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GreyAI.class.getName()).log(Level.FINE, null, ex);
                        break;
                    }

                    if(isFireLegal){    //commit to firing
                       // logger.append("Robot: " + rc.getRobot().getName() + " FIRED AT " + temp.getName() + "\n");
                       
                       print("Robot: " + rc.getRobot().getName() + " FIRED AT " + temp.getName() + "\n");
                        boolean isAHit = (temp.getX() - prevX == 0) && (temp.getY() - prevY == 0);
                        
                        if(isAHit){
                            temp.setHealth( ( ( temp.getHealth() - 35.0 ) < 0 ) ? 0 : ( temp.getHealth() - 35 ) ); //Prevent negative values for health.
                            
                            System.out.println("LASER HIT");
                          
                          //  print("Robot: " + temp.getName() + " INCURRS DAMAGE." + "\n");
                            SwingUtilities.invokeLater(()->{
                                logger.append("Robot: " + temp.getName() + " INCURRS DAMAGE " + "From: " + myRobot.getName() + "\n"); 
                            });
                            
                            if(temp.getHealth() <= 0){
                                 //logger.append("Robot: " + temp.getName() + " DIED." + "\n");  
                                 //print("Robot: " + temp.getName() + " DIED." + "\n");
                                 SwingUtilities.invokeLater(()->{
                                     logger.append("Robot: " + temp.getName() + " DIED." + "\n");  
                                 });
                            }
                                
                                
                        }else{
                            System.out.println("LASER MISSED");
                            //logger.append("Robot: " + rc.getRobot().getName() + " MISSES THE TARGET. \n");   
                           // print("Robot: " + rc.getRobot().getName() + " MISSES THE TARGET. \n");
                           SwingUtilities.invokeLater(()->{
                                     logger.append("Robot: " + rc.getRobot().getName() + " MISSES THE TARGET. \n");  
                                 });
                        }
                        
                        break;
                    }
                }

            }

            switch(direction) {
                case NORTH: 
                    if(!rc.moveNorth()){
                        direction = EAST;
                    }
                    break;
                case EAST:
                    if(!rc.moveEast()){
                        direction = SOUTH;
                    }
                    break;   
                case SOUTH:
                    if(!rc.moveSouth()){
                        direction = WEST;
                    }
                case WEST: 
                    if(!rc.moveWest()){
                        direction = NORTH;
                    }
                    break;
            }

            try {
                Thread.sleep(1000); //1sec sleep
            } catch (InterruptedException ex) {
                Logger.getLogger(GreyAI.class.getName()).log(Level.FINE, null, ex);
                break;
            }

        }//end while
    }//end run
   
}//end class
    

