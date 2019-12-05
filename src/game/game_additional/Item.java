package game.game_additional;

import additional.Location;
import game.player.*;
import maze.Board;

import java.awt.*;


public class Item
{
    Location current_loc;   // where the item is

    // for later adding items
    Integer Damage;
    Integer Health;
    Integer Weight;

    // for drawing
    Color myColor;

    void use(Player player) { };

    public void draw(Graphics g, Board board)
    {
        g.setColor(myColor);
        g.fillRect(current_loc.get_column() * board.cellSize, current_loc.get_row() * board.cellSize,  board.cellSize, board.cellSize);
    }
}