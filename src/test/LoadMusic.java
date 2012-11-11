package test;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import org.jfugue.Player;

public class LoadMusic
{
    public static void main(String[] args)
    {
        Player player = new Player();
        try {
			player.playMidiDirectly(new File("crabcanon_slow.mid"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InvalidMidiDataException ex) {
			ex.printStackTrace();
		}
        player.close();
    }
}


