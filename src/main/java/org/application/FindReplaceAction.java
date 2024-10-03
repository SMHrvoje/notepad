package org.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

public class FindReplaceAction extends AbstractAction  implements LocaleObserver{

    private JFrame frame;
    private JTextArea body;
    private Dialog dialog;
    private static ResourceBundle messages = ResourceBundleLanguages.getInstance();

    /**
     * Creates an {@code Action}.
     */
    public FindReplaceAction(JFrame frame,JTextArea body) {
        super(messages.getString("find_replace"));
        this.frame = frame;
        this.body = body;
        LocaleManager.addObserver(this);
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dialog = new JDialog();
        dialog.setModal(false);
        dialog.setLocationRelativeTo(null);
        dialog.setTitle(messages.getString("find_replace"));
        dialog.setSize(400,200);
        dialog.setLayout(new GridLayout(3,2));

        JLabel findLabel = new JLabel(messages.getString("find"));
        findLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField findField = new JTextField();
        findField.setToolTipText(messages.getString("text_found"));
        JLabel replaceLabel = new JLabel(messages.getString("replace"));
        replaceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField replaceField = new JTextField();
        replaceField.setToolTipText(messages.getString("text_replace"));
        dialog.add(findLabel);
        dialog.add(findField);
        dialog.add(replaceLabel);
        dialog.add(replaceField);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setLayout(new GridLayout(1,4));
        panel2.setLayout(new GridLayout(1,4));
        JButton nextButton = new JButton(messages.getString("find_next"));
        JButton replaceButton = new JButton(messages.getString("replace"));
        JButton replaceAllButton = new JButton(messages.getString("replace_all"));
        JButton cancelButton = new JButton(messages.getString("cancel"));
        panel1.add(nextButton);
        panel1.add(replaceButton);
        panel2.add(replaceAllButton);
        panel2.add(cancelButton);
        dialog.add(panel1);
        dialog.add(panel2);

        nextButton.addActionListener(e1 -> nextText(findField.getText()));
        replaceButton.addActionListener(e1 -> replaceText(findField.getText(),replaceField.getText()));
        replaceAllButton.addActionListener(e1 -> replaceAllText(findField.getText(),replaceField.getText()));
        cancelButton.addActionListener(e1 -> dialog.setVisible(false));

        dialog.setVisible(true);
    }
    private void nextText(String text) {
        String bodyText = body.getText();
        int caretPosition = body.getCaretPosition();
        int startPosition = bodyText.indexOf(text);
        boolean exists = false;
        if(body.getSelectedText()!= null && body.getSelectedText().equals(text)){
            exists = true;
            caretPosition = body.getSelectionEnd();
            startPosition = bodyText.indexOf(text,caretPosition);
        }


        if(startPosition != -1){
            body.setCaretPosition(startPosition);
            body.select(startPosition, startPosition+text.length());
        }
        else if(exists){
            body.setCaretPosition(0);
            nextText(text);
        }
        else{
            JOptionPane.showMessageDialog(dialog,messages.getString("no_occurances"));
        }
    }
    private void replaceText(String text, String replaceText) {
        if(body.getSelectedText()!=null && body.getSelectedText().equals(text)){
           body.replaceSelection(replaceText);
        }
        nextText(text);
    }
    private void replaceAllText(String text, String replaceAllText) {
        body.setText(body.getText().replace(text,replaceAllText));
    }

    @Override
    public void updateLocale() {
        messages = ResourceBundleLanguages.getInstance();
        putValue(Action.NAME, messages.getString("find_replace"));
    }
}
