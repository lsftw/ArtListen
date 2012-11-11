package musicmachine.songs;
import java.util.Scanner;

import org.jfugue.Player;

public class PlayMusic {
	public static void main(String[] args) {
		Player player = new Player();
		Scanner kb = new Scanner(System.in);
		String input;
		while (true) {
			input = kb.nextLine();
			//
			/*try {
				player.saveMidi(input, new File("songs/playMusic.mid"));
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			//
			player.play(input);
		}
	}
}
