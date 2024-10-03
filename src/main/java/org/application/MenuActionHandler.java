package org.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuActionHandler implements ActionListener {

    private NotepadUI notepadUI;
    private String type;

    public MenuActionHandler(NotepadUI notepadUI, String type) {
        this.notepadUI = notepadUI;
        this.type = type;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (type){
            case "open":
                FileOperations.openFile(this.notepadUI.getbody());
                break;
            case "save":
                FileOperations.saveFIle(this.notepadUI.getbody());
                break;
        }
    }
}
