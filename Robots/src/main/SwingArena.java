package main;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import dependencies.RobotInfo;
import main.Hit;
import main.Line;

/**
 * A Swing GUI element that displays a grid on which you can draw images, text and lines.
 */
public class SwingArena extends JPanel
{
    // Represents the image to draw. You can modify this to introduce multiple images.
    private static final String IMAGE_NORMAL = "./assets/1554047213.png";
    private static final String IMAGE_ATTACKER = "./assets/attacker.png";
    private static final String IMAGE_VICTIM = "./assets/victim.png";
    
    private ImageIcon robot_normal_img;
    private ImageIcon robot_attacker_img;
    private ImageIcon robot_victim_img;
    
    

    // The following values are arbitrary, and you may need to modify them according to the 
    // requirements of your application.
    private int gridWidth = 12;
    private int gridHeight = 8;
    private double robotX = 1;
    private double robotY = 3;
    
    private double gridSquareSize; // Auto-calculated
    

    /**
     * Creates a new arena object, loading the robot image.
     */
    public SwingArena()
    {
        // Here's how you get an Image object from an image file (which you provide in the 
        // 'resources/' directory.
        robot_normal_img = new ImageIcon(getClass().getClassLoader().getResource(IMAGE_NORMAL));
        robot_attacker_img = new ImageIcon(getClass().getClassLoader().getResource(IMAGE_ATTACKER));
        robot_victim_img = new ImageIcon(getClass().getClassLoader().getResource(IMAGE_VICTIM));
        // You will get an exception here if the specified image file cannot be found.
    }
    
    
    /**
     * Moves a robot image to a new grid position. This method is a *demonstration* of how you
     * can do such things, and you may want or need to modify it substantially.
     */
    public void setRobotPosition(double x, double y)
    {
       // robotX = x;
        //robotY = y;
        repaint();
    }
    
    public void drawRobot(Graphics2D gfx, ImageIcon robotIcon, 
                            String robotName, double health, 
                            int robotX, int robotY
                         ){
        drawImage(gfx, robotIcon, robotX, robotY);
        drawLabel(gfx, robotName + " " +  "(" + health + "%)", robotX, robotY);
        
      //  drawLine(gfx, robotX, robotY, robotX + 2.0, robotY - 2.0);
    }
    
    
    /**
     * This method is called in order to redraw the screen, either because the user is manipulating 
     * the window, OR because you've called 'repaint()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D gfx = (Graphics2D) g;
        gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                             RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            (double) getWidth() / (double) gridWidth,
            (double) getHeight() / (double) gridHeight);
            
        int arenaPixelWidth = (int) ((double) gridWidth * gridSquareSize);
        int arenaPixelHeight = (int) ((double) gridHeight * gridSquareSize);
            
            
        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setColor(Color.GRAY);
        gfx.drawRect(0, 0, arenaPixelWidth - 1, arenaPixelHeight - 1); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            int x = (int) ((double) gridX * gridSquareSize);
            gfx.drawLine(x, 0, x, arenaPixelHeight);
        }
        
        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            int y = (int) ((double) gridY * gridSquareSize);
            gfx.drawLine(0, y, arenaPixelWidth, y);
        }

        
       /* // Invoke helper methods to draw things at the current location.
        // ** You will need to adapt this to the requirements of your application. **
        drawImage(gfx, robot1, robotX, robotY);
        drawLabel(gfx, "Robot Name (100%)", robotX, robotY);
        drawLine(gfx, robotX, robotY, robotX + 1.0, robotY - 2.0); */

       Hit hit = null;
       State state = State.getInstance();
       if(state != null)
       for(RobotInfo robot: state.getRobotArray()){
           
            drawRobot(gfx, robot_normal_img, robot.getName(), robot.getHealth(), robot.getX(), robot.getY());
            
            hit = state.getHitQueue().peek();
            
            if(hit != null){
                drawRobot(gfx, robot_attacker_img, hit.getAttacker().getName(), hit.getAttacker().getHealth(),hit.getAttacker().getX(), hit.getAttacker().getY());
                drawRobot(gfx, robot_victim_img, hit.getVictim().getName(), hit.getVictim().getHealth(),hit.getVictim().getX(), hit.getVictim().getY());
                System.out.println("Attacker = " + hit.getAttacker().getName() + " and victim = " + hit.getVictim().getName() + " painting...");
                state.removeHit();
            }
            else{
               
                drawRobot(gfx, robot_normal_img, robot.getName(), robot.getHealth(),robot.getX(), robot.getY());
            }
            
            
            for(Line l : state.getLineQueue()){
                drawLine(gfx, l.getStartX(), l.startY, l.endX, l.endY);
            }
            
       }
       
       
    } 
    
    
    /** 
     * Draw an image in a specific grid location. *Only* call this from within paintComponent(). 
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image 
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *     
     * You shouldn't need to modify this method.
     */
    private void drawImage(Graphics2D gfx, ImageIcon icon, double gridX, double gridY)
    {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = (double) robot_normal_img.getIconWidth();
        double fullSizePixelHeight = (double) robot_normal_img.getIconHeight();
        
        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to 
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(icon.getImage(), 
            (int) (x - displayedPixelWidth / 2.0),  // Top-left pixel coordinates.
            (int) (y - displayedPixelHeight / 2.0), 
            (int) displayedPixelWidth,              // Size of displayed image.
            (int) displayedPixelHeight, 
            null);
    }
    
    
    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within 
     * paintComponent(). 
     *
     * You shouldn't need to modify this method.
     */
    private void drawLabel(Graphics2D gfx, String label, double gridX, double gridY)
    {
        gfx.setColor(Color.BLUE);
        FontMetrics fm = gfx.getFontMetrics();
        gfx.drawString(label, 
            (int) ((gridX + 0.5) * gridSquareSize - (double) fm.stringWidth(label) / 2.0), 
            (int) ((gridY + 1.0) * gridSquareSize) + fm.getHeight());
    }
    
    /** 
     * Draws a (slightly clipped) line between two grid coordinates. 
     *
     * You shouldn't need to modify this method.
     */
    private void drawLine(Graphics2D gfx, double gridX1, double gridY1, 
                                          double gridX2, double gridY2)
    {
        gfx.setColor(Color.RED);
        
        // Recalculate the starting coordinate to be one unit closer to the destination, so that it
        // doesn't overlap with any image appearing in the starting grid cell.
        final double radius = 0.5;
        double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
        double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
        double clippedGridY1 = gridY1 + Math.sin(angle) * radius;
        
        gfx.drawLine((int) ((clippedGridX1 + 0.5) * gridSquareSize), 
                     (int) ((clippedGridY1 + 0.5) * gridSquareSize), 
                     (int) ((gridX2 + 0.5) * gridSquareSize), 
                     (int) ((gridY2 + 0.5) * gridSquareSize));
    }
}
