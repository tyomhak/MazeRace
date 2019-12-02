package maze;

import additional.*;
//import game.*;

public class Cell
{
    public Cell( Item obj )
    {
        belongs_to_path = null;
        status = Cell_Status.NOTHING;
        visited = false;
        set_item(obj);
    }

    public Item get_item()
    {
        return object;
    }

    public void set_item(Item obj)
    {
        if(status.value == -1 || status.value == 0 )
            return;
        object = obj;
    }

    public void set_path(Path path)
    {
        belongs_to_path = path;
    }

    public Path get_path()
    {
        return belongs_to_path;
    }

    private Path belongs_to_path;
    public Cell_Status status;
    public Boolean visited;
    private Item object;
}

