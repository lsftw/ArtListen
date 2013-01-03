package musicmachine.artlisten.runner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import musicmachine.artlisten.core.ImageMusicConverter;
import musicmachine.artlisten.imc.CrunchIMC;

import org.jfugue.Player;

public class ArtListenConsoleRunner {
	private static final double NANOSECONDS_TO_SECONDS = 1 / 1000000000.0;

	private static final int NUMBER_VOICES = 10;

	private static String notesPath(File imageFile) {
		return "imc/" + imageFile.getName() + "-" + NUMBER_VOICES + "_song.txt";
	}
	private static String midiPath(File imageFile) {
		return "imc/" + imageFile.getName() + "-" + NUMBER_VOICES + ".midi";
	}

	// TODO put number of voices into parameter, as well as saving notes and midi
	public static void main(String[] args) {
		String imagePath;
		if (args != null) {
			if (args.length >= 1) {
				imagePath = getArgs(args);
			} else {
				System.out.println("Usage: imc <imageFile> or imc gui");
				return;
			}
		} else {
			System.out.println("Usage: imc <imageFile> or imc gui");
			return;
		}
		ImageMusicConverter imc = new CrunchIMC(NUMBER_VOICES);
		File imageFile = new File(imagePath);
		File outputFile = new File(notesPath(imageFile));
		long timeStart, timeEnd;
		Player musician = new Player();

		// Load
		BufferedImage image = null;
		System.out.println("Loading file " + imageFile);
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
		String musicString = "";

		// Process
		System.out.println("Processing image " + imageFile);
		timeStart = System.nanoTime();
		musicString = imc.convert(image);
		timeEnd = System.nanoTime();
		System.out.println("Took " + timeTaken(timeStart, timeEnd) + " seconds to process image into music string.");

		// Save
		System.out.println("Saving file of length " + musicString.length());
		timeStart = System.nanoTime();
		try {
			FileWriter writer = new FileWriter(outputFile);
			writer.write(musicString);
			writer.close();
			musician.saveMidi(musicString, new File(midiPath(imageFile)));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		timeEnd = System.nanoTime();
		System.out.println("Took " + timeTaken(timeStart, timeEnd) + " seconds to process save music string into file.");

		System.out.println("Playing music of length " + musicString.length());
		musician.play(musicString);
	}

	private static String getArgs(String[] args) {
		StringBuffer temp = new StringBuffer();
		for (String arg : args) {
			temp.append(arg);
			temp.append(" ");
		}
		temp.setLength(temp.length() - 1); // Remove last space
		return temp.toString();
	}
	private static double timeTaken(long timeStart, long timeEnd) {
		return (timeEnd - timeStart) * NANOSECONDS_TO_SECONDS;
	}
}
