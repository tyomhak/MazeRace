package maze;

//import java.util.LinkedHashSet;
import java.awt.*;
import java.util.Set;

//import additional.*;
import additional.Cell_Status;
import game.*;
import game.Action;

import javax.swing.*;

public class Board extends JPanel implements State
{
    public Board(int width, int height)
    {
        maze = new Cell[width][height];
        mazeWidth = width;
        mazeHeight = height;

        cellSize = Math.min(winWidth / maze.length, winHeight/ maze[0].length);

        // initializing the board with null and nothings
        for(int i = 0; i < maze.length; i = i + 1)
        {
            for(int j = 0; j< maze[i].length; j = j + 1)
            {
                maze[i][j] = new Cell(null);
            }
        }
    }

    public Set<Action> getApplicableActions(int row, int col) 
    {
        //Set<Action> actions = new LinkedHashSet<Action>();
        // to be implemented . . . 
        return null;
    }

    public State getActionResult(Action action) 
    {
        // to be implemented . . .
        return null;
    }

    public void print()
    {
        for(int i = 0; i < maze.length; i = i + 1)
        {
            for(int j = 0; j< maze[i].length; j = j + 1)
            {
                System.out.print(maze[i][j].status.value + "  ");
            }
            System.out.println();
        }
    }

    public void drawMaze(Graphics g)
    {
        int col = 0;
        g.setColor(Color.ORANGE);
        for(int i = 0; i < maze.length; ++i)
        {
            for(int j = 0; j < maze[0].length; ++j)
            {
                if(maze[i][j].status == Cell_Status.PATH)
                {
                    g.setColor(Color.white);
                }
                else if(maze[i][j].status == Cell_Status.WALL)
                {
                    g.setColor(Color.GRAY);
                }
                else if(maze[i][j].status == Cell_Status.ROOM)
                {
                    g.setColor(Color.pink);
                }
                else
                {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }

        }

        Toolkit.getDefaultToolkit().sync();
    }

    public void paintComponent(Graphics g) {
//        g.setColor(Color.white);
//        g.drawRect(0, 0, winWidth, winHeight);
//
//        g.setColor(Color.LIGHT_GRAY);
//        g.fillRect(0, 0, winWidth, winHeight);

        drawMaze(g);
    }

    public Cell[][] get_maze()
    {
        return maze;
    }
        
    
    Cell[][] maze;

    int mazeWidth;
    int mazeHeight;

    int winWidth = 768;
    int winHeight = 768;
    int cellSize;

}