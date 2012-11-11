package musicmachine.artlisten;

import java.awt.image.BufferedImage;

// Pixel by Pixel IMC, each individual pixel corresponds to a note and each note is independent of other notes
public class SimpleIMC implements ImageMusicConverter {
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
		case 0: return "o";
		case 1: return "x";
		case 2: return "xo";
		case 3: return "t";
		case 4: return "tx";
		case 5: return "s";
		case 6: return "i";
		}
		return "[badduration:" + b + "]";
	}
}
/*
Red (Pitch)
A255*0/6
B255*1/6
C255*2/6
D255*3/6
E255*4/6
F255*5/6
G255*6/6
Green (Octave)
(8/255) * GREEN
Blue (Duration)
(6/255) * BLUE
0:i
1:q
2:h
3:w
4:wi
5:wq
6:wh
 */
