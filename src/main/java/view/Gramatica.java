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
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Torna todas as células não editáveis
                return false;
            }
        };
        
        model.addColumn("program");
        model.addColumn("int");
        model.addColumn("boolean");
        model.addColumn(".");
        model.addColumn(";");
        model.addColumn("procedure");
        model.addColumn("begin");
        model.addColumn("identificador");
        model.addColumn(":");
        model.addColumn(",");
        model.addColumn("(");
        model.addColumn(")");
        model.addColumn("var");
        model.addColumn("end");
        model.addColumn("else");
        model.addColumn("if");
        model.addColumn("while");
        model.addColumn(":=");
        model.addColumn("+");
        model.addColumn("-");
        model.addColumn("número");
        model.addColumn("not");
        model.addColumn("=");
        model.addColumn("<>");
        model.addColumn("<");
        model.addColumn("<=");
        model.addColumn("=>");
        model.addColumn(">");
        model.addColumn("then");
        model.addColumn("do");
        model.addColumn("]");
        model.addColumn("or");
        model.addColumn("*");
        model.addColumn("div");
        model.addColumn("and");
        model.addColumn("[");
        model.addColumn("$");
        
         // Preenche o modelo com os dados
        for (Map.Entry<String, Map<String, String>> entrada : dados.entrySet()) {
            String chavePrincipal = entrada.getKey();
            Map<String, String> subMapa = entrada.getValue();
            
            for (Map.Entry<String, String> subEntrada : subMapa.entrySet()) {
                model.addRow(new Object[]{
                    chavePrincipal,
                    subEntrada.getKey(),
                    subEntrada.getValue()
                });
            }
        }
        
        JTable table = new JTable(model);
        return table;
    }
}
