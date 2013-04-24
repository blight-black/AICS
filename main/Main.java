package main;

import io.command.Command;
import io.command.Command.Attack;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import visualizer.GraphicalOutput;
import config.Config;

import game.Bot;
import game.GameFactory;
import game.agent.Building;
import game.agent.Unit;
import game.agent.Unit.UnitType;
import game.map.Field;
import game.map.Field.TileType;

public class Main
{

	public static void printHelpMessage()
	{
		System.out.println("use the following arguments to run a game, or simply run the game without ant arguments to use the default settings:");
		System.out.println("-port\t\t[portNumber]\t\tsets the servers port number");
		System.out.println("-seed\t\t[seed]\t\t\tsets the game's seed(number)");
		System.out.println("-flat\t\t[flatness]\t\tsets the flatness of generated maps");
		System.out.println("-width\t\t[width]\t\t\tsets the mapSize.width");
		System.out.println("-height\t\t[height]\t\tsets the mapSize.height");
		System.out.println("-players\t[playersCount]\t\tsets the number of players in the game");
		System.out.println("-hqs\t\t[hqsCount]\t\tsets the number of HQs per players");
		System.out.println("-delay\t\t[delay(ms)]\t\tsets the amount of time between cycles");
		System.out.println("-cycle\t\t[max cycles]\t\tsets the cycle in which the game is force-finished");
	}

	private static enum ArgType
	{
		PORT, HELP, SEED, FLATNESS, MAP_WIDTH, MAP_HEIGHT, PLAYERS, HQS, DELAY, CYCLE, INVALID;
	}

	public static void main(String[] args)
	{
//		Field map = new Field(40, 40);
//		map.putSuppliesAt(34, 16);
//		map.setTileType(35, 17, TileType.LAVA);
//		
//		new Unit.Melee(map, 5, 7, null).setTeamNumber(0);
//		new Unit.Ranged(map, 14, 7, null).setTeamNumber(0);
//		new Building.HeadQuarters(map, 25, 7, null).setTeamNumber(0);
//		
//		new Unit.Melee(map, 25, 25, null).setTeamNumber(4);
//		Building b = new Building.HeadQuarters(map, 25, 25, null);
//		b.setTeamNumber(4);
//		b.setCommand(new Command.Spawn(new Point(25, 25), UnitType.MELEE));
//		Building b2 = new Building.HeadQuarters(map, 10, 20, null);
//		b2.setTeamNumber(4);
//		b2.damageHealth(1200);
//		b.setTeamNumber(4);
//		new Unit.Melee(map, 9, 21, null).setTeamNumber(1).setCommand(new Command.Attack(new Point(9, 21), b2.getPosition()).setTarget(b2));
//		new Unit.Ranged(map, 7, 19, null).setTeamNumber(1).setCommand(new Command.Attack(new Point(8, 19), b2.getPosition()).setTarget(b2));
//		
//		new Unit.Melee(map, 5, 35, null).setTeamNumber(1);
//		new Unit.Melee(map, 7, 35, null).setTeamNumber(1);
//		new Unit.Ranged(map, 6, 35, null).setTeamNumber(1);
//		new Unit.Ranged(map, 9, 35, null).setTeamNumber(1);
//		new Unit.Ranged(map, 10, 35, null).setTeamNumber(1);
//		
//		new Unit.Melee(map, 5, 32, null).setTeamNumber(0);
//		Unit unit = new Unit.Ranged(map, 9, 32, null);
//		unit.setTeamNumber(0);
//		Command cmd = new Command.Attack(unit.getPosition(), new Point(9, 35));
//		cmd.setTarget(new Unit.Ranged(map, 8, 35, null).setTeamNumber(1));
//		unit.setCommand(cmd);
//		
//		GraphicalOutput go = new GraphicalOutput(40, 40);
//		
//		
//		go.update(map, new ArrayList<Bot>(), 0);
//		
//		try
//		{
//			System.in.read();
//		}
//		catch(IOException e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		System.exit(0);
		int playersCount = 2;
		int mapWidth = 60;
		int mapHeight = 60;
		int gameSeed = 10000;
		int flatness = 8;
		int hqs = 1;

		try
		{
			ArgType type = ArgType.INVALID;
			for(String arg : args)
			{			
				switch(arg)
				{
					case "-help":
					case "-h":
					case "/?":
						type = ArgType.HELP;
						printHelpMessage();
						System.exit(0);
						break;

					case "-port":
						type = ArgType.PORT;
						break;

					case "-players":
						type = ArgType.PLAYERS;
						break;

					case "-hqs":
						type = ArgType.HQS;
						break;

					case "-width":
						type = ArgType.MAP_WIDTH;
						break;

					case "-height":
						type = ArgType.MAP_HEIGHT;
						break;
					case "-seed":
						type = ArgType.SEED;
						break;
						
					case "-flat":
						type = ArgType.FLATNESS;
						break;
						
					case "-delay":
						type = ArgType.DELAY;
						break;
						
					case "-cycle":
						type = ArgType.CYCLE;
						break;

					default:
						switch(type)
						{
							case HQS:
								hqs = Integer.parseInt(arg);
								break;
							case PLAYERS:
								playersCount = Integer.parseInt(arg);
								break;
							case PORT:
								Config.Game.serverPort = Integer.parseInt(arg);
								break;
							case MAP_HEIGHT:
								mapHeight = Integer.parseInt(arg);
								break;
							case MAP_WIDTH:
								mapWidth = Integer.parseInt(arg);
								break;
							case SEED:
								gameSeed = Integer.parseInt(arg);
								break;
							case FLATNESS:
								flatness = Integer.parseInt(arg);
								break;
							case DELAY:
								Config.Game.cycleTimeLimit = Integer.parseInt(arg);
								break;
							case CYCLE:
								Config.Game.cycleCountLimit = Integer.parseInt(arg);
								break;
							case INVALID:
								printHelpMessage();
								System.exit(1);
								break;
							default:
								break;
						}

						type = ArgType.INVALID;
				}
			}
		}
		catch(Exception e)
		{
			printHelpMessage();
			System.exit(2);
		}
		
		System.out.println("AUTAIC Server Version 2.1 - Use commandline argument \"-h\" for more info");
		System.out.println("Games' seed is set to " + gameSeed);
		System.out.println("There will be " + playersCount + " bots per game, with each one having " + hqs + " HQ(s)");
		System.out.println(String.format("(%d,  %d) maps will be generated using %dx wall removals", mapWidth, mapHeight, flatness));
		System.out.println(String.format("Games will end after reaching cycle #%d. Thinking time between cycles is %dms.", Config.Game.cycleCountLimit, Config.Game.cycleTimeLimit));
		System.out.println(String.format("Starting the server on port %d...", Config.Game.serverPort));
		
		Config.Game.startingSupplies = hqs * Config.Game.suppliesPerPack;
		
		try
		{
			new GameFactory(mapWidth, mapHeight, hqs, playersCount, gameSeed, flatness).startGames(1);
		}
		catch(NullPointerException e)
		{
			System.err.println("The server has encountered a fatal error... Please check if port #" + Config.Game.serverPort + " is open on your system, and try again.");
		}
	}
}
