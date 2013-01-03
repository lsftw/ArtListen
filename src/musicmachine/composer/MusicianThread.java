package musicmachine.composer;
import org.jfugue.Player;

public class MusicianThread extends Thread {
	Player musician = new Player();
	String song = "";
	boolean playing = false;
	volatile boolean shouldStop = false;
	boolean ranOnce = false;

	public void run() {
		ranOnce = true;
		playing = true;
		while (true) {
			while (!shouldStop) {
				musician.play(song);
				playing = true;
			}
			playing = false;
			try {
				Thread.sleep(1);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void stopAsap() {
		musician.stop();
		shouldStop = true;
	}
	public void restart() {
		shouldStop = false;
	}
}