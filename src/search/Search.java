package search;
import java.util.Map;


public interface Search
{
    Map<State, Action> findStrategy(State intialState, TerminalTest test);
    int get_states_generated();     
}