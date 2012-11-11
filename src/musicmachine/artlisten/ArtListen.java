package musicmachine.artlisten;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfugue.Player;

public class ArtListen {
	private static final double NANOSECONDS_TO_SECONDS = 1 / 1000000000.0;
	private static final String IMAGE_PATH = "imc/" + "bonfire.png";

	public static void main(String[] args) {
		ImageMusicConverter imc = new CrunchIMC(10);
		File imageFile = new File(IMAGE_PATH);
		File outputFile = new File("imc/" + imageFile.getName() + "_song.txt");
		long timeStart, timeEnd;

		// Load
		BufferedImage image = null;
		System.out.println("Loading file " + imageFile);
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException ex) {
			ex.printStackTrace();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		timeEnd = System.nanoTime();
		System.out.println("Took " + timeTaken(timeStart, timeEnd) + " seconds to process save music string into file.");

		System.out.println("Playing music of length " + musicString.length());
		Player musician = new Player();
		musician.play(musicString);
	}

	private static double timeTaken(long timeStart, long timeEnd) {
		return (timeEnd - timeStart) * NANOSECONDS_TO_SECONDS;
	}
}
