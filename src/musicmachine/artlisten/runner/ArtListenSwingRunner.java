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
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import musicmachine.artlisten.core.ImageMusicConverter;
import musicmachine.artlisten.gui.FileChosenListener;
import musicmachine.artlisten.gui.ImageFilePanel;
import musicmachine.artlisten.gui.ImageLoadPanel;
import musicmachine.artlisten.imc.CrunchIMC;

import org.jfugue.Player;

// TODO threading, synchronization
// TODO stop or pause?
@SuppressWarnings("serial")
public class ArtListenSwingRunner extends JPanel implements ActionListener, FileChosenListener {
	public static final String TITLE = "Art Listen Image Music Converter";
	public static final int WINDOW_HEIGHT = 800;
	public static final int WINDOW_WIDTH = 600;
	protected static final Border PADDING_BORDER = new EmptyBorder(10,10,10,10);
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
	protected static final String STOP_PLAYING = "Stop Playing";
	protected JButton btnStop = new JButton();
	protected ImageFilePanel imagePanel = new ImageLoadPanel("song");

	protected boolean convertingImage = false;
	protected boolean playingImage = false;
	protected boolean savingMidi = false, savedMidi = false;
	protected boolean savingSong = false, savedSong = false;

	protected static final int NUM_VOICES = 10;
	protected ImageMusicConverter imc = new CrunchIMC(NUM_VOICES);
	protected Player musician = new Player();
	protected String musicStr;
	protected File curFile;

	public ArtListenSwingRunner() {
		super(new BorderLayout());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupGui();
			}
		});
	}
	protected void setupGui() {
		imagePanel.setBorder(PADDING_BORDER);
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
		// TODO clean up logic for midi/song unsaved saving saved
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
		} else {
			btnSaveSong.setText(SONG_UNSAVED);
			btnSaveSong.setEnabled(false);
		}
		btnProcess.setEnabled(imagePanel.hasFileSelected());
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
			stop();
		}
	}
	protected void process() {
		// TODO Auto-generated method stub
		// TODO make it threaded and handle flag flipping cases when manually Stopped
		if (!convertingImage && !playingImage) {
			setConvertingImage(true);
			curFile = imagePanel.getImageFile();
			musicStr = imc.convert(imagePanel.getImage());
			setConvertingImage(false);
			setPlayingImage(true);
			musician.play(musicStr);
			setPlayingImage(false);
		}
	}
	protected void saveMidi() {
		// TODO handle being manually Stopped if pausing is implemented
		if (playingImage && !savedMidi) {
			File midiFile = getMidiSaveFile();
			try {
				setSavingMidi(true);
				musician.saveMidi(musicStr, midiFile);
				setSavedMidi(true);
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed to save midi \"" + midiFile.getAbsolutePath() + "\"." +
						" Make sure you have permission to access the file.",
						"Midi Save Failure", JOptionPane.INFORMATION_MESSAGE);
			} finally {
				setSavingMidi(false);
			}
		}
	}
	protected void saveSong() {
		// TODO handle being manually Stopped if pausing is implemented
		if (playingImage && !savedSong) {
			File songFile = getSongSaveFile();
			try {
				setSavingSong(true);
				FileWriter writer = new FileWriter(songFile);
				writer.write(musicStr);
				writer.close();
				setSavedSong(true);
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed to save song to text file \"" + songFile.getAbsolutePath() + "\"." +
						" Make sure you have permission to access the file.",
						"Song Save Failure", JOptionPane.INFORMATION_MESSAGE);
			} finally {
				setSavingSong(false);
			}
		}
	}
	protected File getMidiSaveFile() {
		return new File(curFile.getName() + "-" + NUM_VOICES + ".midi");
	}
	protected File getSongSaveFile() {
		return new File(curFile.getName() + "-" + NUM_VOICES + "_song.txt");
	}
	protected void stop() {
		// TODO Auto-generated method stub
	}
	public void fileChosen(File file) {
		// TODO stop musician, continue saves?
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
}
