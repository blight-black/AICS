package game;

import java.util.ArrayList;

import config.Config;
import network.Receptor;
import game.map.Field;
import game.map.MapGenerator;

public class GameFactory
{
	private Receptor receptor;

	private int playersCount;

	private int hqPerBot;
	private int mapWidth;
	private int mapHeight;

	private int gameSeed;
	private int mapFlatness;

	public GameFactory(int mapWidth, int mapHeight, int hqPerBot, int playersCount, int gameSeed, int flatness)
	{
		this.hqPerBot = hqPerBot;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;

		this.gameSeed = gameSeed;
		this.mapFlatness = flatness;

		receptor = new Receptor(Config.Game.serverPort);

		this.playersCount = playersCount;
	}

	private void runOneGame(ArrayList<Bot> bots, Field map)
	{
//		return Game.runGame(playersCount, map, gameSeed, bots);
		new Thread(new GameHandler(playersCount, map, gameSeed, bots)).start();
	}

	public void startGames(int gamesCount)
	{
		for(int gameNum = 0 ; gameNum < gamesCount ; gameNum++)
		{
			ArrayList<Bot> bots = new ArrayList<Bot>();

			Field map = new Field(mapWidth, mapHeight);
			MapGenerator mapGen = new MapGenerator(map, gameSeed);

			mapGen.generateRandomMap(mapFlatness);

			System.out.println("Waiting for clients...");
			for(int i = 0 ; i < playersCount ; i++)
			{
				bots.add(new Bot(map, i, receptor.acceptClient()));
				System.out.println(String.format("\tBot #%d connected.", i));
			}
			
			mapGen.placeHQs(playersCount, hqPerBot);

			runOneGame(bots, map);
		}
		
		receptor.close();
	}
}
