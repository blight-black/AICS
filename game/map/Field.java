package game.map;

import java.awt.Point;

import game.agent.Building;
import game.agent.Unit;

/**
 * represents the game field
 * 
 * @author farzad
 * 
 */
public class Field
{
	private Tile[][]	gameField;

	private int			width;
	private int			height;

	/**
	 * constructs a field using the given dimensions
	 * 
	 * @param width
	 * @param height
	 */
	public Field(int width, int height)
	{
		this.width = width;
		this.height = height;

		gameField = new Tile[width][height];

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				gameField[i][j] = new Tile();

	}

	/**
	 * determines if the specified cell is out of game field's bounds
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isOutOfField(int x, int y)
	{
		return !(((x >= 0) && (x < width)) && ((y >= 0) && (y < height)));
	}

	/**
	 * determines if the cell with the given coordinates is walkable. note that
	 * it will only consider natural obstacles, and not other units blocking the
	 * way.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isWalkable(int x, int y)
	{
		if (isOutOfField(x, y))
			return false;

		return gameField[x][y].isWalkable();
	}

	/**
	 * sets the type of the specified tile
	 * 
	 * @param x
	 * @param y
	 * @param type
	 */
	public void setTileType(int x, int y, TileType type)
	{
		gameField[x][y].setType(type);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return the type of the specified tile. null will be returned if the
	 *         specified coordinates does not exist in game
	 */
	public TileType getTileType(int x, int y)
	{
		if (isOutOfField(x, y))
			return null;

		return gameField[x][y].getType();
	}

	/**
	 * clears the field of supplies. WARNING: never use this, or you might
	 * corrupt your information on supplies locations
	 * 
	 */
	public void clearAllSupplies()
	{
		for (Tile[] col : gameField)
			for (Tile cell : col)
				cell.removeSupplies();
	}

	/**
	 * puts a supplies pack in the target tile. WARNING: never use this, or you
	 * might corrupt your information on supplies locations
	 * 
	 * @param x
	 * @param y
	 */
	public void putSuppliesAt(int x, int y)
	{
		gameField[x][y].putSupplies();
	}

	/**
	 * checks to see if the target tile has supplies or not.
	 * 
	 * @param x
	 * @param y
	 * @return true if there is any supplies pack in the target tile, false if
	 *         there is not
	 */
	public boolean isSuppliesAt(int x, int y)
	{
		return gameField[x][y].hasSupplies();
	}
	
	public Tile getTileAt(int x, int y)
	{
		if(isOutOfField(x, y))
			return null;
		
		return gameField[x][y];
	}
	
	public Tile getTileAt(Point pos)
	{
		return getTileAt(pos.x, pos.y);
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public String toString()
	{
		String tempString = "";
		
		for(int i = 0 ; i < height ; i++)
		{
			for(int j = 0 ; j < width ; j++)
			{
				tempString += gameField[j][i].toString();
			}
			
			tempString += "\n";
		}
		
		return tempString;
	}
	
	public Field clone()
	{
		Field tempField = new Field(width, height);

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				tempField.gameField[i][j] = this.gameField[i][j].clone();
		
		return tempField;
	}

	/**
	 * represents the Tiles in the Field
	 * 
	 * @author farzad
	 * 
	 */
	public class Tile
	{
		private TileType	type;
		private boolean		hasSupplies;
		
		private Unit		unit;
		private Building	building;

		/**
		 * creates a new tile with the default type TileType.ROCK
		 */
		public Tile()
		{
			this.type = TileType.ROCK;
			this.hasSupplies = false;
			
			this.unit = null;
			this.building = null;
		}
		
		public Tile clone()
		{
			Tile tempTile = new Tile();
			
			tempTile.type = this.type;
			tempTile.hasSupplies = this.hasSupplies;
			tempTile.unit = this.unit;
			tempTile.building = this.building;
			
			return tempTile;
		}

		/**
		 * determines if there's a supplies pack here
		 * 
		 * @return
		 */
		public boolean hasSupplies()
		{
			return this.hasSupplies;
		}

		/**
		 * puts a pack of supplies on this tile
		 */
		public void putSupplies()
		{
			this.hasSupplies = true;
		}

		/**
		 * removes the supplies from this tile
		 */
		public void removeSupplies()
		{
			this.hasSupplies = false;
		}

		/**
		 * changes the tile type
		 * 
		 * @param type
		 */
		public void setType(TileType type)
		{
			this.type = type;
		}

		/**
		 * 
		 * @return the type of this tile
		 */
		public TileType getType()
		{
			return this.type;
		}

		/**
		 * 
		 * @return true if the Tile is walkable, and false if it is not
		 */
		public boolean isWalkable()
		{
			return type.isWalkable;
		}
		
		public Unit getUnit()
		{
			return unit;
		}
		
		public void setUnit(Unit unit)
		{
			this.unit = unit;
		}
		
		public Building getBuilding()
		{
			return this.building;
		}
		
		public void setBuilding(Building building)
		{
			this.building = building;
		}
		
		public String toString()
		{
			if(hasSupplies)
				return "O";
			
			return type.toString();
		}

	}

	/**
	 * represents the different TileTypes used in the Field. the order of values
	 * matters, as the server uses this order
	 * 
	 * @author farzad
	 * 
	 */
	public enum TileType
	{
		ROCK(true), LAVA(false);

		public final boolean	isWalkable;

		TileType(boolean isWalkable)
		{
			this.isWalkable = isWalkable;
		}
		
		public String toString()
		{
			switch(this)
			{
				case LAVA:
					return "#";
				case ROCK:
					return " ";
				default:
					return null;				
			}
		}
	}
}
