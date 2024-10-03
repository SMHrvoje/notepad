package org.application;

import javax.imageio.IIOException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimerTask;


public class NotepadUI {

    private JFrame frame;
    private JTextArea body;
    private JMenuBar menuBar;
    private UndoManager undoManager;
    private LineNumber lineNumber;
    private java.util.Timer timer;
    private ResourceBundle messages;

    public NotepadUI(JFrame frame) {

        messages= ResourceBundleLanguages.getInstance();

        this.frame = frame;
        this.undoManager = new UndoManager();

        this.frame.setTitle(messages.getString("title"));
        this.frame.setSize(1000,600);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());
        this.body = new JTextArea();
        this.lineNumber = new LineNumber(this.body);
        JScrollPane scrollPane = new JScrollPane(body);
        scrollPane.setRowHeaderView(lineNumber);
        JPanel statusPanel = new JPanel();
        this.frame.add(statusPanel,BorderLayout.SOUTH);

        setupJMenu();
        setupStatusBar(statusPanel);

        this.body.getDocument().addDocumentListener(new DocumentListener() {
            /**
             * Gives notification that there was an insert into the document.  The
             * range given by the DocumentEvent bounds the freshly inserted region.
             *
             * @param e the document event
             */
            @Override
            public void insertUpdate(DocumentEvent e) {
                lineNumber.repaint();
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
                lineNumber.repaint();
            }

            /**
             * Gives notification that an attribute or set of attributes changed.
             *
             * @param e the document event
             */
            @Override
            public void changedUpdate(DocumentEvent e) {
                lineNumber.repaint();
            }
        });

        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            /**
             * Invoked when the value of the adjustable has changed.
             *
             * @param e the event to be processed
             */
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                lineNumber.repaint();
            }
        });

        this.body.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        this.frame.add(scrollPane, BorderLayout.CENTER);
        this.frame.setLocationRelativeTo(null);
    }

    public void setupJMenu(){
        JMenuBar menuBar = new JMenuBar();

        LocalizedJMenu menu = new LocalizedJMenu("file");

        LocalizedJMenuItem item1 = new LocalizedJMenuItem("open");
        item1.addActionListener(new MenuActionHandler(this,"open"));
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        LocalizedJMenuItem item2 = new LocalizedJMenuItem("save");
        item2.addActionListener(new MenuActionHandler(this,"save"));
        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        JCheckBoxMenuItem item4 = new JCheckBoxMenuItem(messages.getString("autosave"));
        item4.addActionListener(e-> toggleAutoSave(item4.isSelected()));
        LocalizedJMenuItem item5 = new LocalizedJMenuItem("print");
        item5.addActionListener(e->printText());
        LocalizedJMenuItem item3 = new LocalizedJMenuItem("exit");
        item3.addActionListener((e)->System.exit(0));
        menu.add(item1);
        menu.add(item2);
        menu.add(item3);
        menu.add(item4);
        menu.add(item5);
        menuBar.add(menu);

        LocalizedJMenu menu1 = new LocalizedJMenu("edit");
        JMenuItem menuItem1 = new JMenuItem(new UndoAction(this.undoManager));
        JMenuItem menuItem2 = new JMenuItem(new RedoAction(this.undoManager));
        JMenuItem menuItem3 = new JMenuItem(new FindReplaceAction(this.frame,this.body));

        menu1.add(menuItem1);
        menu1.add(menuItem2);
        menu1.add(menuItem3);
        menuBar.add(menu1);


        LocalizedJMenu formatMenu = new LocalizedJMenu("format");
        JMenuItem formatMenu1 = new JMenuItem("3");
        JMenuItem formatMenu2 = new JMenuItem("3");
        JMenuItem formatMenu3 = new JMenuItem("3");
        formatMenu.add(formatMenu1);
        formatMenu.add(formatMenu2);
        formatMenu.add(formatMenu3);
        menuBar.add(formatMenu);

        LocalizedJMenu viewMenu = new LocalizedJMenu("view");
        JMenuItem viewMenu1 = new JMenuItem(messages.getString("font_styling"));
        viewMenu1.addActionListener((e)->showFontDialog());
        LocalizedJMenu viewMenu2 = new LocalizedJMenu("language");
        JMenuItem eng = new JMenuItem(messages.getString("english"));
        JMenuItem de = new JMenuItem(messages.getString("german"));
        JMenuItem fr = new JMenuItem(messages.getString("french"));
        viewMenu2.add(eng);
        viewMenu2.add(de);
        viewMenu2.add(fr);
        eng.addActionListener(e->LocaleManager.changeLocale(Locale.ENGLISH));
        de.addActionListener(e->LocaleManager.changeLocale(Locale.GERMAN));
        fr.addActionListener(e->LocaleManager.changeLocale(Locale.FRANCE));
        JMenuItem viewMenu3 = new JMenuItem("3");
        viewMenu.add(viewMenu1);
        viewMenu.add(viewMenu2);
        viewMenu.add(viewMenu3);
        menuBar.add(viewMenu);


        this.frame.setJMenuBar(menuBar);
    }

    public void setupStatusBar(JPanel statusPanel){
        new StatusBar(statusPanel,this.body);
    }

    private void toggleAutoSave(boolean selected){
        if(selected){
            startAutoSave();
        }
        else{
            stopAutoSave();
        }
    }
    private void startAutoSave(){
        if(timer==null){
            timer = new java.util.Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    autoSave();
                }
            },0,300000);
        }
    }
    private void stopAutoSave(){
    if(timer!=null){
        timer.cancel();
        timer=null;
        JOptionPane.showMessageDialog(frame,messages.getString("autosave_activated"));
    }
    }

    private void autoSave(){
        try(BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/autosave.txt"))){
            bufferedWriter.write(body.getText());
            System.out.printf("Autosaved at %s\n",LocalDateTime.now().toString());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public JTextArea getbody() {
        return this.body;
    }
    public void showFontDialog(){
        FontDialog fontDialog = new FontDialog(this.frame,this.body,messages.getString("font_customization"));
        fontDialog.setVisible(true);
    }

    private void printText(){
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if(pageIndex > 0){
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(),pageFormat.getImageableY());

                body.printAll(g2d);
                return PAGE_EXISTS;
            }
        });

        boolean canPrint = printJob.printDialog();
        if(canPrint){
            try{
                printJob.print();
            }
            catch (PrinterException e){
                e.printStackTrace();
            }
        }
    }

    public void showScreen(){
        //this.frame.pack();
        this.frame.setVisible(true);
    }
}
