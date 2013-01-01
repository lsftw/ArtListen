package musicmachine.artlisten.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ImageLoadPanel extends ImageFilePanel {
	public ImageLoadPanel(String name) {
		super(name);
	}

	public void actionPerformed(ActionEvent ae) {
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();
			try {
				setImage(ImageIO.read(selected));
				setImageFile(selected);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed to load image \"" + selected.getName() + "\". Make sure you have permission to access the file.",
						"Image Load Failure", JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, "\"" + selected.getName() + "\" is not an image.",
						"Invalid File Type", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
