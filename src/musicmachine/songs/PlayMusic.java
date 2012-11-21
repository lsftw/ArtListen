package musicmachine.songs;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.jfugue.Player;

public class PlayMusic {
	public static final boolean SAVE_SONG = false;
	public static final String SAVE_LOCATION = "songs/playMusic.mid";
	
	//public static final int JFUGUE_ERROR_LIMIT = 17; // after playing 17 times, play() no longer works
	
	public static void main(String[] args) {
		Player player = new Player();
		Scanner kb = new Scanner(System.in);
		String input;
		while (true) {
			input = kb.nextLine();
			
			if (SAVE_SONG) {
				try {
					player.saveMidi(input, new File(SAVE_LOCATION));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			player.play(input);
		}
	}
}
