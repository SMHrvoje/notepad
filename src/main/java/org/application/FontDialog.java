package org.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class FontDialog extends JDialog {

    private JFrame frame;
    private JTextArea body;
    private JComboBox<String> fontComboBox;
    private JComboBox<String> styleComboBox;
    private JSpinner spinner;
    private ResourceBundle messages;

    /**
     * Creates a modeless dialog without a title and without a specified
     * {@code Frame} owner.  A shared, hidden frame will be
     * set as the owner of the dialog.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by {@code JComponent.getDefaultLocale}.
     * <p>
     * NOTE: This constructor does not allow you to create an unowned
     * {@code JDialog}. To create an unowned {@code JDialog}
     * you must use either the {@code JDialog(Window)} or
     * {@code JDialog(Dialog)} constructor with an argument of
     * {@code null}.
     *
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()}
     *                           returns {@code true}.
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public FontDialog(JFrame frame,JTextArea body,String title) {
        super(frame,title,true);
        ResourceBundle bundle = ResourceBundleLanguages.getInstance();
        this.frame = frame;
        this.body = body;

        this.setLayout(new GridLayout(4,2));

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fonts);
        add(new JLabel(messages.getString("text")));
        this.add(fontComboBox);
        this.setSize(400,200);
        this.setLocationRelativeTo(frame);

        String[] styles = {messages.getString("plain"),messages.getString("bold"),messages.getString("italic")};
        styleComboBox = new JComboBox<>(styles);
        add(new JLabel(messages.getString("style")));
        this.add(styleComboBox);

        spinner = new JSpinner(new SpinnerNumberModel(14,4,72,1));
        add(new JLabel(messages.getString("size")));
        add(spinner);

        JButton okButton = new JButton(messages.getString("apply"));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFont();
            }
        });
        add(okButton);
        JButton cancelButton = new JButton(messages.getString("cancel"));
        cancelButton.addActionListener((event)->this.setVisible(false));
        add(cancelButton);
    }

    private void applyFont(){
        body.setFont(new Font((String)fontComboBox.getSelectedItem(),styleComboBox.getSelectedIndex(),(int)spinner.getValue()));
        setVisible(false);
    }
}
