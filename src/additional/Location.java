package additional;

import game.Action;

public class Location implements Action 
{
	private int row;
	private int col;

	public Location(int row, int col) 
	{
		this.row = row;
		this.col = col;
	}
	
	public void set_row(int num) 
	{
		row = num;
	}
	public void set_column(int num) 
	{
		col = num;
	}

	public int get_row()
	{
		return row;
	}
	public int get_column()
	{
		return col;
	}
}