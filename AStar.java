package astar;
import java.util.PriorityQueue;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class AStar extends JFrame implements Runnable{
    private static final int[][] maze = 
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
    
     BufferedImage image;
     int x,pathX,pathY;
     boolean flag=false,dest=false;
     Timer timer; 
      Thread runner;
    
    public final List<Integer> path = new ArrayList<Integer>();
    public static final int STEP_COST=1;
    private Cell[][] grid;
    private PriorityQueue<Cell> openCells;
    private boolean[][] closedCells;

    private int startI,startJ;
    
    private int endI,endJ;
    public int BallPath;
    
    public AStar(int width,int height,int si,int sj,int ei,int ej,int[][] maze){
        this.x = path.size()-1;
        
        x=0;
        setTitle("Mazer Blazer");
        setSize(1200, 900);
        getContentPane().setBackground(Color.white);
        setIconImage(new ImageIcon("C:\\Users\\Dell\\Desktop\\s.png").getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        grid=new Cell[width][height];
        closedCells=new boolean[width][height];
        openCells=new PriorityQueue<Cell> ((Cell c1,Cell c2) ->{
            return c1.finalCost<c2.finalCost? -1: c1.finalCost>c2.finalCost? 1:0;
        });
        
        startI=si;
        startJ=sj;
        endI=ei;
        endJ=ej;
                
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[i].length;j++){
                grid[i][j]=new Cell(i,j);
                grid[i][j].heuristicCost=Math.abs(i - endI) + Math.abs(j - endJ);
                grid[i][j].solution=false;
            }
        }
        
        grid[startI][startJ].finalCost=0;
       
       for(int i=0;i<width;i++){
           for(int j=0;j<height;j++){
               if(maze[i][j]==0)
           
           grid[i][j]=null;             
           }
        }
           
    }
        
    
    public void updateCost(Cell current, Cell t,int cost){
        if(t==null || closedCells[t.i][t.j])
            return;
        int tFinalCost= t.heuristicCost +cost;
        boolean isOpen= openCells.contains(t);
        
        if(!isOpen){
            t.finalCost=tFinalCost;
            t.parent=current;
            
            if(!isOpen){
                openCells.add(t);
            }
        }
    }
    
    public void process(){
        openCells.add(grid[startI][startJ]);
        Cell current;
        
        while(true){
            current=openCells.poll();
            
            if(current == null){
                break;
            }
            closedCells[current.i][current.j]=true;
            
            if(current.equals(grid[endI][endJ]))
                return;
            
            Cell t;
            if(current.i - 1>=0){
                t=grid[current.i -1][current.j];
                updateCost(current,t,current.finalCost + STEP_COST);
                              
            }
            
            if(current.j -1 >=0){
                t=grid[current.i][current.j - 1];
                updateCost(current ,t,current.finalCost+ STEP_COST);
            }
            
            if(current.j+1 < grid[0].length){
                t=grid[current.i][current.j +1];
                updateCost(current,t,current.finalCost + STEP_COST);
            }
            
            if(current.i +1<grid.length){
                t=grid[current.i+1][current.j];
                updateCost(current,t,current.finalCost+STEP_COST);

            }
        }
            
    }  
    public void display(){
        System.out.println("Grid:");
        
        for(int i=0;i<grid.length ;i++){
            for(int j=0;j<grid[i].length ;j++){
                if(i==startI && j==startJ)
                    System.out.print("S "); 
                else if(i==endI && j==endJ)
                    System.out.print("D ");
                else if(grid[i][j]!=null)
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
            System.out.println();
        }
        System.out.println("");
    }
    
    public void displayScores(){
        System.out.println("\n Scores for Cells:");
        
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[i].length;j++){
                if(grid[i][j]!=null){
                    System.out.print("  "+grid[i][j].finalCost);
                }
                    
                else
                    System.out.print(" 000 ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public void displaySolution(){
        int pathCost=0;
        Color c;
        Graphics g;
        if(closedCells[endI][endJ]){
            System.out.print("Path:");
            Cell current=grid[endI][endJ];
          
            path.add(current.i);
            path.add(current.j);
            grid[current.i][current.j].solution=true;
            
            while(current.parent !=null){
                pathCost+=1;
               
                path.add(current.parent.i);
                path.add(current.parent.j);
                
                grid[current.parent.i][current.parent.j].solution=true;
                current=current.parent;
            }
            int x=path.size()-1;
            while(x>0){
                System.out.print("["+path.get(x)+","+path.get(x-1)+"] ->  ");
                x=x-2;
            }
            System.out.println(" \nNo.of Blocks Visited: "+pathCost+"\n");
            BallPath=path.size()-2;
            
            System.out.println();
        }else{
            System.out.println("No possible path!");
        }
    }
    public void run(){
       System.out.println("x"+x);
       while(x>=0){
            flag=true;
            pathX=path.get(x);
            pathY=path.get(x-1);
            repaint();
            x=x-2;
        try {
            Thread.sleep(1000);
            }
        catch (InterruptedException e) {
        }
       }
        JOptionPane.showMessageDialog(null, "You Reached the Destination!","Yay!!",1);
       flag=false;
    }
    
      @Override
     public void paint(Graphics g) {
        super.paint(g);
           
        Toolkit tkit=Toolkit.getDefaultToolkit();
        Image image_path=tkit.getImage("C:\\Users\\Dell\\Desktop\\r.png");
        g.translate(60, 60);
        
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 13 ; col++) {
                Color color;
                if(row==startI && col==startJ)
                    color=Color.GREEN;
                else if(row==endI && col==endJ)
                    color=Color.RED;
                else if(maze[row][col]==1)
                    color=Color.WHITE;
                else
                    color=Color.BLACK;
                
                g.setColor(color);
                g.fillRect(80 * col, 80 * row, 80, 80);
              
                g.setColor(Color.BLACK);
                g.drawRect(80 * col, 80 * row, 80, 80);
            }
        }
        int y=0;
        while(y<path.size()-1){
            int pathX = path.get(y);
            int pathY = path.get(y + 1);
            if(y==0){
                g.setColor(Color.GREEN);
                g.fillRect(80 * pathY,80 * pathX, 80, 80);
            }
            else if(y== path.size()-2){
                g.setColor(Color.RED);
                g.fillRect(80 * pathY,80 * pathX, 80, 80);
            }
            else{
            g.setColor(Color.YELLOW);
            g.fillRect(80 * pathY,80 * pathX, 80, 80);
            }
            g.setColor(Color.BLUE);
            g.drawRect(80 * pathY, 80 * pathX, 80, 80);
              y=y+2;
            
        }
       
          
            if(flag==true){
            g.setColor(Color.BLACK);
            g.drawRect(80 * pathY, 80 * pathX, 80, 80);
           g.drawImage(image_path, pathX*80,pathY*80, this);
            }
            else{
                x=path.size()-1;
                runner=new Thread(this);
                runner.start();
            }
            
         } 
 }
