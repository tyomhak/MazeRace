package com.base;
import Graphics.DrawEngine;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        int windowWidth = 768;
        int windowHeight = 768;
        int[][] dungeon = new int[40][40];

        for(int i = 0; i < dungeon.length; ++i) {
            for (int j = 0; j < dungeon[0].length; ++j) {
                dungeon[i][j] = (i + j) % 2;
            }
        }

        DrawEngine temp = new DrawEngine(dungeon, windowWidth, windowHeight);

        JFrame obj = new JFrame();
        obj.setSize(windowWidth + 15, windowHeight + 40);
//        obj.setBounds(0, 0, windowWidth, windowHeight);
        obj.setBackground(Color.GRAY);
        obj.setResizable(true);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(temp);

//        while(true)
//        {
//            for(int i = 0; i < 9; ++i)
//            {
//                for(int j = 0; j < 9; ++j)
//                {
//                    System.out.println("nothing");
//                }
//            }
//            obj.paintComponents(obj.getGraphics());
////            obj.update(obj.getGraphics());
//
//        }
    }
}
