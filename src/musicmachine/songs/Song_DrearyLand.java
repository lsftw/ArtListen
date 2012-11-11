package musicmachine.songs;

import java.util.Scanner;

import org.jfugue.Player;

public class Song_DrearyLand {
	public static void main(String[] args) {
		Player player = new Player();
		Scanner songReader = new Scanner("songs/dreary_land.txt");
		String input;
		while (songReader.hasNextLine()) {
			input = songReader.nextLine();
			System.out.println(input);
			player.play(input);
		}
	}
}
