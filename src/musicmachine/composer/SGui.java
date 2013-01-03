package musicmachine.composer;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class SGui extends JPanel {
	//
	// Drawing related
	//
	public static final int SCREEN_X = 800;
	public static final int SCREEN_Y = 600;
	public static int FPS_CAP = 5000; // Cannot exceed this in FPS
	//
	// Eh you *might* not want to mess with these, but you *can*
	//
	public static final long FPS_RESET_INTERVAL = 100; // Lower = more accurate FPS display but more overhead
	//
	// Don't mess with below
	//
	protected DrawingThread drawer = new DrawingThread(this);
	// Double-buffering adapted from http://docs.rinet.ru/J21/ch11.htm
	protected Image ibuff = null;
	protected Graphics gbuff = null;
	// FPS counting
	protected long count_framereset = FPS_RESET_INTERVAL;
	protected long count_frames = 0;
	protected long time_fpscount = 0;
	protected long time_lastframe = 0; // time when last frame was drawn
	//
	public SGui() {
		setBackground(Color.WHITE);
	}

	public void inception() {
		drawer.start();
	}

	public void paintComponent(Graphics g) {
		if (ibuff == null) {
			ibuff = this.createImage(SCREEN_X, SCREEN_Y);
			gbuff = ibuff.getGraphics();
		}

		//gbuff.setColor(getBackground());
		//gbuff.fillRect(0, 0, this.getWidth(), this.getHeight()); // clear buffer
		gbuff.clearRect(0, 0, this.getWidth(), this.getHeight()); // clear buffer
		drawGUI(gbuff);
		g.drawImage(ibuff, 0, 0, this); // copy buffer to current image
	}
	protected abstract void drawGUI(Graphics g);

	protected void resetfps() {
		// reset fps to average to prevent overflow from count_frames or time_fpscount
		count_frames = count_frames*1000/time_fpscount;
		time_fpscount = 1000;
		count_framereset = count_frames + FPS_RESET_INTERVAL;
	}
	public abstract void dt();

	protected void setTitle(String title) {
		Container c = this.getTopLevelAncestor();
		((JFrame)c).setTitle(title);
	}
	protected void exitProgram() {
		Container c = this.getTopLevelAncestor();
		((JFrame)c).setVisible(false);
		((JFrame)c).dispose();
		System.exit(0);
	}
	//////////////////////////////////////////////////////
	//               Class Drawing Thread               //
	//////////////////////////////////////////////////////
	protected class DrawingThread extends Thread {
		SGui instance;

		public DrawingThread(SGui instance) { this.instance = instance;  }
		// Constantly update screen image
		public void run() {
			time_lastframe = System.currentTimeMillis();
			while (true) {
				instance.dt();
				instance.repaint();
				try { // Cap FPS
					// ALGORITHM DERIVATION
					// t0 = time before drawing current frame
					// tf = time after drawing current frame
					// dt = time taken to draw current frame
					//
					/////find when curfps=fpscap
					//
					// frames*1000/tf = frames*1000/(t0+dt)
					// tf = t0+dt             [dt = System.currentTimeMillis() - time_lastframe]
					//
					/////solve for tf in fpscap equation? Then use that to calculate sleep time?
					//
					// tf-t0=dt, sleep for tf-time_fpscount-System.currentTimeMillis()+time_lastframe
					//
					// count_frames*1000/fpscap = tf
					long sleeptime; //count_frames + 1 because you're going to process another frame
					sleeptime = (count_frames + 1) * 1000 / FPS_CAP - time_fpscount + time_lastframe;
					sleeptime -= System.currentTimeMillis();
					if (sleeptime > 0) {
						Thread.sleep(sleeptime);
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				time_fpscount += System.currentTimeMillis() - time_lastframe;
				time_lastframe = System.currentTimeMillis();
				count_frames++;
			}
		}
	}
}
