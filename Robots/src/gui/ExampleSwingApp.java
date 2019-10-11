package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import main.RobotControl;
import robotai.GreyAI;
import robotsgame.RobotInfo;
import utils.State;

public class ExampleSwingApp 
{
    public static void main(String[] args) 
    {
        // Note: SwingUtilities.invokeLater() is equivalent to JavaFX's Platform.runLater().
        SwingUtilities.invokeLater(() ->
        {
            JFrame window = new JFrame("Robot AI Test (Swing)");
            SwingArena arena = new SwingArena();
            
            JToolBar toolbar = new JToolBar();
            JButton btn1 = new JButton("My Button 1");
            JButton btn2 = new JButton("My Button 2");
            toolbar.add(btn1);
            toolbar.add(btn2);
            
            btn1.addActionListener((event) ->
            {
                System.out.println("Button 1 pressed");
            });
            
            JTextArea logger = new JTextArea();
            JScrollPane loggerArea = new JScrollPane(logger);
            loggerArea.setBorder(BorderFactory.createEtchedBorder());
            logger.append("Hello\n");
            logger.append("World\n");
            
            
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
            
            function(arena);
        });
        
    }
    
    public static State state;
    
    public static void function(SwingArena arena){
        state = new State(5);
        state.addRobot(new RobotInfo("Robot-1", 1, 1, 1000.0));
        state.addRobot(new RobotInfo("Robot-2", 3, 3, 1000.0));
        state.addRobot(new RobotInfo("Robot-3", 2, 2, 1000.0));
        state.addRobot(new RobotInfo("Robot-4", 6, 6, 1000.0));
        state.addRobot(new RobotInfo("Robot-5", 4, 4, 1000.0));
        
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
                 
                    arena.repaint();
                  
                    Thread.sleep(250);
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(ExampleSwingApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        
        Thread guiUpdate = new Thread(gui, "GUI UPDATER THREAD");
        guiUpdate.start();
       
    }
}
