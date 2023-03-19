package app.ui;

import javax.swing.*;
import java.awt.*;

public class RoundedPasswordField extends JPasswordField {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public RoundedPasswordField(String text, int columns) {
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
