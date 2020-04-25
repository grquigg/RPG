package game;

import javax.swing.*;
import java.awt.FlowLayout;

public class SwingTester extends JFrame {
	
	private JLabel item;
	
	public SwingTester() {
		super("Title");
		setLayout(new FlowLayout());
		
		item = new JLabel("This is a sentence.");
		item.setToolTipText("This is a JLabel!");
		add(item);
	}
	
	public static void main (String [] args) {
		SwingTester news = new SwingTester();
		news.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		news.setSize(275, 180);
		news.setVisible(true);
	}

}
