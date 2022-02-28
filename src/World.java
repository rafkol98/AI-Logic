
/*
 * CS5011 A2 Starter code
 * This enum class holds the various boards to be played
 *
 * author Mun See Chang
 *
 * TEST: 6 boards
 * SMALL (S): 5x5, 10 boards
 * MEDIUM (M): 7x7, 10 boards
 * LARGE (L): 9x9, 10 boards
 *
 * Two hints are given at indexes [0][0] and [length/2][length/2]
 *

 */

public enum World {

	TEST1(new char[][] {{'0', 'b', 'b'}, {'b', '3', 'b'}, {'m', 'm', 'm'}}), //sps ok
	TEST2(new char[][] {{'2', 'm', 'm'}, {'m', '5', 'm'}, {'b', 'b', 'm'}}), //sps ok
	TEST3(new char[][] {{'0', '0', 'b'}, {'1', '1', '1'}, {'1', 'm', '1'}}), //sps not ok, SATS ok
	TEST4(new char[][] {{'0', '1', 'm'}, {'0', '2', '2'}, {'b', '1', 'm'}}), //sps not ok, SATS ok
	TEST5(new char[][] {{'0', '1', 'm'}, {'0', '1', '1'}, {'0', 'b', '0'}}), //sps not ok, SATS ok
	TEST6(new char[][] {{'0', 'b', '0'}, {'b', '2', '2'}, {'1', 'm', 'm'}}), //sps and SATS not ok


	SMALL1(new char[][] {{'0', '0', '1', 'b', 'b'}, {'0', 'b', '2', 'm', '2'}, {'0', '0', '2', 'm', '2'}, {'0', 'b', '2', '2', '2'}, {'0', 'b', '1', 'm', '1'}}),
	SMALL2(new char[][] {{'1', '1', '0', '0', '0'}, {'m', '1', 'b', '1', '1'}, {'b', '1', '0', '1', 'm'}, {'0', '0', 'b', '2', '2'}, {'0', '0', '0', '1', 'm'}}),
	SMALL3(new char[][] {{'0', 'b', 'm', '2', '0'}, {'1', '3', 'm', 'b', '0'}, {'1', 'm', '2', '1', '0'}, {'1', '1', '1', '0', 'b'}, {'0', '0', '0', '0', '0'}}),
	SMALL4(new char[][] {{'0', '0', '0', '1', 'm'}, {'0', '0', 'b', 'b', '2'}, {'2', '2', '1', '1', 'm'}, {'m', 'm', '2', '1', '1'}, {'m', 'm', '2', '0', 'b'}}),
	SMALL5(new char[][] {{'0', '1', 'b', '1', '0'}, {'0', '1', 'm', 'b', '0'}, {'0', '1', '1', 'b', '0'}, {'1', '1', 'b', '1', '1'}, {'1', 'm', '2', 'm', 'b'}}),
	SMALL6(new char[][] {{'0', '0', '0', '0', 'b'}, {'1', '1', '0', '0', '0'}, {'m', '2', '1', 'b', '0'}, {'4', 'm', '2', '0', 'b'}, {'m', 'm', '2', '0', '0'}}),
	SMALL7(new char[][] {{'1', 'm', '2', 'm', 'b'}, {'1', 'b', '2', '2', '2'}, {'0', 'b', '0', '1', 'm'}, {'1', '1', '0', 'b', '1'}, {'m', '1', '0', '0', '0'}}),
	SMALL8(new char[][] {{'0', '2', 'm', '3', 'm'}, {'0', 'b', 'm', '3', '1'}, {'0', 'b', '1', '1', 'b'}, {'1', '2', '3', 'b', '1'}, {'1', 'm', 'm', 'm', '1'}}),
	SMALL9(new char[][] {{'0', '0', 'b', '0', '0'}, {'0', '0', 'b', '0', '0'}, {'2', '2', '1', '0', '0'}, {'m', 'm', '2', '1', 'b'}, {'2', '2', '2', 'm', '1'}}),
	SMALL10(new char[][] {{'0', '0', '1', 'm', '1'}, {'0', '0', '1', '1', '1'}, {'0', '0', '0', 'b', '1'}, {'0', '0', '0', 'b', 'm'}, {'b', '0', 'b', 'b', 'm'}}),


