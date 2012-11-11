package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Noiser extends JFrame implements ActionListener {
	public Noiser() {
		JButton btn = new JButton("Hear Me");
		btn.addActionListener(this);
		add(btn);
		pack();
	}
	public void actionPerformed(ActionEvent ae) {
		try {
			playClip(new File("disconnect_x.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Noiser window = new Noiser();
		window.setVisible(true);
	}
	//
	// Source: http://stackoverflow.com/questions/577724/trouble-playing-wav-in-java/577926#577926
	private static void playClip(File soundFile)
	throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			try {
				clip.start();
				clip.drain();
			} finally {
				clip.close();
			}
		} finally {
			audioInputStream.close();
		}
	}
}
