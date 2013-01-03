package musicmachine.artlisten.runner;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import musicmachine.artlisten.core.ImageMusicConverter;
import musicmachine.artlisten.gui.FileChosenListener;
import musicmachine.artlisten.gui.ImageLoadPanel;
import musicmachine.artlisten.imc.CrunchIMC;

import org.jfugue.Player;

@SuppressWarnings("serial")
public class ArtListenSwingRunner extends JPanel implements ActionListener, FileChosenListener {
	public static final String TITLE = "Art Listen - Image Music Converter";
	public static final int WINDOW_HEIGHT = 800;
	public static final int WINDOW_WIDTH = 600;
	protected static final File DEFAULT_DIRECTORY = new File(System.getProperty("user.dir"));

	protected static final String PROCESS_READY = "Convert & Play";
	protected static final String PROCESS_CONVERTING = "Converting...";
	protected static final String PROCESS_PLAYING = "Playing";
	protected JButton btnProcess = new JButton();
	protected static final String MIDI_UNSAVED = "Save Midi";
	protected static final String MIDI_SAVING = "Saving Midi...";
	protected static final String MIDI_SAVED = "Midi saved.";
	protected JButton btnSaveMidi = new JButton();
	protected static final String SONG_UNSAVED = "Save Song";
	protected static final String SONG_SAVING = "Saving Song...";
	protected static final String SONG_SAVED = "Song saved.";
	protected JButton btnSaveSong = new JButton();
	protected static final String STOP_UNUSABLE = "Stop";
	protected static final String STOP_CONVERTING = "Stop Converting";
	protected static final String STOP_PLAYING = "Pause Playing";
	protected static final String STOP_RESUME = "Resume Playing";
	protected JButton btnStop = new JButton();
	protected ImageLoadPanel imagePanel = new ImageLoadPanel("song");

	protected boolean convertingImage = false;
	protected boolean playingImage = false;
	protected boolean savingMidi = false, savedMidi = false;
	protected boolean savingSong = false, savedSong = false;

	protected static final int NUM_VOICES = 10;
	protected ImageMusicConverter imc = new CrunchIMC(NUM_VOICES);
	protected MusicianThread musicianThread = new MusicianThread();
	protected File curFile;

	protected boolean fakingFileChange = false; // used in a rare state warning

