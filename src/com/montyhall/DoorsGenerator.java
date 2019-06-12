package com.montyhall;

import static com.montyhall.Main.r;

class DoorsGenerator {
	/**
	 * Creates 3 doors behind one of which is a prize.
	 * @return Size 3 array with only one true value evenly distributed
	 */
	static synchronized boolean[] generateDoors(){
		boolean[] result = new boolean[3];
		result[r.nextInt(3)] = true;
		return result;
	}
}
