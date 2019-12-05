package game.game_additional;

import game.game;
import additional.Location;
import game.player.Player;

public class Exit extends Item 
{

    Exit(Location loc)
    {
        Weight = 0;
        current_loc = loc; 
    }
    
    void use(Player player)
    {
        // game over Player Has Reached the End
        game.set_status(false);
    }


}