	public ArtListenSwingRunner() {
		super(new BorderLayout());
		musicianThread.start();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupGui();
			}
		});
	}
	protected void setupGui() {
		imagePanel.setCurrentDirectory(DEFAULT_DIRECTORY);
		imagePanel.addFileChosenListener(this);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(4,0));
		btnProcess.addActionListener(this);
		buttonsPanel.add(btnProcess);
		btnSaveMidi.addActionListener(this);
		buttonsPanel.add(btnSaveMidi);
		btnSaveSong.addActionListener(this);
		buttonsPanel.add(btnSaveSong);
		btnStop.addActionListener(this);
		buttonsPanel.add(btnStop);

		this.add(imagePanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.EAST);

		updateButtons();
		btnProcess.setEnabled(false);
	}
	protected void updateButtons() {
		if (convertingImage) {
			btnProcess.setText(PROCESS_CONVERTING);
			btnProcess.setEnabled(false);
			btnStop.setText(STOP_CONVERTING);
			btnStop.setEnabled(true);
		} else if (playingImage) {
			btnProcess.setText(PROCESS_PLAYING);
			btnProcess.setEnabled(false);
			btnStop.setText(STOP_PLAYING);
			btnStop.setEnabled(true);
		} else {
			btnProcess.setText(PROCESS_READY);
			btnProcess.setEnabled(true);
			btnStop.setText(STOP_UNUSABLE);
			btnStop.setEnabled(false);
		}

		if (savingMidi) {
			btnSaveMidi.setText(MIDI_SAVING);
			btnSaveMidi.setEnabled(false);
		} else if (savedMidi) {
			btnSaveMidi.setText(MIDI_SAVED);
			btnSaveMidi.setEnabled(false);
		} else if (playingImage) {
			btnSaveMidi.setText(MIDI_UNSAVED);
			btnSaveMidi.setEnabled(true);
		} else {
			btnSaveMidi.setText(MIDI_UNSAVED);
			btnSaveMidi.setEnabled(false);
		}

		if (savingSong) {
			btnSaveSong.setText(SONG_SAVING);
			btnSaveSong.setEnabled(false);
		} else if (savedSong) {
			btnSaveSong.setText(SONG_SAVED);
			btnSaveSong.setEnabled(false);
		} else if (playingImage) {
			btnSaveSong.setText(SONG_UNSAVED);
			btnSaveSong.setEnabled(true);
		} else {
			btnSaveSong.setText(SONG_UNSAVED);
			btnSaveSong.setEnabled(false);
		}

		if (musicianThread.isSongPaused()) {
			btnStop.setText(STOP_RESUME);
			btnStop.setEnabled(true);
		}
		btnProcess.setEnabled(imagePanel.hasFileSelected() && !playingImage);
	}

	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if (src == btnProcess) {
			process();
		} else if (src == btnSaveMidi) {
			saveMidi();
		} else if (src == btnSaveSong) {
			saveSong();
		} else if (src == btnStop) {
			stopCurrent();
		}
	}
	protected void process() {
		// TODO make it threaded and handle flag flipping cases when manually Stopped
		if (!convertingImage && !playingImage) {
			setConvertingImage(true);
			musicianThread.setSong(imc.convert(imagePanel.getImage()));
			setConvertingImage(false);
			musicianThread.playSong();
		}
	}
	protected void saveMidi() {
		if (playingImage && !savedMidi) {
			File midiFile = getMidiSaveFile();
			try {
				musicianThread.saveMidi(midiFile);
			} catch (IOException ex) {
				setSavingMidi(false);
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed to save midi \"" + midiFile.getAbsolutePath() + "\"." +
						" Make sure you have permission to access the file.",
						"Midi Save Failure", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	protected void saveSong() {
		if (playingImage && !savedSong) {
			File songFile = getSongSaveFile();
			try {
				musicianThread.saveSong(songFile);
				setSavedSong(true);
			} catch (IOException ex) {
				setSavingSong(false);
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed to save song to text file \"" + songFile.getAbsolutePath() + "\"." +
						" Make sure you have permission to access the file.",
						"Song Save Failure", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	protected File getMidiSaveFile() {
		return new File(curFile.getName() + "-" + NUM_VOICES + ".midi");
	}
	protected File getSongSaveFile() {
		return new File(curFile.getName() + "-" + NUM_VOICES + "_song.txt");
	}
	protected void stopCurrent() {
		if (musicianThread.isSongPaused()) {
			musicianThread.resumeSong();
		} else {
			musicianThread.pauseSong();
		}
	}
	public void fileChosen(File file) {
		// File change while saving might cause state errors, but this warning should rarely appear
		if (fakingFileChange) {
			fakingFileChange = false;
			return;
		}
		boolean resumeAfterwards = false;
		if (musicianThread.isSongStarted()) {
			if (!musicianThread.isSongPaused()) {
				musicianThread.pauseSong();
				resumeAfterwards = true;
			}
		}
		if (savingMidi || savingSong) {
			int choice = JOptionPane.showConfirmDialog(this,
					"Ongoing saves are occurring. Changing file at this time may result in errors.\nContinue anyway?",
					"Saving Incomplete", JOptionPane.YES_NO_OPTION);
			if (choice != JOptionPane.YES_OPTION) {
				fakingFileChange = true;
				imagePanel.loadImageFile(curFile);
				if (musicianThread.isSongPaused() && resumeAfterwards) {
					musicianThread.resumeSong();
				}
				return;
			}
		}

		// Normal file switch case
		curFile = file;
		if (musicianThread.isSongStarted()) {
			musicianThread.setSong(null);
			musicianThread.stopSong();
		}
		setConvertingImage(false);
		setPlayingImage(false);
		setSavingMidi(false);
		setSavedMidi(false);
		setSavingSong(false);
		setSavedSong(false);
		updateButtons();
	}

	protected void setConvertingImage(boolean convertingImage) {
		this.convertingImage = convertingImage;
		updateButtons();
	}
	protected void setPlayingImage(boolean playingImage) {
		this.playingImage = playingImage;
		updateButtons();
	}
	protected void setSavingMidi(boolean savingMidi) {
		this.savingMidi = savingMidi;
		updateButtons();
	}
	protected void setSavedMidi(boolean savedMidi) {
		this.savedMidi = savedMidi;
		updateButtons();
	}
	protected void setSavingSong(boolean savingSong) {
		this.savingSong = savingSong;
		updateButtons();
	}
	protected void setSavedSong(boolean savedSong) {
		this.savedSong = savedSong;
		updateButtons();
	}

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle(TITLE);
		window.setSize(WINDOW_HEIGHT, WINDOW_WIDTH);
		JPanel panel = new ArtListenSwingRunner();
		window.add(panel);
		window.setVisible(true);
		panel.revalidate();
	}

	public class MusicianThread extends Thread {
		protected Player musician = new Player();
		protected String song;
		protected boolean begun = false;

		public synchronized void run() {
			while (true) {
				while (!begun) {
					try {
						wait();
					} catch (InterruptedException e) { }
				}
				begun = false;
				if (song != null) {
					setPlayingImage(true);
					musician.play(song);
					setPlayingImage(false);
				}
			}
		}

		public synchronized void playSong() {
			begun = true;
			notifyAll();
		}

		public boolean isSongStarted() {
			return musician.isStarted();
		}
		public boolean isSongPaused() {
			return musician.isPaused();
		}
		public void pauseSong() {
			musician.pause();
			updateButtons();
		}
		public void resumeSong() {
			musician.resume();
			updateButtons();
		}
		public void stopSong() {
			musician.stop();
			setPlayingImage(false);
			updateButtons();
		}

		public void setSong(String song) {
			this.song = song;
		}

		public void saveMidi(File midiFile) throws IOException {
			setSavingMidi(true);
			musician.saveMidi(song, midiFile);
			setSavingMidi(false);
			setSavedMidi(true);
		}
		public void saveSong(File songFile) throws IOException {
			setSavingSong(true);
			FileWriter writer = new FileWriter(songFile);
			writer.write(song);
			writer.close();
			setSavingSong(false);
			setSavedSong(true);
		}
	}
}
