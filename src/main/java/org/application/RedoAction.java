package org.application;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

public class RedoAction extends AbstractAction implements  LocaleObserver{

    private UndoManager undoManager;
    private static ResourceBundle messages = ResourceBundleLanguages.getInstance();

    /**
     * Creates an {@code Action}.
     */
    public RedoAction(UndoManager undoManager ) {
        super(messages.getString("redo"));
        this.undoManager=undoManager;
        LocaleManager.addObserver(this);
        putValue(Action.SHORT_DESCRIPTION,messages.getString("redo_edit"));
        putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(undoManager.canRedo()){
            try{
                undoManager.redo();
            }
            catch (CannotRedoException exception){
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void updateLocale() {
        messages = ResourceBundleLanguages.getInstance();
        this.putValue(Action.NAME,messages.getString("redo"));
    }
}
