package game;

import java.util.Set;

public interface State
{
//    Player getPlayer();
    Set<Action> getApplicableActions(int row, int col);
    State getActionResult(Action action);
}

