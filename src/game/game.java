package game;

import additional.Location;
import game.game_additional.Exit;
import game.game_additional.Item;
import game.player.Escaper;
import game.player.Player;
import maze.Board;
import maze.Wyrm;
import search.GoalTest;
import search.GraphSearch;
import search.Node;
import search.SearchUtils.PathGoalTest;
import search.SearchUtils.PathState;
import search.State;
import search.frontier.BreadthFirstFrontier;
import search.frontier.Frontier;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashSet;

public class game
{
    public game()
    {
        is_going = true;

        start_game();
    }

    public static void set_status(boolean curr_status)
    {
        is_going = curr_status;
    }

    public void start_game()
    {
        Board maze = new Board(40, 40);

        JFrame obj = new JFrame();
        obj.setSize(768 + 50, 768 + 50);
//        obj.setBounds(0, 0, windowWidth, windowHeight);
        obj.setBackground(Color.GRAY);
        obj.setResizable(true);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(maze);

        Wyrm modified_maze = new Wyrm(maze);
        modified_maze.create_maze(100);
        //maze.print();
        maze.update();

        Player player1 = new Escaper(modified_maze.getRandRoomCell(), maze);
        maze.addPlayer(player1);

        Item exit = new Exit(modified_maze.getRandRoomCell(), maze);
        maze.addItem(exit);

        Frontier ourFrontier = new BreadthFirstFrontier();
        GraphSearch graphSearch = new GraphSearch(ourFrontier);


        Node solution = graphSearch.findStrategy(player1.get_state(), new PathGoalTest(exit.current_loc));

        Node temp = solution.parent;
        while(temp != null)
        {

            ((PathState)(temp.state)).get_current_Location().printLoc();
            temp = temp.parent;
        }





        maze.update();
    }

    static boolean is_going;
}