	MEDIUM1(new char[][] {{'0', '0', '0', '0', '1', '1', '1'}, {'0', '0', '0', '0', '1', 'm', '2'}, {'1', '2', '2', '2', 'b', '3', 'm'}, {'1', 'm', 'm', '2', 'm', '2', 'b'}, {'2', '3', '3', 'b', '3', '3', '1'}, {'2', 'm', 'b', '2', 'm', 'm', '1'}, {'2', 'm', 'm', '2', '2', '2', '1'}}),
	MEDIUM2(new char[][] {{'0', '0', '0', '0', '0', 'b', '0'}, {'0', '0', '0', '0', '1', 'b', '1'}, {'0', 'b', '1', '1', '1', 'm', '2'}, {'1', '3', 'm', '2', '1', '2', 'm'}, {'m', '4', 'm', '4', 'b', '1', 'b'}, {'b', '5', 'm', 'm', 'b', '0', '0'}, {'m', '3', 'm', 'm', '2', '0', '0'}}),
	MEDIUM3(new char[][] {{'0', '1', '1', '1', '1', 'm', 'm'}, {'b', '1', 'm', '1', '1', '2', 'b'}, {'0', 'b', '2', 'b', '0', '1', 'b'}, {'0', '1', 'm', '1', '1', '2', 'm'}, {'0', '1', 'b', '2', '2', 'm', '3'}, {'0', '0', '1', 'm', '2', '2', 'm'}, {'b', 'b', '1', '1', '1', '1', '1'}}),
	MEDIUM4(new char[][] {{'0', '0', '0', '0', '0', '0', 'b'}, {'b', '0', '1', '1', '1', 'b', '0'}, {'b', '1', '1', 'm', '2', '1', '0'}, {'m', '3', '3', '4', 'm', '3', '1'}, {'3', 'm', 'm', '3', 'm', '4', 'm'}, {'m', '3', '2', '2', '1', '4', 'm'}, {'1', '1', '0', '0', 'b', '2', 'm'}}),
	MEDIUM5(new char[][] {{'0', 'b', 'b', '0', '2', 'm', '2'}, {'0', 'b', '0', '0', '2', 'm', '2'}, {'1', '1', '1', '1', '2', '2', '1'}, {'2', 'm', '1', '2', 'm', '3', '1'}, {'m', '2', '1', '2', 'm', 'b', 'm'}, {'1', '2', '1', '2', 'b', 'm', 'm'}, {'0', '1', 'm', '1', '1', '3', 'm'}}),
	MEDIUM6(new char[][] {{'0', '1', '1', '2', 'm', 'm', '1'}, {'0', '1', 'm', 'b', 'b', '4', 'b'}, {'0', '1', '2', 'm', '2', 'm', 'm'}, {'0', '0', '1', '1', '3', '3', 'b'}, {'0', 'b', '0', '0', '1', 'm', '2'}, {'0', '0', '1', '1', '2', '2', 'm'}, {'b', '0', '1', 'm', '1', '1', '1'}}),
	MEDIUM7(new char[][] {{'0', '0', '0', '1', 'm', '1', 'b'}, {'0', '0', '0', '2', '2', '2', '0'}, {'b', '2', '1', '1', 'm', '1', 'b'}, {'m', 'm', '1', '2', '2', '2', 'b'}, {'4', '4', '2', '1', 'm', '2', '1'}, {'m', 'm', '1', '1', '2', 'm', '1'}, {'2', 'b', '1', '0', '1', '1', 'b'}}),
	MEDIUM8(new char[][] {{'0', '0', '1', '1', '1', '1', 'm'}, {'0', '1', '3', 'm', '3', '2', '1'}, {'0', '1', 'm', 'm', 'm', '1', '0'}, {'0', '1', 'b', '3', '2', '1', '0'}, {'1', '1', '1', '1', 'b', '2', 'b'}, {'2', 'm', '3', 'b', 'm', '4', 'm'}, {'2', 'm', 'm', 'b', 'm', '4', 'm'}}),
	MEDIUM9(new char[][] {{'0', 'b', '0', '1', '1', '2', 'b'}, {'1', '1', '1', '2', 'm', 'b', 'm'}, {'m', 'b', '2', 'm', '2', '3', 'm'}, {'2', 'm', '2', '1', '1', '1', '1'}, {'b', '1', '1', '0', '1', 'b', '1'}, {'0', 'b', '1', '2', '2', 'm', '1'}, {'0', '1', 'm', '2', 'm', '2', '1'}}),
	MEDIUM10(new char[][] {{'1', '2', '3', 'm', '2', 'm', '1'}, {'2', 'm', 'm', 'b', '2', '2', '2'}, {'m', '4', '2', 'b', 'b', '1', 'm'}, {'m', '2', '0', '0', '0', 'b', '1'}, {'1', '1', 'b', '1', '1', '0', '0'}, {'1', '2', '3', 'm', '2', '2', '1'}, {'1', 'm', 'm', '3', 'm', '2', 'm'}}),


