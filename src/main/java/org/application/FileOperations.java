package org.application;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class FileOperations {


    public static void openFile(JTextArea textArea){
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Text File", "txt");
        jfc.setFileFilter(fileNameExtensionFilter);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
        int option = jfc.showOpenDialog(null);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = jfc.getSelectedFile();
            try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                textArea.read(reader,null);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

    }
    public static void saveFIle(JTextArea textArea){
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Text File", "txt");
        jfc.setFileFilter(fileNameExtensionFilter);
        jfc.setAcceptAllFileFilterUsed(false);
        int option = jfc.showSaveDialog(null);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = jfc.getSelectedFile();
            if(!file.getName().toLowerCase().endsWith(".txt")){
                file = new File(file.getAbsolutePath()+".txt");
            }
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
                textArea.write(writer);
            }
            catch (IOException e){

            }
        }
    }
}
