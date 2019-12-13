package search.SearchUtils;

import additional.Location;
import additional.Utils;
import search.GoalTest;
import search.Node;
import search.NodeFunction;

public class StraightLineHueristic implements NodeFunction
{
    private Location destination;

    public StraightLineHueristic(Location destination)
    {
        this.destination = destination;
    }

    @Override
    public int get_node_value(Node node)
    {
        return Utils.distance(((PathState)node.state).get_current_Location(), destination);
    }

}
