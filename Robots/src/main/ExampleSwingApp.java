package main;

import dependencies.LogTextArea;
import main.SwingArena;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import dependencies.RobotControl;
import robotai.GreyAI;
import dependencies.RobotInfo;
import dependencies.State;

public class ExampleSwingApp 
{
    public static JTextArea logger; //needs to be accessed outside this class.
    private static Object loggerMutex = new Object();
    private static State state; 
    
    public static void print(String string){
        synchronized(loggerMutex){
            if(logger!=null)
                logger.append(string);
        }
    }
    
    public static void main(String[] args) 
    {
        // Note: SwingUtilities.invokeLater() is equivalent to JavaFX's Platform.runLater().
        SwingUtilities.invokeLater(() ->
        {
            JFrame window = new JFrame("Robot AI Test (Swing)");
            SwingArena arena = new SwingArena();
            
            JToolBar toolbar = new JToolBar();
            JButton btn1 = new JButton("Start");
            JButton btn2 = new JButton("Stop");
            toolbar.add(btn1);
            toolbar.add(btn2);

            logger = new JTextArea();
            JScrollPane loggerArea = new JScrollPane(logger);
            loggerArea.setBorder(BorderFactory.createEtchedBorder());
            LogTextArea.initializeLogTextArea(logger);
            
            
            /***
             * Title: Used link below to fix no scroll in text area.
             * Link: https://stackoverflow.com/questions/10177183/java-add-scroll-into-text-area
             * Author: Mikle Garin
             * Accessed: 9th October 2019
             */
            JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, arena, loggerArea);

            Container contentPane = window.getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(toolbar, BorderLayout.NORTH);
            contentPane.add(splitPane, BorderLayout.CENTER);
            
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setPreferredSize(new Dimension(800, 800));
            window.pack();
            window.setVisible(true);
            
            splitPane.setDividerLocation(0.75);
            
           // function(arena);
           
            state = State.getInstance();
            state.setRobotArraySize(5);
            
            state.addRobot(new RobotInfo("Robot-1", 1, 1, 100.0));
            state.addRobot(new RobotInfo("Robot-2", 3, 3, 100.0));
            state.addRobot(new RobotInfo("Robot-3", 2, 2, 100.0));
            state.addRobot(new RobotInfo("Robot-4", 6, 6, 100.0));
            state.addRobot(new RobotInfo("Robot-5", 4, 4, 100.0));

            RobotControl rc1 = new RobotControl(state.getRobot(0));
            GreyAI greyAI1 = new GreyAI();
            //greyAI1.runAI(rc1);

            RobotControl rc2 = new RobotControl(state.getRobot(1));
            GreyAI greyAI2 = new GreyAI();
           // greyAI2.runAI(rc2);

            RobotControl rc3 = new RobotControl(state.getRobot(2));
            GreyAI greyAI3 = new GreyAI();
          //  greyAI3.runAI(rc3);

            RobotControl rc4 = new RobotControl(state.getRobot(3));
            GreyAI greyAI4 = new GreyAI();
            //greyAI4.runAI(rc4);

            RobotControl rc5 = new RobotControl(state.getRobot(4));
            GreyAI greyAI5 = new GreyAI();
            //greyAI5.runAI(rc5);
            
            Thread guiThread = guiThreadInit(arena);
            
            btn1.addActionListener((event) ->
            {
                LogTextArea.getLogTextArea().append("Start Pressed\n");
       
                /***
                 * Title: Referred to the link below to solve the problem of IllegalThreadStateException
                 * Link: https://stackoverflow.com/questions/7315941/java-lang-illegalthreadstateexception
                 * Author: Toby
                 * Accessed: 30th October 2019
                 */
                if(guiThread.getState() == Thread.State.NEW){ //Checking the guiThread is enough
                    guiThread.start();
                    greyAI1.runAI(rc1);
                    greyAI2.runAI(rc2);
                    greyAI3.runAI(rc3);
                    greyAI4.runAI(rc4);
                    greyAI5.runAI(rc5);
                 
                }else{
                    LogTextArea.getLogTextArea().append("Gui Thread not new\n");
                }
                
            });
            
            btn2.addActionListener((ev)->{
                LogTextArea.getLogTextArea().append("Stop Pressed\n");
                guiThread.interrupt();
                greyAI1.interrupt();
                greyAI2.interrupt();
                greyAI3.interrupt();
                greyAI4.interrupt();
                greyAI5.interrupt();
                
            });
        });
        
    }
    
    
    public static void function(SwingArena arena){
        state = State.getInstance();
        state.setRobotArraySize(5);
        state.addRobot(new RobotInfo("Robot-1", 1, 1, 100.0));
        state.addRobot(new RobotInfo("Robot-2", 3, 3, 100.0));
        state.addRobot(new RobotInfo("Robot-3", 2, 2, 100.0));
        state.addRobot(new RobotInfo("Robot-4", 6, 6, 100.0));
        state.addRobot(new RobotInfo("Robot-5", 4, 4, 100.0));
        
        RobotControl rc1 = new RobotControl(state.getRobot(0));
        GreyAI greyAI1 = new GreyAI();
        greyAI1.runAI(rc1);
        
        RobotControl rc2 = new RobotControl(state.getRobot(1));
        GreyAI greyAI2 = new GreyAI();
        greyAI2.runAI(rc2);
        
        RobotControl rc3 = new RobotControl(state.getRobot(2));
        GreyAI greyAI3 = new GreyAI();
        greyAI3.runAI(rc3);
        
        RobotControl rc4 = new RobotControl(state.getRobot(3));
        GreyAI greyAI4 = new GreyAI();
        greyAI4.runAI(rc4);
        
        RobotControl rc5 = new RobotControl(state.getRobot(4));
        GreyAI greyAI5 = new GreyAI();
        greyAI5.runAI(rc5);
        
        
        Runnable gui = () -> {
            State prev = null;
            while(true){
        
                try {
                   
                    Thread.sleep(250);

                    arena.repaint();
                    
                    int deadRobots=0;
                    for(int i =0; i < state.getRobotArray().length; i++){
                        if(state.getRobotArray()[i].getHealth() == 0){
                            deadRobots++;
                        }
                    }
                    
                    if(state.getRobotArray().length - 1 == deadRobots){
                        System.out.println("Ending, last robot standing........");
                        LogTextArea.getLogTextArea().append("Ending, Last Robot Standing. GAME OVER\n");
                        break;
                    }
              
  
                } catch (InterruptedException ex) {
                    Logger.getLogger(ExampleSwingApp.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }

            }
        };
        
        Thread guiUpdate = new Thread(gui, "GUI UPDATER THREAD");
        guiUpdate.start();
       
    }
    
    public static Thread guiThreadInit(SwingArena arena){
              Runnable gui = () -> {
            State prev = null;
            while(true){
        
                try {
                   
                    Thread.sleep(250);
                    
                    SwingUtilities.invokeLater(()->{
                        arena.repaint();
                    });
             
                    int deadRobots=0;
                    for(int i =0; i < state.getRobotArray().length; i++){
                        if(state.getRobotArray()[i].getHealth() == 0){
                            deadRobots++;
                        }
                    }
                    
                    if(state.getRobotArray().length - 1 == deadRobots){
                        System.out.println("Ending, last robot standing........");
                       
                        SwingUtilities.invokeLater(()->{
                            LogTextArea.getLogTextArea().append("Ending, Last Robot Standing. GAME OVER\n");
                        });
                        
                        break;
                    }
              
  
                } catch (InterruptedException ex) {
                    Logger.getLogger(ExampleSwingApp.class.getName()).log(Level.FINE, null, ex);
                    break;
                }

            }
        };
        
        Thread guiUpdate = new Thread(gui, "GUI UPDATER THREAD");
        return guiUpdate;
    }
}
