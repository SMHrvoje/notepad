package org.application;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ResourceBundle;

public class StatusBar {

    private JPanel statusPanel;
    private JTextArea textArea;
    private JLabel empty;
    private JLabel firstLabel;
    private JLabel secondLabel;
    private JLabel thirdLabel;
    private ResourceBundle messages;

    public StatusBar(JPanel panel,JTextArea textArea){
        messages= ResourceBundleLanguages.getInstance();
        this.statusPanel=panel;
        this.textArea=textArea;
        statusPanel.setLayout(new GridBagLayout());
        firstLabel = new JLabel(String.format("%s: 1, %s: 1",messages.getString("line"),messages.getString("column")));
        empty = new JLabel("");
        secondLabel = new JLabel("100%");
        thirdLabel = new JLabel("Windows (CRLF)");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx=0.4;
        statusPanel.add(empty,gbc);
        gbc.gridx = 1;
        gbc.weightx=0.2;
        statusPanel.add(firstLabel,gbc);
        gbc.gridx = 2;
        gbc.weightx=0.2;
        statusPanel.add(secondLabel,gbc);
        gbc.gridx = 3;
        gbc.weightx=0.2;
        statusPanel.add(thirdLabel,gbc);
        this.textArea.addCaretListener(new CaretListener() {
            /**
             * Called when the caret position is updated.
             *
             * @param e the caret event
             */
            @Override
            public void caretUpdate(CaretEvent e) {
                updateStatus();
            }
        });
        this.textArea.getDocument().addDocumentListener(new DocumentListener() {
            /**
             * Gives notification that there was an insert into the document.  The
             * range given by the DocumentEvent bounds the freshly inserted region.
             *
             * @param e the document event
             */
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStatus();
            }

            /**
             * Gives notification that a portion of the document has been
             * removed.  The range is given in terms of what the view last
             * saw (that is, before updating sticky positions).
             *
             * @param e the document event
             */
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStatus();
            }

            /**
             * Gives notification that an attribute or set of attributes changed.
             *
             * @param e the document event
             */
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStatus();
            }
        });
    }



    public void updateStatus(){
        int carePos = textArea.getCaretPosition();

        try{
            int row = textArea.getLineOfOffset(carePos)+1;
            int col =carePos - textArea.getLineStartOffset(row-1)+1;

            this.firstLabel.setText(String.format("%s: %d, %s: %d", messages.getString("line"),row,messages.getString("column"),col));


        }
        catch(Exception e){

        }
    }
}
