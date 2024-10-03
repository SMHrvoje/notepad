package org.application;

import javax.swing.*;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

public class UndoAction extends AbstractAction implements LocaleObserver{

    private UndoManager undoManager;
    private static ResourceBundle messages= ResourceBundleLanguages.getInstance();

    /**
     * Creates an {@code Action} with the specified name.
     *
     */
    public UndoAction(UndoManager undoManager) {
        super(messages.getString("undo"));
        this.undoManager = undoManager;
        LocaleManager.addObserver(this);
        putValue(Action.SHORT_DESCRIPTION,messages.getString("undo_edit"));
        putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(undoManager.canUndo()){
            try {
                undoManager.undo();
            }
            catch (CannotUndoException exception){
                exception.printStackTrace();
        }

        }
    }

    @Override
    public void updateLocale() {
        messages = ResourceBundleLanguages.getInstance();
        putValue(Action.NAME,messages.getString("undo"));
    }
}
