package musicmachine.songs;

import java.util.Scanner;

import org.jfugue.Player;

public class Song_ManorMystery {
	public static void main(String[] args) {
		Player player = new Player();
		Scanner songReader = new Scanner("songs/manor_mystery.txt");
		String input;
		while (songReader.hasNextLine()) {
			input = songReader.nextLine();
			System.out.println(input);
			player.play(input);
		}
	}
}
