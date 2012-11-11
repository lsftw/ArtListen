package test;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

import javax.swing.JApplet;

@SuppressWarnings("serial")
public class Audioapp extends JApplet
{
	public class Sound // Holds one audio file
	{
		private AudioClip song; // Sound player
		private URL songPath; // Sound path
		Sound(String filename)
		{
			try
			{
				songPath = new URL(getCodeBase(),filename); // Get the Sound URL
				song = Applet.newAudioClip(songPath); // Load the Sound
			}
			catch(Exception e){} // Satisfy the catch
		}
		public void playSound()
		{
			song.loop(); // Play 
		}
		public void stopSound()
		{
			song.stop(); // Stop
		}
		public void playSoundOnce()
		{
			song.play(); // Play only once
		}
	}
	public void init()
	{
		Sound testsong = new Sound("disconnect_x.wav");
		testsong.playSound();
	}
}

