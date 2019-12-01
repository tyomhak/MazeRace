package maze;

import additional.Cell_Status;
import additional.Location;

import java.util.Stack;

public class Path
{
    Path(int id, Cell[][] m, Location loc )
    {
        ID = id;
        maze = m;
        initial_location = loc;
        dead_end = new Stack<Location>();
        length = 0;
    }

    void create_path()
    {
        Location curr_loc = initial_location;
        while(curr_loc != null)
        {
            maze[curr_loc.get_row()][curr_loc.get_column()].visited = true;
            maze[curr_loc.get_row()][curr_loc.get_column()].status = Cell_Status.PATH;
            length++;
            Location curr_next_loc = Wyrm.getLocation(curr_loc);
            if(curr_next_loc == null)
            {
                curr_loc = null;
                continue;
            }
            maze[curr_next_loc.get_row()][curr_next_loc.get_column()].visited = true;
            maze[curr_next_loc.get_row()][curr_next_loc.get_column()].status = Cell_Status.PATH;
            Location curr_next_next_loc = Wyrm.getLocation(curr_next_loc);
            length++;
            if(curr_next_next_loc == null)
            {
                curr_loc = null;
                continue;
            }
            maze[curr_next_next_loc.get_row()][curr_next_next_loc.get_column()].visited = true;
            maze[curr_next_next_loc.get_row()][curr_next_next_loc.get_column()].status = Cell_Status.PATH;

            Wyrm.wallify(curr_loc);
            Wyrm.wallify(curr_next_loc);
            curr_loc = curr_next_next_loc;

            if(is_dead_end(curr_next_loc))
            {
                dead_end.push(curr_next_loc);
            }

        }
    }

    boolean is_dead_end(Location curr_location)
    {
        int counter = 0;

        int curr_row = curr_location.get_row();
        int curr_col = curr_location.get_column();

        // UP
        if(curr_row - 1 >= 0 && maze[curr_row - 1][curr_col].status == Cell_Status.WALL )
            counter++;

        // LEFT
        if(curr_col - 1 >= 0 && maze[curr_row][curr_col - 1].status == Cell_Status.WALL )
            counter++;

        // DOWN
        if(curr_row + 1 < maze.length && maze[curr_row + 1][curr_col].status == Cell_Status.WALL )
            counter++;

        // RIGHT
        if(curr_col + 1 < maze[curr_row].length && maze[curr_row][curr_col + 1].status == Cell_Status.WALL )
            counter++;

        if(counter > 2)
            return true;

        return false;
    }

    Location get_dead_end()
    {
        return dead_end.pop();
    }

    void add_dead_end(Location location)
    {
        dead_end.push(location);
    }

    Location initial_location;
    Cell[][] maze;
    int length;
    double ID; // for keeping track of the path and later on merging
    Stack<Location> dead_end;
}
