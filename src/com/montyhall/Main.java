package com.montyhall;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Main {

	static Random r = new Random();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
		Game.Player basicPlayer = new Game.BasicPlayer();
		Game.Player wisePlayer = new Game.WisePlayer();

		List<Game> gamesWithBasicPlayer = IntStream.range(0, 10000).mapToObj(i -> new Game(basicPlayer)).collect(toList());
		List<Game> gamesWithWisePlayer = IntStream.range(0, 10000).mapToObj(i -> new Game(wisePlayer)).collect(toList());

		ExecutorService basicPlayerExecutor = Executors.newFixedThreadPool(8);
		List<Future<Boolean>> basicPlayerResults = basicPlayerExecutor.invokeAll(gamesWithBasicPlayer);
		basicPlayerExecutor.shutdown();

		int winCount = 0;
		int looseCount = 0;

		for (Future<Boolean> result : basicPlayerResults) {
			if (result.get()) winCount ++;
			else looseCount ++;
		}

		System.out.println("Basic wins: " + winCount);
		System.out.println("Basic loses: " + looseCount);

		/*-------------------------------------------------------------------------------------------------------------*/

		ExecutorService wisePlayerExecutor = Executors.newFixedThreadPool(8);
		List<Future<Boolean>> wisePlayerResults = wisePlayerExecutor.invokeAll(gamesWithWisePlayer);
		wisePlayerExecutor.shutdown();

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
