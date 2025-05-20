package Presentation;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel that displays a background image stretched to fit the panel size.
 * <p>
 * This panel is typically used as a decorative background in GUI windows,
 * providing visual style without interfering with the layout of other components.
 * </p>
 */

class BackgroundPanel extends JPanel {
    private Image bgImage;

    public BackgroundPanel(Image bgImage) {
        this.bgImage = bgImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}