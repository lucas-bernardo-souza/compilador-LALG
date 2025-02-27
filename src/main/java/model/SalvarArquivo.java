/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Bernardo
 */
public class SalvarArquivo {
    public static void salvarTexto(String conteudo){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Arquivo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos de Texto (*.txt)", "txt"));
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if(userSelection == JFileChooser.APPROVE_OPTION){
            File arquivo = fileChooser.getSelectedFile();
            
            if(!arquivo.getAbsolutePath().endsWith(".txt")){
                arquivo = new File(arquivo.getAbsolutePath()+".txt");
            }
            
            try (FileWriter writer = new FileWriter(arquivo)) {
                writer.write(conteudo);
                JOptionPane.showMessageDialog(null, "Arquivo salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
