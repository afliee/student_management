package app.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;



public class RoundTextField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoundTextField(String text, int columns) {
		super(text, columns);
		setOpaque(false);
		setBorder(new RoundBorder());
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
		super.paintComponent(g);
	}
}

class RoundBorder extends AbstractBorder {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Color oldColor = g.getColor();
		
		g.setColor(Color.black);
		g.drawRoundRect(x, y, width - 1, height - 1,
		20, 20);
		
		g.setColor(oldColor);
	}
	
	public Insets getBorderInsets(Component c) {
		return new Insets(4, 4, 4, 4);
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = insets.top = insets.right = insets.bottom = 4;
		return insets;
	}
} 
