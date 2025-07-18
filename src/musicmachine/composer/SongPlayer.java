package musicmachine.composer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jfugue.Player;

@SuppressWarnings("serial")
public class SongPlayer extends SGui implements ActionListener, KeyListener, MouseListener {
	// Window Screen
	private static final Color BACKGROUND_COLOR = Color.WHITE;
	//
	// Don't modify following variables
	//
	// Song related
	private ArrayList<MusicianThread> musicians = new ArrayList<MusicianThread>();
	private int manipIndex = 0;
	// Error Handling
	private StringWriter exceptionWriter = new StringWriter();
	private PrintWriter exceptionHandler = new PrintWriter(exceptionWriter);//converts stack trace to string

	public SongPlayer() {
		// Listeners
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setFocusable(true); // Allows KeyListener to work
		this.setFocusTraversalKeysEnabled(false); // Allows listening to <Tab> key
	}
	//////////////////////////////////////////////////
	//               Graphics Methods               //
	//////////////////////////////////////////////////
	public void drawGUI(Graphics g) {
		for (int i = 0; i < musicians.size(); i++) {
			if (i == manipIndex) {
				gbuff.setColor(Color.YELLOW);
				if (musicians.get(i).playing) {
					gbuff.setColor(Color.GREEN);
				}
			} else if (musicians.get(i).playing) {
				gbuff.setColor(Color.BLUE);
			} else {
				gbuff.setColor(Color.BLACK);
			}
			gbuff.drawString(i + ": " + musicians.get(i).song, 10, 32 + i * 20);
		}
		if (manipIndex == musicians.size()) {
			gbuff.drawString(manipIndex + "...", 10, 32 + manipIndex * 20);
		}
	}
	public void dt() {
	}

	private static void setupGUI() {
		JFrame frame = new JFrame("Song Player");
		// Frame Setup
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(SCREEN_X, SCREEN_Y);
		SongPlayer program = new SongPlayer();
		//program.setBackground(BACKGROUND_COLOR);
		frame.setBackground(BACKGROUND_COLOR);
		frame.add(program);
		frame.addMouseListener(program);
		frame.addKeyListener(program);

		program.inception();
		frame.setVisible(true);
	}
	///////////////////////////////////////////////
	//               main() method               //
	///////////////////////////////////////////////
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { setupGUI(); }
		}); // For thread safety
	}
	//////////////////////////////////////////////
	//               Misc Methods               //
	//////////////////////////////////////////////
	public String exceptiontostr(Exception ex) {
		ex.printStackTrace(exceptionHandler);
		return exceptionWriter.toString();
	}
	//	private void addNote(String note) {
	//		if (manipindex < musicians.size()) {
	//			musicians.get(manipindex).song += ' ' + note;
	//		} else {
	//			musicians.add(new MusicianThread());
	//			musicians.get(manipindex).song += ' ' + note;
	//		}
	//	}
	//	private boolean removeNote() {
	//		if (manipindex < musicians.size()) {
	//			String song = musicians.get(manipindex).song;
	//			if (song.length() >= 2) {
	//				musicians.get(manipindex).song = song.substring(0, song.length() - 2);
	//				return true;
	//			}
	//		}
	//		return false;
	//	}
	/////////////////////////////////////////
	//               Actions               //
	/////////////////////////////////////////
	public void actionPerformed(ActionEvent ae) { }
	//////////////////////////////////////////
	//               Keyboard               //
	//////////////////////////////////////////
	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			exitProgram();
			break;
			/*case KeyEvent.VK_A:
			addNote("C");
			break;
		case KeyEvent.VK_S:
			addNote("D");
			break;
		case KeyEvent.VK_D:
			addNote("E");
			break;
		case KeyEvent.VK_F:
			addNote("F");
			break;
		case KeyEvent.VK_J:
			addNote("G");
			break;
		case KeyEvent.VK_K:
			addNote("A");
			break;
		case KeyEvent.VK_L:
			addNote("B");
			break;*/
		case KeyEvent.VK_F5:
			try {
				musicians.get(manipIndex).musician.saveMidi(musicians.get(manipIndex).song, new File(JOptionPane.showInputDialog("Midi Name")));
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case KeyEvent.VK_ENTER:
			//musician.play(song);
			//Pattern temp = musician.loadMidi(new File("morrowind.mid"));
			//temp.offset();
			//musician.play(temp);
			//musician.playMidiDirectly(new File("morrowind.mid"));
			/*MusicianThread temp = new MusicianThread();
			temp.start();
			musicians.add(temp);*/
			if (manipIndex < musicians.size()) {
				if (musicians.get(manipIndex).playing) { // stop the music
					musicians.get(manipIndex).stopAsap();
				} else if (!musicians.get(manipIndex).playing) {
					if (musicians.get(manipIndex).ranOnce) { // start again
						musicians.get(manipIndex).restart();
					} else { // first time starting
						musicians.get(manipIndex).start();
					}
				}
			} else {
				System.out.println("Current musician doesn't have any notes!");
			}
			break;
		case KeyEvent.VK_BACK_SPACE:
			//removeNote();
			if (manipIndex < musicians.size()) {
				String song = musicians.get(manipIndex).song;
				if (song.length() >= 1) {
					musicians.get(manipIndex).song = song.substring(0, song.length() - 1);
				}
			}
			break;
		case KeyEvent.VK_DELETE:
			Player curmusician;
			for (int i = 0; i < musicians.size(); i++) {
				curmusician = musicians.get(i).musician;
				curmusician.close();
				if (curmusician.isPlaying()) {
					curmusician.stop();
				}
			}
			break;
		case KeyEvent.VK_UP:
			manipIndex--;
			if (manipIndex < 0) {
				manipIndex = musicians.size();
			}
			break;
		case KeyEvent.VK_DOWN:
			manipIndex++;
			if (manipIndex > musicians.size()) {
				manipIndex = 0;
			}
			break;
		default:
			break;
		}
	}
	public void keyReleased(KeyEvent ke) { }
	public void keyTyped(KeyEvent ke) {
		if (!validKey(ke.getKeyChar())) return;
		System.out.print("Typed something...   ");
		if (manipIndex < musicians.size()) {
			musicians.get(manipIndex).song += ke.getKeyChar();
		} else {
			musicians.add(new MusicianThread());
			musicians.get(manipIndex).song += ke.getKeyChar();
		}
		System.out.print("Added char: (" + ke.getKeyChar() + ")");
		if (ke.getKeyChar() == ' ') System.out.print(" space");
		System.out.println();
	}
	public static boolean validKey(char keychar) {
		return !Character.isISOControl(keychar);
		//return Character.isLetter(keychar) || Character.isDigit(keychar)
		//|| keychar == ' ' || keychar == '[' || keychar == ']';
	}
	///////////////////////////////////////
	//               Mouse               //
	///////////////////////////////////////
	public void mousePressed(MouseEvent me) { }
	public void mouseClicked(MouseEvent me) { }
	public void mouseEntered(MouseEvent me) { }
	public void mouseExited(MouseEvent me) { }
	public void mouseReleased(MouseEvent me) { }
}
