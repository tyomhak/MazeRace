package search;

import search.Action;

import java.util.Set;


public interface State
{
//    Player getPlayer();
    Set<Action> getApplicableActions();
    State getActionResult(Action action);
}