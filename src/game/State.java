package game;

import game.Action;

import java.util.Set;

import additional.Location;

public interface State
{
//    Player getPlayer();
    Set<Action> getApplicableActions(Location location);
    State getActionResult(Action action);
}