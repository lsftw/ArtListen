package musicmachine.artlisten.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ImagePreview extends JComponent {
	protected BufferedImage image;

	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	// scale to fill, bicubic
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (image == null) return;

		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		int tw = this.getWidth();
		int th = this.getHeight();
		int iw = image.getWidth();
		int ih = image.getHeight();
		double scaleX = (double)tw/iw;
		double scaleY = (double)th/ih;
		double scale = Math.max(scaleX, scaleY);

		if (scale > 1) scale = 1;

		int newWidth = (int)(scale*iw);
		int newHeight = (int)(scale*ih);
		int newX = (tw - newWidth)/2;
		int newY = (th - newHeight)/2;

		g2.drawImage(image, newX, newY, newWidth, newHeight, this);
	}
}
