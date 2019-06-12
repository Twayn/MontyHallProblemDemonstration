package com.montyhall;

import static com.montyhall.Main.r;

import java.util.concurrent.Callable;

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

	static class Host {
		int openDoor(boolean[] doors, int chosenDoor) {
			for (int i = 0; i < doors.length; i++) {
				if (doors[i] == false && chosenDoor != i) return i;
			}
			System.out.println("Problem 3");
			throw new RuntimeException();
		}
	}

	interface MontyHallStrategy {
		int makeChoice(int chosenDoor, int openedDoor);
	}

	static class SaveSelectionStrategy implements MontyHallStrategy {
		@Override
		public int makeChoice(int chosenDoor, int openedDoor) {
			return chosenDoor;
		}
	}

	static class ChangeSelectionStrategy implements MontyHallStrategy {
		@Override
		public int makeChoice(int chosenDoor, int openedDoor) {
			int[] choices = {0, 1, 2};
			for (int i = 0; i < choices.length; i++) {
				if (choices[i] != chosenDoor && choices[i] != openedDoor) return i;
			}
			System.out.println("Problem 4");
			throw new RuntimeException();
		}
	}
}
