package game;

import java.util.Arrays;
import java.util.List;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.FancyGroundFactory;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.MoveActorAction;
import edu.monash.fit2099.engine.World;

/**
 * The main class for the zombie apocalypse game.
 *
 */
public class Application {

	public static void main(String[] args) {
		World2 world = new World2(new Display());

		FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Fence(), new Tree());
		
		List<String> map = Arrays.asList(
		"................................................................................",
		"................................................................................",
		"....................................##########..................................",
		"..........................###########........#####..............................",
		"............++...........##......................########.......................",
		"..............++++.......#..............................##......................",
		".............+++...+++...#...............................#......................",
		".........................##..............................##.....................",
		"..........................#...............................#.....................",
		".........................##...............................##....................",
		".........................#...............................##.....................",
		".........................###..............................##....................",
		"...........................####......................######.....................",
		"..............................#########.........######..........................",
		"............+++.......................#.........#...............................",
		".............+++++....................#.........#...............................",
		"...............++........................................+++++..................",
		".............+++....................................++++++++....................",
		"............+++.......................................+++.......................",
		"................................................................................",
		".........................................................................++.....",
		"........................................................................++.++...",
		"#.#......................................................................++++...",
		"#.#.......................................................................++....",
		"###.............................................................................");
		
		// town is 24 x 79
		List<String> map2 = Arrays.asList(
		".........................#...........#........................................##",
		".........................#...........#........................................#.",
		".........................#...........#........................................#.",
		".................#.......#####...#####........................................#.",
		"................#.#.................................##########................#.",
		"................#.#...............................###........###..............#.",
		"................#.#.......#........#..............#............#...........####.",
		"......###########.###....#.#......#.#.............#............#..........#.....",
		"......#.............#...#.+.#....#.+.#............###........###.........#......",
		"......#.............#....#.#......#.#...............#........#..........#.......",
		"......#.............#.....#...#....#................####..####.........###...###",
		"......###....########........#.#................................................",
		".........................#...###...#............................................",
		"........................#.#.......#.#...........................................",
		"###########..............#.#.....#.#............................................",
		"..........#...............#.#####.#..................#######.....###############",
		"..........#................#.....#...................#.....#.....#.............#",
		"............................#####....................#.....#######.............#",
		"################.....................................#.....#.....#########.....#",
		"#....#...............................................#.....#.....#.............#",
		"#....#...............................................#.....#.....#.............#",
		"#..............#..............####.####..............#.........................#",
		"######.........#..............#.......#..............#######......##############",
		"#..............#..............#.......#.........................................",
		"################..............#.......#.........................................");
		GameMap gameMap = new GameMap(groundFactory, map );
		GameMap town = new GameMap(groundFactory, map2 );
		world.addGameMap(gameMap);
		world.addGameMap(town);
		
		Actor player = new Player("Player", '@', 100);
		world.addPlayer(player, gameMap.at(42, 15));
		gameMap.at(41,15).addItem(new Shotgun());
		gameMap.at(43,15).addItem(new Sniper());
		gameMap.at(41,14).addItem(new Ammobox(10,RangeCapability.SPREAD));
		gameMap.at(43,14).addItem(new Ammobox(10,RangeCapability.SNIPE));
		
	    // Place some random humans
		String[] humans = {"Carlton", "May", "Vicente", "Andrea", "Wendy",
				"Elina", "Winter", "Clem", "Jacob", "Jaquelyn"};
		
		String[] farmers = {"Leeroy Jenkins","Bob Nelson","Dave Franco"};
		
		String[] zombies = {"Zombozo","Zombae","Zombita","Zomboto"};
		
		int x, y;
		for (String name : humans) {
			do {
				x = (int) Math.floor(Math.random() * 20.0 + 30.0);
				y = (int) Math.floor(Math.random() * 7.0 + 5.0);
			} 
			while (gameMap.at(x, y).containsAnActor());
			gameMap.at(x,  y).addActor(new Human(name));	
		}
		
		for (String name : zombies) {
			do {
				x = (int) Math.floor(Math.random() * 20.0 + 30.0);
				y = (int) Math.floor(Math.random() * 7.0 + 5.0);
			} 
			while (gameMap.at(x, y).containsAnActor());
			gameMap.at(x,  y).addActor(new Zombie(name));	
		}

		for (String name : farmers) {
			do {
				x = (int) Math.floor(Math.random() * 20.0 + 30.0);
				y = (int) Math.floor(Math.random() * 7.0 + 15.0);
			} 
			while (gameMap.at(x, y).containsAnActor());
			gameMap.at(x,  y).addActor(new Farmer(name));	
		}
		
		// place a simple weapon
		gameMap.at(74, 20).addItem(new Plank());
		gameMap.at(49, 17).addItem(new Plank());
		
		// place a vehicle in gameMap 1
		Vehicle vehicle = new Vehicle("Vehicle",'V',false);
		vehicle.addAction(new MoveActorAction(town.at(34, 24), "to town"));
		gameMap.at(1,23).addItem(vehicle);
		
		// place a vehicle in town
		Vehicle vehicle2 = new Vehicle("Vehicle",'V',false);
		vehicle2.addAction(new MoveActorAction(gameMap.at(1,23), "to camp"));
		town.at(34, 24).addItem(vehicle2);
		
		town.at(68, 16).addItem(new Plank());
		town.at(2, 20).addItem(new Plank());
		
		town.at(33, 24).addItem(new Food("Apple",'f',10));
		town.at(35, 24).addItem(new Food("Orange",'f',10));
		town.at(32, 24).addItem(new Food("Pear",'f',10));
		town.at(36, 24).addItem(new Food("Banana",'f',10));
		
		town.at(3, 15).addActor(new Farmer("Dale"));
		town.at(3, 17).addActor(new Farmer("Mace"));
		
		town.at(30, 1).addActor(new Farmer("Luke"));
		town.at(32, 2).addActor(new Farmer("Yoda"));
		
		town.at(75, 20).addActor(new Human("George"));
		town.at(73, 17).addActor(new Human("Floyd"));
		town.at(74, 19).addActor(new Human("Mayweather"));
		
		town.at(55, 6).addActor(new Human("Johnny"));
		town.at(58, 7).addActor(new Human("Sonya"));
		
		// FIXME: Add more zombies!
//		gameMap.at(30, 20).addActor(new Zombie("Groan"));
//		gameMap.at(30,  18).addActor(new Zombie("Boo"));
//		gameMap.at(10,  4).addActor(new Zombie("Uuuurgh"));
//		gameMap.at(50, 18).addActor(new Zombie("Mortalis"));
//		gameMap.at(1, 10).addActor(new Zombie("Gaaaah"));
//		gameMap.at(62, 12).addActor(new Zombie("Aaargh"));
		town.at(20, 20).addActor(new Zombie("Groan"));
		town.at(7,  22).addActor(new Zombie("Boo"));
		town.at(47,  15).addActor(new Zombie("Uuuurgh"));
		town.at(50, 18).addActor(new Zombie("Mortalis"));
		town.at(10, 8).addActor(new Zombie("Gaaaah"));
		town.at(75, 8).addActor(new Zombie("Aaargh"));
//		town.at(30,24).addActor(new MamboMarie(500));
		
		world.run();
	}
}