	LARGE1(new char[][] {{'0', '1', '1', '1', '1', 'm', '2', '1', 'b'}, {'0', '1', 'm', 'b', 'b', '2', 'm', '3', '2'}, {'0', '1', '1', '2', 'b', 'b', '2', 'm', 'm'}, {'b', '1', '1', '2', 'm', '2', 'b', '3', 'b'}, {'0', '2', 'm', '3', '2', 'm', '2', 'm', '1'}, {'0', '2', 'm', '3', '3', '3', '4', '2', 'b'}, {'1', '2', '3', 'm', '3', 'm', 'm', '2', '1'}, {'m', '2', '3', 'm', '4', '3', '3', '3', 'm'}, {'2', 'm', '2', '2', 'm', '1', '1', 'm', 'b'}}),
	LARGE2(new char[][] {{'0', '0', '1', 'm', 'b', '0', '1', '1', '1'}, {'0', '1', 'b', '3', '2', '1', '1', 'm', '1'}, {'0', '1', 'm', 'b', 'm', '2', '2', '2', '2'}, {'0', '1', '1', '3', 'm', '2', '1', 'm', 'b'}, {'1', '2', 'b', '3', '2', '2', 'b', '2', 'm'}, {'m', '3', 'm', '2', 'm', 'b', '1', '1', '1'}, {'m', '6', '3', '3', '2', 'm', '2', '1', '0'}, {'m', 'm', 'm', '1', '1', '2', 'm', '1', '0'}, {'2', '3', 'b', 'b', '0', '1', 'b', '1', '0'}}),
	LARGE3(new char[][] {{'0', '1', 'm', '1', '0', '2', 'm', '3', 'm'}, {'0', '1', '1', '2', '1', '3', 'm', '4', '2'}, {'b', '0', '0', '1', 'm', 'b', '2', 'm', '1'}, {'0', '0', '1', 'b', '2', '2', 'b', '2', '1'}, {'0', '0', 'b', 'm', '1', '1', 'm', '1', '0'}, {'2', '2', '3', '2', 'b', '2', '2', '1', '0'}, {'m', 'm', '3', 'm', '4', 'm', '2', '0', '0'}, {'3', '3', 'b', 'm', 'b', 'm', '2', '0', '0'}, {'1', 'm', '3', 'm', 'b', '1', 'b', '0', '0'}}),
	LARGE4(new char[][] {{'0', '1', '2', 'm', 'm', '4', 'm', 'm', '1'}, {'0', '1', 'm', '5', 'm', 'm', 'b', '2', '1'}, {'0', '2', '4', 'm', '5', 'm', '2', '1', '1'}, {'0', '1', 'm', 'm', '3', '1', '1', '1', 'm'}, {'1', '2', '2', '2', '2', '1', '1', '1', '1'}, {'m', '1', '1', '1', '2', 'm', '1', '0', '0'}, {'1', '2', '2', 'm', '2', '1', '1', '0', '0'}, {'0', '1', 'm', '3', 'b', '1', '0', '0', '0'}, {'0', '1', '1', 'b', 'm', '1', '0', '0', 'b'}}),
	LARGE5(new char[][] {{'2', '2', '2', 'b', '2', 'b', 'b', 'b', '1'}, {'m', 'm', '2', 'm', '2', 'm', '2', 'm', 'b'}, {'b', '4', '4', '2', '2', '1', '3', '3', '3'}, {'1', 'm', 'm', '2', '0', '0', '1', 'm', 'm'}, {'2', '4', 'm', '2', '0', '0', '1', '3', '3'}, {'1', 'm', '3', 'b', '2', '2', '1', '2', 'm'}, {'1', '1', '2', 'm', 'm', '2', 'm', '2', '1'}, {'1', '1', 'b', '2', '2', '2', '1', '2', '1'}, {'1', 'm', '1', 'b', '0', 'b', '0', '1', 'm'}}),
	LARGE6(new char[][] {{'0', '0', '1', 'm', '2', '2', 'm', '1', '0'}, {'0', 'b', '1', '1', '2', 'm', '2', '1', 'b'}, {'1', '1', '0', 'b', '2', '3', '2', 'b', '0'}, {'m', '3', '1', '1', 'm', '3', 'm', '3', '1'}, {'m', 'm', '1', '1', '1', '3', 'm', '4', 'm'}, {'3', '3', '1', '1', '1', '2', '2', 'm', '3'}, {'m', '1', '0', '1', 'm', '2', '2', '2', 'm'}, {'2', '2', '0', '2', '3', 'm', '1', 'b', 'b'}, {'m', '1', '0', '1', 'm', '2', '1', '0', 'b'}}),
	LARGE7(new char[][] {{'0', '1', '1', '1', '2', 'm', '3', '3', 'm'}, {'0', '1', 'm', '1', '2', 'm', 'm', '3', 'm'}, {'0', '1', 'b', '1', '1', '2', '3', '3', '2'}, {'2', '2', '1', '0', '0', '0', '1', 'm', '1'}, {'m', 'm', 'b', '0', '0', '0', '1', '2', '2'}, {'3', 'm', '3', '2', '2', '2', '1', '3', 'm'}, {'2', '2', '2', 'm', 'm', '4', 'm', '3', 'm'}, {'m', '2', '2', '4', 'm', 'm', '2', 'b', '1'}, {'1', '2', 'm', '2', '2', '2', 'b', '0', '0'}}),
	LARGE8(new char[][] {{'0', '0', '1', '2', '2', '1', '1', 'm', '1'}, {'0', '0', '1', 'm', 'm', '1', '2', 'b', '2'}, {'1', '1', '1', '2', '2', '1', '1', 'm', 'b'}, {'m', '1', '0', '0', '0', '0', '1', '1', '1'}, {'1', '2', 'b', '3', '2', '2', '1', '1', '0'}, {'b', 'b', 'm', 'm', 'm', '2', 'm', '1', '0'}, {'m', '5', 'm', '7', '4', 'b', '1', '1', '0'}, {'m', '4', 'm', 'm', 'm', '2', 'b', '2', '1'}, {'1', '2', '3', 'm', '3', '2', 'm', '2', 'm'}}),
	LARGE9(new char[][] {{'1', '1', '0', '0', '2', 'm', 'm', '1', '0'}, {'m', '1', '0', '0', '2', 'm', '3', 'b', '0'}, {'2', '2', '1', '0', '1', '1', '1', '1', '1'}, {'2', 'm', '2', '0', '0', '0', '1', '2', 'm'}, {'3', 'm', '3', 'b', '0', '0', '1', 'm', '3'}, {'4', 'm', '3', '0', '0', '0', '1', '3', 'm'}, {'m', 'm', 'b', '0', '1', '1', 'b', 'b', 'm'}, {'3', 'b', '3', 'b', '2', 'm', '3', 'm', 'b'}, {'1', 'm', 'm', '1', '2', 'm', '3', '1', '1'}}),
	LARGE10(new char[][] {{'1', '1', '1', '1', 'm', 'm', 'b', '1', 'm'}, {'1', 'm', '1', '1', '2', '2', '1', '1', '1'}, {'1', 'b', '2', '1', '0', '0', '0', '0', '0'}, {'1', 'b', 'm', '2', '0', '0', '0', '0', 'b'}, {'m', '5', 'm', '2', '0', '0', '0', '1', '1'}, {'m', 'm', '2', '1', '0', '0', '0', '1', 'm'}, {'3', '3', '1', '1', '1', '1', '0', '1', '1'}, {'m', '3', '2', '2', 'm', '1', 'b', '1', '1'}, {'m', 'm', '2', 'm', '2', '1', '0', '1', 'm'}}),


	;


	public char[][] map;

	World(char[][] map) {
		this.map = map;
	}
}
