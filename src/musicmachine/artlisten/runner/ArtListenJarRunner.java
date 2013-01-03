package musicmachine.artlisten.runner;

public class ArtListenJarRunner {
	public static void main(String[] args) {
		if (args != null && args.length == 1) {
			if (args[0].equalsIgnoreCase("gui")) {
				ArtListenSwingRunner.main(args);
				return;
			}
		}
		ArtListenConsoleRunner.main(args);
	}
}
