package com.montyhall;

import static com.montyhall.Main.r;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

public class Game implements Callable<Boolean> {
	private final Player player;
	private Host host = new Host();

	Game(Player player) {
		this.player = player;
	}

	@Override
	public Boolean call() {
		boolean[] doors = DoorsGenerator.generateDoors();
		//System.out.println(Arrays.toString(doors));

		int chosenDoor = player.chooseDoor();
		//System.out.println("chosenDoor: " + chosenDoor);

		int openedDoor = host.openDoor(doors, chosenDoor);
		//System.out.println("openedDoor: " + openedDoor);

		//if (chosenDoor == openedDoor) System.out.println("Problem");
		//if (doors[openedDoor]) System.out.println("Problem 2");

		int finalChoice = player.applyStrategy(chosenDoor, openedDoor);
		//System.out.println("finalChoice: " + finalChoice);

		return doors[finalChoice];
	}

	static class Host {
		int openDoor(boolean[] doors, int chosenDoor) {
			return IntStream.range(0, doors.length).filter(i -> !doors[i] && i != chosenDoor).findAny().getAsInt();
		}
	}

	static abstract class Player {
		final MontyHallStrategy strategy;

		Player(MontyHallStrategy strategy) {
			this.strategy = strategy;
		}

		int chooseDoor() {
			return r.nextInt(3);
		}

		int applyStrategy(int chosenDoor, int openedDoor) {
			return strategy.makeChoice(chosenDoor, openedDoor);
		}
	}

	static class BasicPlayer extends Player {
		BasicPlayer() {
			super(new SaveSelectionStrategy());
		}
	}

	static class WisePlayer extends Player {
		WisePlayer() {
			super(new ChangeSelectionStrategy());
		}
	}

	interface MontyHallStrategy {
		int makeChoice(Integer chosenDoor, Integer openedDoor);
	}

	static class SaveSelectionStrategy implements MontyHallStrategy {
		@Override
		public int makeChoice(Integer chosenDoor, Integer openedDoor) {
			return chosenDoor;
		}
	}

	static class ChangeSelectionStrategy implements MontyHallStrategy {
		@Override
		public int makeChoice(Integer chosenDoor, Integer openedDoor) {
			List<Integer> availableDoors = new ArrayList<>(List.of(0, 1, 2));
			availableDoors.remove(chosenDoor);
			availableDoors.remove(openedDoor);
			return availableDoors.get(0);
		}
	}
}
