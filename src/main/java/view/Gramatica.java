/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bernardo
 */
public class Gramatica {
    public static JTable gramatica(Map<String, Map<String, String>> dados){
        String[] colunas = {
            "program", "int", "boolean", ".", ";", "procedure", "begin",
            "identificador", ":", ",", "(", ")", "var", "end", "else",
            "if", "while", ":=", "+", "-", "número", "not", "=", "<>",
            "<", "<=", "=>", ">", "then", "do", "]", "or", "*", "div",
            "and", "[", "$"
        };
        
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        model.addColumn("Não-terminal");
        for(String coluna : colunas){
            model.addColumn(coluna);
        }
        for(Map.Entry<String, Map<String,String>> entrada : dados.entrySet()){
            String naoTerminal = entrada.getKey();
            Map<String, String> producoes = entrada.getValue();

            Object[] linha = new Object[colunas.length + 1];

            linha[0] = naoTerminal;

            for(int i = 0; i < colunas.length; i++){
                String producao = producoes.get(colunas[i]);
                linha[i + 1] = (producao != null) ? producao : "";
            }
            model.addRow(linha);
        }


        JTable table = new JTable(model);

        return table;
    }    
}
