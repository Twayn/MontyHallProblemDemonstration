package com.montyhall;

import static com.montyhall.Game.BasicPlayer;
import static com.montyhall.Game.WisePlayer;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
	final static Random r = new Random();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
		var basicPlayer = new BasicPlayer();
		List<Game> basicPlayerGames = range(0, 10000).mapToObj(i -> new Game(basicPlayer)).collect(toList());

		var executor = newFixedThreadPool(8);
		var basicPlayerResults = executor.invokeAll(basicPlayerGames);
		executor.shutdown();

		int winCount = 0;
		int looseCount = 0;

		for (Future<Boolean> result : basicPlayerResults) {
			if (result.get()) winCount ++;
			else looseCount ++;
		}

		System.out.println("Basic wins: " + winCount);
		System.out.println("Basic loses: " + looseCount);

		/*-------------------------------------------------------------------------------------------------------------*/
		var wisePlayer = new WisePlayer();
		List<Game> wisePlayerGames = range(0, 10000).mapToObj(i -> new Game(wisePlayer)).collect(toList());

		executor = newFixedThreadPool(8);
		var wisePlayerResults = executor.invokeAll(wisePlayerGames);
		executor.shutdown();

		winCount = 0;
		looseCount = 0;

		for (Future<Boolean> result : wisePlayerResults) {
			if (result.get()) winCount ++;
			else looseCount ++;
		}

		System.out.println("Wise wins: " + winCount);
		System.out.println("Wise loses: " + looseCount);
    }
}
