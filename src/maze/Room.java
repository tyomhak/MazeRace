package maze;

import additional.Cell_Status;
import additional.Location;

import java.util.ArrayList;
import java.util.Random;

//import additional.Location;

public class Room
{
    ArrayList<Location> room_cells;
    double ID;
    int width;
    int height;
    private Path belongs_to_path;
    Board board;


    public Room(Board b)
    {
        board = b;
        room_cells = new ArrayList<Location>();
        Cell[][] maze = board.get_maze();

        belongs_to_path = null;
        int maze_width = maze[0].length;
        int maze_height = maze.length;
        int scale = 3;

        int max_height = maze_height / scale;   // setting the max limit for the height
        int max_width = maze_width   / scale;     // setting the max limit for the width

        // randomly selecting a number in between min and maximum width/height
        width = get_random(3, max_height );
        height = get_random(3, max_width );

    }

    public int get_random( int min, int max ) 
    {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

    public void update_cell_room()
    {
        /* Update RoomCells.belongs_to_room */
        if(room_cells.size() > 0) {
            Cell[][] tempMaze = board.get_maze();
            for (int i = 0; i < room_cells.size(); ++i) {
                Location temp = room_cells.get(i);
                tempMaze[temp.get_row()][temp.get_column()].set_room(this);
            }
        }
    }

    public void set_path(Path path)
    {
        belongs_to_path = path;

        /* Update RoomCells.belongs_to_path */
        if(room_cells.size() > 0) {
            Cell[][] tempMaze = board.get_maze();
            for (int i = 0; i < room_cells.size(); ++i) {
                Location temp = room_cells.get(i);
                tempMaze[temp.get_row()][temp.get_column()].set_path(belongs_to_path);
                tempMaze[temp.get_row()][temp.get_column()].status = Cell_Status.PATH;

            }
        }
    }

    public Path get_path(){return belongs_to_path;}
}