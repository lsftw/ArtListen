package musicmachine.composer;
import org.jfugue.Player;

public class MusicianThread extends Thread {
	Player musician = new Player();
	String song = "";
	boolean playing = false;
	volatile boolean shouldstop = false;
	boolean ranonce = false;
	//
	public void run() {
		ranonce = true;
		playing = true;
		while (true) {
			while (!shouldstop) {
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
	//
	public void stopASAP() {
		musician.stop();
		shouldstop = true;
	}
	public void restart() {
		shouldstop = false;
	}
}