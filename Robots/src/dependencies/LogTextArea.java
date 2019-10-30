/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dependencies;

import javax.swing.JTextArea;

/**
 *
 * @author sasinda
 */
public class LogTextArea {
    private static JTextArea logger;
    
    public static JTextArea getLogTextArea(){
        if(logger == null){
            throw new IllegalStateException("JTextArea for logger is not set.");
        }else{
            return logger;
        }
    }
    
    public static void initializeLogTextArea(JTextArea loggerP){
        logger = loggerP;
    }
}
