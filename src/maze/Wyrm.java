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
        // creating paths at locations where we find NOTHING(-1)
        int id = 0;
        while(check() != null)
        {
            Location curr_loc = check();
            Path tempPath = new Path(id, maze, curr_loc);
            tempPath.create_path();
            paths_stack.add(tempPath);
            id++;
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
                    if(col - 1 > -1)
                        maze[i][col - 1].status = Cell_Status.WALL;
                    maze[i][col + room_width].status = Cell_Status.WALL;
                }

                // Upper wall and Bottom Wall
                for( i = col -1; i < col + room_width + 1; i++)
                {
                    if(i > maze[0].length || i < 0)
                        continue;

                    if(row - 1 > -1)
                        maze[row - 1][i].status = Cell_Status.WALL;
                    maze[row + room_height][i].status = Cell_Status.WALL;
                }
            }
        }
    }

    boolean find_another_path(int curr_row, int curr_col, Path curr_path)
    {
        boolean check = false;
        // UP
        if(curr_row - 1 >= 0 && maze[curr_row - 1][curr_col].status == Cell_Status.PATH)
        {
            if(!(maze[curr_row - 1][curr_col].get_path() == curr_path))
            {
                curr_path.merge(maze[curr_row - 1][curr_col].get_path());
                check = true;
            }
        }

        // LEFT
        if(curr_col - 1 >= 0 && maze[curr_row][curr_col - 1].status == Cell_Status.PATH )
        {
            if(!(maze[curr_row][curr_col - 1].get_path() == curr_path))
            {
                curr_path.merge(maze[curr_row][curr_col - 1].get_path());
                check = true;
            }
        }
        // DOWN
        if(curr_row + 1 < maze.length && maze[curr_row + 1][curr_col].status == Cell_Status.PATH )
        {
            if(!(maze[curr_row + 1][curr_col].get_path() == curr_path))
            {
                curr_path.merge(maze[curr_row + 1][curr_col].get_path());
                check = true;
            }
        }
        // RIGHT
        if(!(curr_col + 1 < maze[curr_row].length && maze[curr_row][curr_col + 1].status == Cell_Status.PATH ))
        {
            if(maze[curr_row][curr_col + 1].get_path() == curr_path)
            { 
                curr_path.merge(maze[curr_row][curr_col + 1].get_path());
                check = true;
            }
        }
        return check;       
    }

    // merges paths if there are walls who have different paths near them else puts a -1 
    void loopy_thingy(Location curr_loc )
    {
        ArrayList<Location> wall_locations = get_desired_cells(curr_loc, Cell_Status.WALL, null);


        int curr_row = curr_loc.get_row();
        int curr_col = curr_loc.get_column();

        if(wall_locations == null || wall_locations.size() == 0)
        {
            Location new_dead_end = find_closest_path(curr_loc);
            if(!(new_dead_end == null))
                if(maze[curr_row][curr_col].get_path().is_dead_end(new_dead_end))
                    maze[new_dead_end.get_row()][new_dead_end.get_column()].get_path().dead_end.push(new_dead_end);

            maze[curr_row][curr_col].set_path(null);
            maze[curr_row][curr_col].status = Cell_Status.WALL;

            return;
        }


        for (int i = 0; i < wall_locations.size(); i++)
        {
            Location temp_loc = wall_locations.get(i);
            maze[temp_loc.get_row()][temp_loc.get_column()].status = Cell_Status.PATH;
            maze[temp_loc.get_row()][temp_loc.get_column()].set_path(maze[curr_row][curr_col].get_path());
            maze[temp_loc.get_row()][temp_loc.get_column()].get_path().path_cells.add(temp_loc);
        }

        // // UP
        // if(curr_row - 1 >= 0 && maze[curr_row - 1][curr_col].status == Cell_Status.WALL)
        // {
        //     if(find_another_path(curr_row - 1, curr_col, maze[curr_row][curr_col].get_path()))
        //     {
        //         check = true;
        //         maze[curr_row - 1][curr_col].status = Cell_Status.PATH;
        //         maze[curr_row - 1][curr_col].set_path(maze[curr_row][curr_col].get_path());
        //         maze[curr_row - 1][curr_col].get_path().path_cells.add(new Location(curr_row - 1,curr_col));
        //     }
        // }

        // // LEFT
        // if(curr_col - 1 >= 0 && maze[curr_row][curr_col - 1].status == Cell_Status.WALL )
        // {
        //     if(find_another_path(curr_row, curr_col - 1, maze[curr_row][curr_col].get_path()))
        //     {
        //         check = true;
        //         maze[curr_row][curr_col - 1].status = Cell_Status.PATH;
        //         maze[curr_row][curr_col - 1].set_path(maze[curr_row][curr_col].get_path());
        //         maze[curr_row][curr_col - 1].get_path().path_cells.add(new Location(curr_row,curr_col - 1));
        //     }
        // }
        
        
        // // DOWN
        // if(curr_row + 1 < maze.length && maze[curr_row + 1][curr_col].status == Cell_Status.WALL )
        // {
        //     if(find_another_path(curr_row + 1, curr_col, maze[curr_row][curr_col].get_path()))
        //     { 
        //         check = true;
        //         maze[curr_row + 1][curr_col].status = Cell_Status.PATH;
        //         maze[curr_row + 1][curr_col].set_path(maze[curr_row][curr_col].get_path());
        //         maze[curr_row + 1][curr_col].get_path().path_cells.add(new Location(curr_row + 1,curr_col));
        //     }
        // }


        // // RIGHT
        // if(curr_col + 1 <= maze[curr_row].length && maze[curr_row][curr_col + 1].status == Cell_Status.WALL )
        // {
        //     if(find_another_path(curr_row, curr_col + 1, maze[curr_row][curr_col].get_path()))
        //     {
        //         check = true;
        //         maze[curr_row][curr_col + 1].status = Cell_Status.PATH;
        //         maze[curr_row][curr_col + 1].set_path(maze[curr_row][curr_col].get_path());
        //         maze[curr_row][curr_col + 1].get_path().path_cells.add(new Location(curr_row,curr_col + 1));
        //     }
        // }


        // if(!check)
        // {
        //     maze[curr_row][curr_col].set_path(null);
        //     maze[curr_row][curr_col].status = Cell_Status.WALL;
        //     Location new_dead_end = find_closest_path(curr_loc);
        //     if((Path).is_dead_end(new_dead_end))
        //     if(!(new_dead_end == null))
        //         maze[new_dead_end.get_row()][new_dead_end.get_column()].get_path().dead_end.push(new_dead_end);
        // }
 
    }

    ArrayList<Location> get_desired_cells( Location curr_loc, Cell_Status cell_desired1, Cell_Status cell_desired2)
    {
        ArrayList<Location> list_of_cells = new ArrayList<Location>();
        int curr_row = curr_loc.get_row();
        int curr_col = curr_loc.get_column();

        if(cell_desired2 == null)
            cell_desired2 = cell_desired1;

        // UP
        if(curr_row - 1 >= 0)
            if(maze[curr_row - 1][curr_col].status == cell_desired1 || maze[curr_row - 1][curr_col].status == cell_desired2)
                list_of_cells.add(new Location(curr_row - 1, curr_col));

        // LEFT
        if(curr_col - 1 >= 0)
            if(maze[curr_row][curr_col - 1].status == cell_desired1 || maze[curr_row][curr_col - 1].status == cell_desired2)
                list_of_cells.add(new Location(curr_row, curr_col - 1));

        // DOWN
        if(curr_row + 1 < maze.length)
            if(maze[curr_row + 1][curr_col].status == cell_desired1 || maze[curr_row + 1][curr_col].status == cell_desired2) 
                list_of_cells.add(new Location(curr_row + 1, curr_col));

        // RIGHT
        if(curr_col + 1 < maze[curr_row].length)
            if(maze[curr_row][curr_col + 1].status == cell_desired1 || maze[curr_row ][curr_col + 1].status == cell_desired2)
                list_of_cells.add(new Location(curr_row, curr_col + 1));

        if(list_of_cells.size() == 0)
            return null;

        return list_of_cells;
    }

    Location find_closest_path(Location curr_loc)
    {
        ArrayList<Location> close_path = get_desired_cells(curr_loc, Cell_Status.PATH, null);

        if(close_path.size() == 0)
            return null;
        else return close_path.get(0);
    }

    // for every dead end it deletes some, and if it can connect paths it will.
    public void cleanup(int limit)       // on aisle 3!
    {
        //int block_to_be_deleted = num_to_del;
        
        while(paths_stack.size() > 1)
        {
            Path temp_path = paths_stack.pop();
            Location dead_end = temp_path.get_dead_end();
            int counter = 0;
            while(dead_end != null && counter < limit)
            {
                if(!temp_path.is_dead_end(dead_end))
                {
                    dead_end = temp_path.get_dead_end();
                    continue;
                }
                loopy_thingy(dead_end);
                counter++;
                dead_end = temp_path.get_dead_end();
            }           
        }
        System.out.println(paths_stack.size());
    } 

    public void create_maze(int num)
    {
        //create_rooms(num);
        carve();
        cleanup(1);
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

