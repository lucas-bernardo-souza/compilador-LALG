/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author bernardo.souza_unesp
 */
public class TabelaSintatica extends javax.swing.JFrame {

    /**
     * Creates new form TabelaSintatica
     * @param tabela
     */
    public TabelaSintatica(Map<String, Map<String, String>> tabela) {
        initComponents();
        
        // Criar o modelo de tabela
        DefaultTableModel model = new DefaultTableModel();
        
        if (!tabela.isEmpty()) {
            Map<String, String> primeiroItem = tabela.values().iterator().next();
            model.addColumn("Chave Principal"); // Coluna para a chave do map externo
            
            // Adicionar colunas para as chaves do map interno
            for (String chaveInterna : primeiroItem.keySet()) {
                model.addColumn(chaveInterna);
            }
            
            // Preencher as linhas
            for (Map.Entry<String, Map<String, String>> entrada : tabela.entrySet()) {
                String chavePrincipal = entrada.getKey();
                Map<String, String> valores = entrada.getValue();
                
                Object[] linha = new Object[valores.size() + 1];
                linha[0] = chavePrincipal;
                
                int i = 1;
                for (String valor : valores.values()) {
                    linha[i++] = valor;
                }
                
                model.addRow(linha);
            }
        }
        
        // Criar a tabela com o modelo
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Configurar layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        setLocationRelativeTo(null); // Centralizar na tela
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
