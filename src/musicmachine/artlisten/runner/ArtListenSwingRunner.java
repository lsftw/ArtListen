package musicmachine.artlisten.runner;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import musicmachine.artlisten.gui.FileChosenListener;
import musicmachine.artlisten.gui.ImageFilePanel;
import musicmachine.artlisten.gui.ImageLoadPanel;

// TODO threading, synchronization
// TODO stop or pause?
@SuppressWarnings("serial")
public class ArtListenSwingRunner extends JPanel implements FileChosenListener {
	public static final String TITLE = "IMOD Image Manip";
	public static final int WINDOW_HEIGHT = 800;
	public static final int WINDOW_WIDTH = 600;
	protected static final Border PADDING_BORDER = new EmptyBorder(10,10,10,10);
	protected static final File DEFAULT_DIRECTORY = new File(System.getProperty("user.dir"));

	protected static final String PROCESS_READY = "Convert & Play";
	protected static final String PROCESS_CONVERTING = "Converting...";
	protected static final String PROCESS_PLAYING = "Playing";
	protected JButton btnProcess = new JButton();
	protected static final String MIDI_UNSAVED = "Save Midi";
	protected static final String MIDI_SAVED = "Midi saved.";
	protected JButton btnSaveMidi = new JButton();
	protected static final String SONG_UNSAVED = "Save Song";
	protected static final String SONG_SAVED = "Song saved.";
	protected JButton btnSaveSong = new JButton();
	protected static final String STOP_UNUSABLE = "Stop";
	protected static final String STOP_CONVERTING = "Stop Converting";
	protected static final String STOP_PLAYING = "Stop Playing";
	protected JButton btnStop = new JButton();

	protected boolean convertingImage = false;
	protected boolean playingImage = false;
	protected boolean savingMidi = false;
	protected boolean savingSong = false;

	public ArtListenSwingRunner() {
		super(new BorderLayout());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupGui();
			}
		});
	}
	protected void setupGui() {
		ImageFilePanel imagePanel = new ImageLoadPanel("song");
		imagePanel.setBorder(PADDING_BORDER);
		imagePanel.setCurrentDirectory(DEFAULT_DIRECTORY);
		imagePanel.addFileChosenListener(this);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(4,0));
		buttonsPanel.add(btnProcess);
		buttonsPanel.add(btnSaveMidi);
		buttonsPanel.add(btnSaveSong);
		buttonsPanel.add(btnStop);

		this.add(imagePanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.EAST);

		updateButtons();
	}
	protected void updateButtons() {
		if (convertingImage) {
			btnProcess.setText(PROCESS_CONVERTING);
			btnProcess.setEnabled(false);
			btnSaveMidi.setText(MIDI_UNSAVED);
			btnSaveMidi.setEnabled(false);
			btnSaveSong.setText(SONG_UNSAVED);
			btnSaveSong.setEnabled(false);
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
			btnSaveMidi.setText(MIDI_UNSAVED);
			btnSaveMidi.setEnabled(false);
			btnSaveSong.setText(SONG_UNSAVED);
			btnSaveSong.setEnabled(false);
			btnStop.setText(STOP_UNUSABLE);
			btnStop.setEnabled(false);
		}
	}

	public void fileChosen(File file) {
		// TODO Auto-generated method stub
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
