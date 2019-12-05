package game;

public class game
{
    game()
    {
        is_going = true;
    }

    public static void set_status(boolean curr_status)
    {
        is_going = curr_status;
    }

    static boolean is_going;
}