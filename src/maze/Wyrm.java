package maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import additional.Cell_Status;
import additional.Location;

public class Wyrm
{
    public Wyrm(Board curr_board )
    {
        maze = curr_board.get_maze();
        paths_stack = new Stack<Path>();
    }

    static Location get_rand_loc(ArrayList<Location> possible_loc)
    {
        Random rand = new Random();
        //return possible_loc.get(0);
        paths_stack = new Stack<Path>();
        return possible_loc.get(rand.nextInt(possible_loc.size())); 
    }

    static ArrayList<Location> get_accessible_Locations(Location curr_loc)
    {
        ArrayList<Location> actions = new ArrayList<Location>();
        
        int curr_row = curr_loc.get_row();
        int curr_col = curr_loc.get_column();
        Location new_loc = new Location(-1,-1);

        // UP
        if(curr_row - 1 >= 0 && maze[curr_row - 1][curr_col].status == Cell_Status.NOTHING )
        {
            new_loc.set_row(curr_row - 1);
            new_loc.set_column(curr_col);
            actions.add(new_loc);
        }

        new_loc = new Location(-1,-1);

        // LEFT
        if(curr_col - 1 >= 0 && maze[curr_row][curr_col - 1].status == Cell_Status.NOTHING )
        {
            new_loc.set_row(curr_row);
            new_loc.set_column(curr_col - 1);
            actions.add(new_loc);
        }
        new_loc = new Location(-1,-1);
        
        
        // DOWN
        if(curr_row + 1 < maze.length && maze[curr_row + 1][curr_col].status == Cell_Status.NOTHING )
        {
            new_loc.set_row(curr_row + 1);
            new_loc.set_column(curr_col);
            actions.add(new_loc);
        }
        new_loc = new Location(-1,-1);

        // RIGHT
        if(curr_col + 1 < maze[curr_row].length && maze[curr_row][curr_col + 1].status == Cell_Status.NOTHING )
        {
            new_loc.set_row(curr_row);
            new_loc.set_column(curr_col + 1);
            actions.add(new_loc);
        }
        new_loc = new Location(-1,-1);

        return actions;
    }

    // checks to see if there are any -1 left on the board so that we can fill them up with paths and walls
    Location check()
    {
        Location current_loc = new Location(-1,-1);
        for(int i = 0; i < maze.length; i = i + 1)
        {
            for(int j = 0; j< maze[i].length; j = j + 1)
            {
                if(maze[i][j].status.value == -1)
                {
                    current_loc.set_row(i);
                    current_loc.set_column(j);
                    return current_loc;
                }
            }
        }

        if(current_loc.get_column() == -1 && current_loc.get_row() == -1)
            return null;

        return current_loc;
    }

    // changes surrounding -1 into 0, means changes nothing into walls
    static void wallify(Location curr_loc)
    {
        int curr_row = curr_loc.get_row();
        int curr_col = curr_loc.get_column();

        // UP
        if(curr_row - 1 >= 0 && maze[curr_row - 1][curr_col].status == Cell_Status.NOTHING )
            maze[curr_row - 1][curr_col].status = Cell_Status.WALL;

        // LEFT
        if(curr_col - 1 >= 0 && maze[curr_row][curr_col - 1].status == Cell_Status.NOTHING )
            maze[curr_row][curr_col -1].status = Cell_Status.WALL;
        
        // DOWN
        if(curr_row + 1 < maze.length && maze[curr_row + 1][curr_col].status == Cell_Status.NOTHING )
            maze[curr_row + 1][curr_col].status = Cell_Status.WALL;

        // RIGHT
        if(curr_col + 1 < maze[curr_row].length && maze[curr_row][curr_col + 1].status == Cell_Status.NOTHING )
            maze[curr_row][curr_col + 1].status = Cell_Status.WALL;
    }
    
