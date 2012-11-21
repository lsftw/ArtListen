package musicmachine.artlisten;

import java.awt.image.BufferedImage;

// Pixel by Pixel IMC, each individual pixel corresponds to a note and each note is independent of other notes
// Relaxed version adds rests and extends durations
public class CrunchIMCRelaxed implements ImageMusicConverter {
	protected int numVoices;
	
	public CrunchIMCRelaxed() {
		numVoices = 10;
	}
	public CrunchIMCRelaxed(int numVoices) {
		this.numVoices = numVoices;
	}
	public String convert(BufferedImage image) {
		String[] musicStrings = new String[numVoices];
		for (int i = 0; i < musicStrings.length; i++) {
			musicStrings[i] = "";
		}
		
		int curVoice;
		int rgb, r, g, b;
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		for (int x = 0; x < image.getWidth(); x++) {
			curVoice = x % musicStrings.length;
			for (int y = 0; y < image.getHeight(); y++) {
				//rgb = image.getRGB(x,y);
				rgb = pixels[x + y*image.getWidth()];
				r = (rgb & 0x00ff0000) >> 16;
				g = (rgb & 0x0000ff00) >> 8;
				b = rgb & 0x000000ff;
				//
				musicStrings[curVoice] += toNote(r, g, b) + " ";
			}
		}
		
		String musicString = "";
		for (int i = 0; i < musicStrings.length; i++) {
			musicString += "V" + i + " " + musicStrings[i];
		}
		return musicString;
	}
	
	private static String toNote(int r, int g, int b) {
		return toPitch(r) + toOctave(g) + toDuration(b);
	}
	private static String toPitch(int r) {
		r = 7 * r / 255;
		switch (r) {
		case 0: return "A";
		case 1: return "B";
		case 2: return "C";
		case 3: return "D";
		case 4: return "E";
		case 5: return "F";
		case 6: return "G";
		case 7: return "R";
		}
		return "[badpitch:" + r + "]";
	}
	private static int toOctave(int g) {
		return (7 * g / 255) + 1;
	}
	private static String toDuration(int b) {
		b = 6 * b / 255;
		switch (b) {
		case 0: return "s";
		case 1: return "st";
		case 2: return "i";
		case 3: return "is";
		case 4: return "q";
		case 5: return "qi";
		case 6: return "h";
		}
		return "[badduration:" + b + "]";
	}
}
/*
Red (Pitch)
A255*0/7
B255*1/7
C255*2/7
D255*3/7
E255*4/7
F255*5/7
G255*6/7
R255*7/7
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
i 8
s 16
t 32
x 64
o 128
 */
