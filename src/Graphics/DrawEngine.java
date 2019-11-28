package Graphics;

import javax.swing.*;
import java.awt.*;

public class DrawEngine extends JPanel {
    private int [][] dungeon;
    int winWidth;
    int winHeight;
    int cellSize;


    public DrawEngine(int[][] dungeon, int wWidth, int wHeight) {
        this.dungeon = dungeon;
        this.winHeight = wHeight;
        this.winWidth = wWidth;

        // CHECK THIS //
        this.cellSize = Math.min(wWidth / dungeon.length, wHeight/ dungeon[0].length); //(Math.min(wHeight, wWidth)) / Math.max(dungeon.length, dungeon[0].length);
    }

    public void drawDungeon(Graphics g)
    {
        drawDungeon(this.dungeon, g);
    }

    public void drawDungeon(int[][] dDungeon, Graphics g)
    {
        int col = 0;
        g.setColor(Color.WHITE);
        for(int i = 0; i < dDungeon.length; ++i)
        {
            for(int j = 0; j < dDungeon[0].length; ++j)
            {
                if(dDungeon[i][j] == 0)
                {
                    if (col != 0)
                    {
                        g.setColor(Color.ORANGE);
                        col = 0;
                    }
                }

                else if(dDungeon[i][j] == 1)
                {
                    if(col != 1)
                    {
                        g.setColor(Color.DARK_GRAY);
                        col = 1;
                    }
                }
                g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }

        }

        Toolkit.getDefaultToolkit().sync();
    }

    public void paintComponent(Graphics g) {
        //draw border for playingfield
//        g.setColor(Color.white);
//        g.drawRect(0, 0, winWidth, winHeight);
//
//        //draw background for the playingfield
//        g.setColor(Color.LIGHT_GRAY);
//        g.fillRect(0, 0, winWidth, winHeight);

        drawDungeon(g);
    }
}