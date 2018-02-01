import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class LanguageGUI implements ActionListener {	

	private int WIDTH, HEIGHT;
	private static JButton button, button2, button3;
	private static JTextArea area, labelArea, info;
	private static JLabel welcome;
	private JFrame frame;
	private Visual vis = new Visual();

	public LanguageGUI() {
		WIDTH = 1100;
		HEIGHT = 600;
	}
	
	public void display() {
		frame = new JFrame("Language Detector");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(700, 500));
		frame.setLayout(new FlowLayout());

		vis.start();
		
		welcome = new JLabel("Having trouble figuring out the language? "
				+ "Just input text and press:");
		welcome.setFont(new Font("Comic", Font.CENTER_BASELINE, 14));
		frame.add(welcome);
		
		button = new JButton();
		button.setText("Guess Language");
		button.setFont(new Font("Comic", Font.BOLD, 12));
		button.setIcon(new ImageIcon ("world.png"));
		frame.add(button);
		button.setEnabled(true);
		button.setVisible(true);
		//the most important button - behavior in method below
		button.addActionListener(this);
		
		//space for user input
		area = new JTextArea(20, 53);
		frame.add(area);
		frame.add(new JScrollPane(area,
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		area.setVisible(true);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		
		//space for answer output
		labelArea = new JTextArea(4, 50);
		frame.add(labelArea);
		labelArea.setFont(new Font("Comic", Font.BOLD, 13));
		labelArea.setEditable(false);

		//space for some cool extra info
		info = new JTextArea("");
		frame.add(info);
		info.setVisible(false);
		
		//another button to reset
		button2 = new JButton("Clear");
		button2.setFont(new Font("Comic", Font.BOLD, 10));
		frame.add(button2);
		button2.setEnabled(true);
		button2.setVisible(true);
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				area.setText("");
				labelArea.setText("");
				vis.start();
				button3.setVisible(false);
			}
		});
		
		//a button that shows information when the mouse is hovering
		//over it.
		button3 = new JButton("Hover for more...");
		frame.add(button3);
		button3.setEnabled(true);
		button3.setVisible(false);
		button3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent event) {
				info.setVisible(true);
				button3.setEnabled(false);
			}

			public void mouseExited(MouseEvent event) {
				info.setVisible(false);
				button3.setEnabled(true);
			}
		});
		frame.setVisible(true);
	}

	//behavior of the first button
	public void actionPerformed(ActionEvent event) {

		String text = area.getText();
		double[][] test = LanguageDetector.getMatrixIn(text);

		try {
			labelArea.setText(LanguageDetector.guessLang(test));
			info.setText(LanguageDetector.label()[LanguageDetector.getSmallest(LanguageDetector.getDistanceArray(test))]);
			vis.clear();
			vis.display(LanguageDetector.getDistanceArray(test));
			button3.setVisible(true);
		} catch (FileNotFoundException e) {
			System.out.println("Language files missing!");
			e.printStackTrace();
		}
		
	}
}