    // loop through the matrix and fill everything up
    public void carve()
    {
        int id = 0;
        while(check() != null)
        {
            Location curr_loc = check();
            Path tempPath = new Path(id, maze, curr_loc);
            paths_stack.push(tempPath);
            id++;
//            while(curr_loc != null)
//            {
//                maze[curr_loc.get_row()][curr_loc.get_column()].visited = true;
//                maze[curr_loc.get_row()][curr_loc.get_column()].status = Cell_Status.PATH;
//                Location curr_next_loc = getLocation(curr_loc);
//                if(curr_next_loc == null)
//                {
//                    curr_loc = null;
//                    continue;
//                }
//                maze[curr_next_loc.get_row()][curr_next_loc.get_column()].visited = true;
//                maze[curr_next_loc.get_row()][curr_next_loc.get_column()].status = Cell_Status.PATH;
//                Location curr_next_next_loc = getLocation(curr_next_loc);
//                if(curr_next_next_loc == null)
//                {
//                    curr_loc = null;
//                    continue;
//                }
//                maze[curr_next_next_loc.get_row()][curr_next_next_loc.get_column()].visited = true;
//                maze[curr_next_next_loc.get_row()][curr_next_next_loc.get_column()].status = Cell_Status.PATH;
//
//                wallify(curr_loc);
//                wallify(curr_next_loc);
//                curr_loc = curr_next_next_loc;
//            }
        }    
    }

    // check to see if the room collides with other rooms
    boolean collision_check(Location curr_loc, Room curr_room)
    {   
        int row = curr_loc.get_row();
        int column = curr_loc.get_column();

        int height = curr_room.height;
        int width = curr_room.width;

        for(int i = row; i < row + height; i = i + 1)
        {
            for(int j = column; j < column + width; j = j + 1 )
            {
                if(maze[i][j].status == Cell_Status.ROOM || maze[i][j].status == Cell_Status.WALL )
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void create_rooms(int attempts)
    {   
        for(int i = 0; i < attempts; i = i + 1)
        {
            Room new_room = new Room(maze);
            int room_height = new_room.height;
            int room_width = new_room.width;

            int max_height = maze.length - room_height;
            int max_width = maze[0].length - room_width;
            int row = new_room.get_random(1, max_height - 1);
            int col = new_room.get_random(1, max_width - 1);
            Location curr_room_loc = new Location(row, col);
            if( collision_check(curr_room_loc, new_room))
            {
                continue;
            }
            else
            {
                // looping everything and setting ROOMS
                for( i = row; i < row + room_height; i++)
                {
                    for(int j = col; j < col + room_width; j++)
                    {
                        maze[i][j].status = Cell_Status.ROOM;
                        maze[i][j].visited = true;
                    }
                }

                // looping through the boundaries to wallify them
                // Left wall and Right Wall
                for( i = row - 1; i < row + room_height + 1; i++)
                {
                    if( i > maze.length || i < 0)
                        continue;
                    Location left_wall = new Location(i, col);
                    Location right_wall = new Location(i, col + room_width - 1);
                    if(col - 1 > -1)
                        maze[i][col - 1].status = Cell_Status.WALL;
                    maze[i][col + room_width].status = Cell_Status.WALL;
                }

                // Upper wall and Bottom Wall
                for( i = col -1; i < col + room_width + 1; i++)
                {
                    if(i > maze[0].length || i < 0)
                        continue;
                    Location top_wall = new Location(row, i);
                    Location bot_wall = new Location(row + room_height -1, i);

                    if(row - 1 > -1)
                        maze[row - 1][i].status = Cell_Status.WALL;
                    maze[row + room_height][i].status = Cell_Status.WALL;
                }
            }
        }
    }

    public void create_maze(int num)
    {
        create_rooms(num);
        carve();
    }

    static Location getLocation(Location curr_loc)
    {
        ArrayList<Location> temp_list = new ArrayList<Location>();
        temp_list = get_accessible_Locations(curr_loc);
        if(temp_list.size() == 0)
            return null;
        return get_rand_loc(temp_list);
    }
    

    // variables <3
    static Stack<Path> paths_stack;
    static Cell[][] maze;
}

