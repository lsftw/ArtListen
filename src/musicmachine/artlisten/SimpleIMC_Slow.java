package musicmachine.artlisten;

import java.awt.image.BufferedImage;

// Pixel by Pixel IMC, each individual pixel corresponds to a note and each note is independent of other notes
// Durations for notes are longer
public class SimpleIMC_Slow implements ImageMusicConverter {
	public String convert(BufferedImage image) {
		String musicString = "";
		int rgb, r, g, b;
		
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				rgb = image.getRGB(x,y);
				r = (rgb & 0x00ff0000) >> 16;
				g = (rgb & 0x0000ff00) >> 8;
				b = (rgb & 0x000000ff);
				
				musicString += toNote(r, g, b) + " ";
			}
		}
		return musicString;
	}
	
	private static String toNote(int r, int g, int b) {
		return toPitch(r) + toOctave(g) + toDuration(b);
	}
	private static String toPitch(int r) {
		r = 6 * r / 255;
		switch (r) {
		case 0: return "A";
		case 1: return "B";
		case 2: return "C";
		case 3: return "D";
		case 4: return "E";
		case 5: return "F";
		case 6: return "G";
		}
		return "[badpitch:" + r + "]";
	}
	private static int toOctave(int g) {
		return (7 * g / 255) + 1;
	}
	private static String toDuration(int b) {
		b = 6 * b / 255;
		switch (b) {
		case 0: return "t";
		case 1: return "s";
		case 2: return "st";
		case 3: return "i";
		case 4: return "q";
		case 5: return "h";
		case 6: return "w";
		}
		return "[badduration:" + b + "]";
	}
}
