package musicmachine.artlisten.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public abstract class ImageFilePanel extends JPanel implements ActionListener {
	protected static final Border LINE_BORDER = new LineBorder(Color.BLACK);
	protected static final String[] SUPPORTED_FORMATS = ImageIO.getReaderFormatNames();

	protected JFileChooser chooser = new JFileChooser();
	protected FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", SUPPORTED_FORMATS);

	protected String name;
	protected File imageFile;
	protected BufferedImage image;
	protected ImagePreview imagePreview = new ImagePreview();
	protected JTextField imagePath = new JTextField();
	protected JButton selectPath = new JButton("...");

	protected List<FileChosenListener> listeners = new ArrayList<FileChosenListener>();

	public ImageFilePanel(String name) {
		super(new BorderLayout());
		this.name = name;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupGui();
			}
		});
	}

	protected void setupGui() {
		image = null;
		imagePreview.setImage(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(new TitledBorder(LINE_BORDER, capitalize(name) + " Image"));
		mainPanel.add(imagePreview, BorderLayout.CENTER);

		JPanel botPanel = new JPanel();
		botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.LINE_AXIS));
		imagePath.setText("Select " + name + " image");
		imagePath.setEditable(false);
		botPanel.add(imagePath);
		selectPath.addActionListener(this);
		botPanel.add(selectPath);
		mainPanel.add(botPanel, BorderLayout.SOUTH);

		this.add(mainPanel);

		chooser.setFileFilter(filter);
	}

	private static String capitalize(String text) {
		if (text.length() > 0) {
			char c = text.charAt(0);
			if (c >= 'a' && c <= 'z') {
				c = (char)(c - 'a' + 'A');
			}
			return c + text.substring(1);
		}
		return text;
	}

	public File getImageFile() {
		return imageFile;
	}
	public boolean hasFileSelected() {
		return imageFile != null;
	}
	public void setImageFile(File file) {
		imageFile = file;
		imagePath.setText(file.getPath());
		alertFileChosenListeners(file);
	}
	public void addFileChosenListener(FileChosenListener listener) {
		listeners.add(listener);
	}
	protected void alertFileChosenListeners(File file) {
		for (FileChosenListener listener : listeners) {
			listener.fileChosen(file);
		}
	}
	public void setCurrentDirectory(File dir) {
		chooser.setCurrentDirectory(dir);
	}
	public Image getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
		imagePreview.setImage(image);
		imagePreview.repaint();
	}

	public abstract void actionPerformed(ActionEvent ae);
}
