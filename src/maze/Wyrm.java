package maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import additional.Cell_Status;
import additional.Location;

public class Wyrm
{
    // variables <3
    static Stack<Path> paths_stack;
    static Cell[][] maze;
    Board board;



    public Wyrm(Board curr_board )
    {
        board = curr_board;
        maze = curr_board.get_maze();
        paths_stack = new Stack<Path>();
    }
    
    public void create_maze(int num)
    {
        create_rooms(num);
        carve();
        cleanup(1);
    }


    /* Utility Functions */

    static Location getLocation(Location curr_loc)
    {
        ArrayList<Location> temp_list = get_desired_cells(curr_loc, Cell_Status.NOTHING, null);
        if( temp_list == null)
            return null;
        return get_rand_loc(temp_list);
    }

    static ArrayList<Location> get_desired_cells( Location curr_loc, Cell_Status cell_desired1, Cell_Status cell_desired2)
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

    static Location get_rand_loc(ArrayList<Location> possible_loc)
    {
        Random rand = new Random();
        //return possible_loc.get(0);
        return possible_loc.get(rand.nextInt(possible_loc.size())); 
    }

    Path get_path()
    {
        if(paths_stack.size() == 0)
            return null;
        return paths_stack.pop();
    }

    void add_path(Path p)
    {
        paths_stack.add(p);
    }

    // changes surrounding -1 into 0, means changes nothing into walls
    static void wallify(Location curr_loc)
    {
        ArrayList<Location> nothing_cells = get_desired_cells(curr_loc, Cell_Status.NOTHING, null);

        if(nothing_cells == null)
            return;

        for(int i = 0; i < nothing_cells.size(); i++)
        {
            maze[nothing_cells.get(i).get_row()][nothing_cells.get(i).get_column()].status = Cell_Status.WALL;
        }
    }

    /* Utility Functions end here */



    /* Functions Needed For Create_Rooms */

    public void create_rooms(int attempts)
    {   
        for(int i = 0; i < attempts; i = i + 1)
        {
            Room new_room = new Room(maze);
            int room_height = new_room.height;
            int room_width = new_room.width;

            int max_height = maze.length - room_height;
            int max_width = maze[0].length - room_width;
            int row = new_room.get_random(2, max_height - 1);
            int col = new_room.get_random(2, max_width - 1);
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

    /* Functions Needed For Creating_Rooms end here */



    /* Functions Needed For Carving */

    // loop through the matrix and fill everything up
    public void carve()
    {
        // creating paths at locations where we find NOTHING(-1)
        int id = 0;
        while(check() != null)
        {
            Location curr_loc = check();
            Path tempPath = new Path(id, board, curr_loc);
            tempPath.create_path();
            add_path(tempPath);
            id++;
        }    
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

    /* Functions Needed For Carving end here */
    

    /* Functions Needed For Cleanup */

    // for every dead end it deletes some, and if it can connect paths it will.
    public void cleanup(int limit)       // on aisle 3!
    {
        Path temp_path = get_path();

        while(temp_path != null)
        {
            Location dead_end = temp_path.get_dead_end();
            int counter = 0;
            while(dead_end != null /*&&  counter < limit */)
            {
                if(!temp_path.is_dead_end(dead_end))
                {
                    dead_end = temp_path.get_dead_end();
                    continue;
                }

                if(loopy_thingy(dead_end))
                {
                    counter++;
                }
                dead_end = temp_path.get_dead_end();
            }
            if(temp_path.dead_end.size() != 0)
                paths_stack.add(temp_path); 

            temp_path = get_path();
                      
        }
    } 

    // finds the closest path from nearest walls, checks to see if the path chosen is the same path or not
    ArrayList<Location> find_closest_path(Location curr_loc, Path from_which_path)
    {
        ArrayList<Location> close_path = get_desired_cells(curr_loc, Cell_Status.PATH, null);
        
        if(close_path == null)
            return null;

        for(int i = 0; i < close_path.size(); ++i)
        {
            if(maze[close_path.get(i).get_row()][close_path.get(i).get_column()].get_path() == from_which_path)
            {
                close_path.remove(i);
                i--;
            }
        }

        if(close_path.size() == 0)
            return null;

            return close_path;
    }


    // merges paths if there are walls who have different paths near them else puts a -1 
    boolean loopy_thingy(Location curr_loc )
    {
        ArrayList<Location> wall_locations = get_desired_cells(curr_loc, Cell_Status.WALL, Cell_Status.NOTHING);

        int curr_col = curr_loc.get_column();
        int curr_row = curr_loc.get_row();

        Path curr_path = maze[curr_row][curr_col].get_path();


        Location temp_loc = wall_locations.get(0);
        ArrayList<Location> all_paths = find_closest_path(temp_loc, curr_path);


        Cell wall_cell = maze[temp_loc.get_row()][temp_loc.get_column()];

        if(all_paths == null)
        {
            ArrayList<Location> new_dead_ends = get_desired_cells(curr_loc, Cell_Status.PATH, null);
            
            if(!(new_dead_ends == null))
            {
                Location new_dead_end = new_dead_ends.get(0);
                maze[curr_row][curr_col].get_path().add_dead_end(new_dead_end);
            }

            maze[curr_row][curr_col].status = Cell_Status.NOTHING;
            maze[curr_row][curr_col].set_path(null);
            board.update();
            return false;
        }

        // looping through every possibility to see if there is a one where the wall has only 1 path nearby 
        for (int i = 1; i < wall_locations.size(); i++)
        {
            ArrayList<Location> temp_all_paths = find_closest_path(wall_locations.get(i), curr_path);
            if(temp_all_paths == null)
                continue;
            if(all_paths.size() > temp_all_paths.size())
            {
                temp_loc = wall_locations.get(i);
                all_paths = temp_all_paths;
            }
        }   
        
        if(all_paths == null)
        {
            ArrayList<Location> new_dead_ends = get_desired_cells(curr_loc, Cell_Status.PATH, null);
            
            if(!(new_dead_ends == null))
            {
                Location new_dead_end = new_dead_ends.get(0);
                maze[curr_row][curr_col].get_path().add_dead_end(new_dead_end);
            }

            maze[curr_row][curr_col].status = Cell_Status.NOTHING;
            maze[curr_row][curr_col].set_path(null);
            board.update();
            return false;
        }
            
        Location new_path = all_paths.get(0);


        wall_cell = maze[temp_loc.get_row()][temp_loc.get_column()];
        Cell new_path_cell = maze[new_path.get_row()][new_path.get_column()];

        wall_cell.status = Cell_Status.PATH;
        wall_cell.set_path(maze[curr_row][curr_col].get_path());
        wall_cell.get_path().path_cells.add(temp_loc);

        board.update();

        if(wall_cell.get_path().path_cells.size() > new_path_cell.get_path().path_cells.size() )
        {
            wall_cell.get_path().merge(new_path_cell.get_path());
        }
        else
        {
            new_path_cell.get_path().merge(wall_cell.get_path());
        }

            return true;
    }

    /* Functions Needed For Cleaning_up end here */

}

