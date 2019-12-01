package game;

import additional.Location;

import java.util.Set;

public interface State
{
//    Player getPlayer();
    Set<Action> getApplicableActions(Location location);
    State getActionResult(Action action);
}

