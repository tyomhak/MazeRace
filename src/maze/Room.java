package maze;

import java.util.Random;

//import additional.Location;

public class Room
{
    public Room(Cell[][] maze)
    {
        int maze_width = maze[0].length;  
        int maze_height = maze.length;   
        int scale = 3;

        int max_height = maze_height / scale;   // setting the max limit for the height
        int max_width = maze_width   / scale;     // setting the max limit for the width

        // randomly selecting a number in between min and maximum width/height
        width = get_random(1, max_height );
        height = get_random(1, max_width );

    }

    public int get_random( int min, int max ) 
    {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

    int width;
    int height;
}