package maze;

import additional.*;
//import game.*;

public class Cell
{
    public Cell( Item obj )
    {
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

    public Cell_Status status;
    public Boolean visited;
    private Item object;
}

