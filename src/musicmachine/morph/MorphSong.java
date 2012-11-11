package musicmachine.morph;
import java.util.HashMap;
import java.util.Scanner;

import org.jfugue.Pattern;
import org.jfugue.Player;

public class MorphSong {
	public static HashMap<String, String> mutations = new HashMap<String, String>();
	public static HashMap<String, Integer> fitness = new HashMap<String, Integer>();
	// TODO use fitness
	//
	public static void main(String[] args) {
		loadMutations1();
		//
		Player musician = new Player();
		Pattern musicPlaying = new Pattern("A5 C4 D3");
		Scanner kb = new Scanner(System.in);
		String input;
		//
		System.out.println("Song: " + musicPlaying);
		while (true) {
			System.out.print(">");
			input = kb.nextLine();
			if (input.equalsIgnoreCase("play")) {
				musician.play(musicPlaying);
				System.out.println("Play Song: " + musicPlaying);
			} else if (input.equalsIgnoreCase("mutate")) {
				musicPlaying = mutate(musicPlaying);
				System.out.println("Mutate Song: " + musicPlaying);
			} else if (input.equalsIgnoreCase("morphsong")) {
				musician.play(musicPlaying);
				musicPlaying = mutate(musicPlaying);
				System.out.println("Mutate Song: " + musicPlaying);
			} else if (input.equalsIgnoreCase("mutate2")) {
				musicPlaying = mutate(mutate(musicPlaying));
				System.out.println("Mutatex2 Song: " + musicPlaying);
			} else if (input.equalsIgnoreCase("mutate3")) {
				musicPlaying = mutate(mutate(mutate(musicPlaying)));
				System.out.println("Mutatex3 Song: " + musicPlaying);
			} else if (input.equalsIgnoreCase("mutate4")) {
				musicPlaying = mutate(mutate(mutate(mutate(musicPlaying))));
				System.out.println("Mutatex4 Song: " + musicPlaying);
			} else if (input.equalsIgnoreCase("mutate5")) {
				musicPlaying = mutate(mutate(mutate(mutate(mutate(musicPlaying)))));
				System.out.println("Mutatex5 Song: " + musicPlaying);
			} else if (input.equalsIgnoreCase("random")) {
				musicPlaying.setMusicString(randomSong(10));
			} else if (input.equalsIgnoreCase("randomplay")) {
				musicPlaying.setMusicString(randomSong(10));
				System.out.println("Random Song: " + musicPlaying);
				musician.play(musicPlaying);
			} else if (input.equalsIgnoreCase("good")) {
				incrementFitness(musicPlaying.getMusicString());
			} else if (input.equalsIgnoreCase("bad")) {
				decrementFitness(musicPlaying.getMusicString());
			}
		}
	}
	private static void incrementFitness(String musicString) {
		Integer fitval = fitness.get(musicString);
		if (fitval != null) {
			fitness.put(musicString, fitval+1);
		} else {
			fitness.put(musicString, 1);
		}
	}
	private static void decrementFitness(String musicString) {
		Integer fitval = fitness.get(musicString);
		if (fitval != null) {
			fitness.put(musicString, fitval-1);
		} else {
			fitness.put(musicString, -1);
		}
	}
	public static String randomSong(int length) {
		StringBuffer temp = new StringBuffer();
		char curnote;
		char nth;
		for (int note = 0; note < length; note++) {
			curnote = (char)((int)(Math.random()*7) + 'A');
			nth = (char)((int)(Math.random()*6) + '1');
			temp.append("" + curnote + nth);
			if (note != length -1) temp.append(" ");
		}
		return temp.toString();
	}
	public static String breedSong(int length) {
		StringBuffer temp = new StringBuffer();
		// TODO implement
		return temp.toString();
	}
	private static void loadMutations1() {
		mutations.put("A5", "A4 C2");
		mutations.put("A4", "C4");
		mutations.put("C4", "C3 D3");
		mutations.put("D3", "C5");
		mutations.put("C3", "C5 D3");
		mutations.put("C5", "A5");
		mutations.put("C2", "D3");
	}
	//
	public static Pattern mutate(Pattern pattern) {
		StringBuffer temp = new StringBuffer();
		String[] tokens = pattern.getTokens();
		for (String token : tokens) {
			temp.append(mutateNote(token) + " ");
		}
		pattern.setMusicString(temp.toString().trim());
		return pattern;
	}
	public static String mutateNote(String noteString) {
		if (mutations.get(noteString) != null) {
			return mutations.get(noteString);
		}
		return noteString;
	}
	//
	//
	//
	static class Song {
		String musicString;
		int fitness = 0;
	}
}
