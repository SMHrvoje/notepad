package org.application;

import javax.swing.*;
import java.awt.*;

public class LineNumber extends JComponent {

    private JTextArea body;

    public LineNumber(JTextArea area) {
        this.body = area;
        this.setFont(new Font("Monospaced", Font.PLAIN, 12));
        this.setPreferredSize(new Dimension(40,body.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setFont(getFont());
        g.setColor(Color.GRAY);

        int lineHeight = body.getFontMetrics(body.getFont()).getHeight();
        int startOffset = body.getInsets().top;

        int lines = body.getLineCount();
        for (int i = 0; i < lines; i++) {
            try {

                int y = (i + 1) * lineHeight + startOffset;
                g.drawString(String.valueOf(i + 1), 5, y);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(40, body.getHeight());
    }
}
