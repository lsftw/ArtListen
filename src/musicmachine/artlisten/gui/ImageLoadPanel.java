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

	public void loadImageFile(File file) {
		try {
			setImage(ImageIO.read(file));
			setImageFile(file);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to load image \"" + file.getName() + "\". Make sure you have permission to access the file.",
					"Image Load Failure", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, "\"" + file.getName() + "\" is not an image.",
					"Invalid File Type", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			loadImageFile(chooser.getSelectedFile());
		}
	}
}
