package astar;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class View extends JFrame {

     private int [][] maze = 
        { {0,0,0,0,0,0,0,0,0,0,0,0,0},
          {0,1,0,1,0,1,0,1,1,1,1,1,0},
          {0,1,0,1,1,1,0,1,0,0,0,1,0},
          {0,1,1,1,0,0,0,1,1,1,1,1,0},
          {0,1,0,1,1,1,1,1,0,0,0,1,0},
          {0,1,0,1,0,0,0,1,0,1,1,1,0},
          {0,1,0,1,0,1,1,1,0,0,0,1,0},
          {0,1,0,1,0,0,0,1,0,1,0,1,0},
          {0,1,1,1,1,1,1,1,1,1,0,1,0},
          {0,0,0,0,0,0,0,0,0,0,0,0,0}
        };
    
    private final List<Integer> path = new ArrayList<Integer>();
    
    public View() {
        setTitle("Mazer Blazer");
        setSize(640, 480);
        setIconImage(new ImageIcon("C:\\Users\\Dell\\Desktop\\s.png").getImage());
        getContentPane().setBackground(Color.white);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.translate(50, 50);
        
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 13; col++) {
                Color color;
                switch (maze[row][col]) {
                  
                    case 1 : color = Color.WHITE; break;
                    case 2:  color = Color.RED; break;                 
                    case 9 : color = Color.GREEN; break;
                    default : color = Color.BLACK;
                }
                g.setColor(color);
                g.fillRect(30 * col, 30 * row, 30, 30);
                g.setColor(Color.BLACK);
                g.drawRect(30 * col, 30 * row, 30, 30);
            }
          
        }
                
       int output;
       output = JOptionPane.showConfirmDialog(null, "Press 'Yes' to Start!" ,"Maze",JOptionPane.YES_NO_OPTION);
         
      if(output == JOptionPane.YES_OPTION){
               SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
                AStar astar= new AStar (10,13,1,1,8,11,maze);                    
                astar.display();
                astar.process();
                astar.displayScores();
                astar.displaySolution();
               
            }
        });
            } 
    }
       
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                View view = new View();
                view.setVisible(true);
            }
        });
    }
    
}