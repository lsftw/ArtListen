package musicmachine.songs;

import org.jfugue.Player;

public class SongTest {
	public static void main(String[] args) {
		//skyMusic();
	}
	//public static void themeMusic()
	public static void skyMusic() {
		// Upbeat Sky Theme
		// Di Ds Di Ds Ei Es Ei Ei Fi Ci Fi Gi Ei Fi Gi
		Player player = new Player();
		// almost sync - better
		player.play("V0 Di Ds Di Ds Ei Es Ei Ei Fi Ci Fi Gi Ei Fi Gi V1 Rs Di Ds Di Ds Ei Es Ei Ei Fi Ci Fi Gi Ei Fi Gi");
		// catching up
		//player.play("V0 Di Ds Di Ds Ei Es Ei Ei Fi Ci Fi Gi Ei Fi Gi V1 Ri Di Ds Di Ds Ei Es Ei Ei Fi Ci Fi Gi Ei Fi Gi V2 Rs Di Ds Di Ds Ei Es Ei Ei Fi Ci Fi Gi Ei Fi Gi");
	}
}
