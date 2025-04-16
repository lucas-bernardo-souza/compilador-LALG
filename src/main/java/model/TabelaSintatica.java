/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author lucas-bernardo
 */
public class TabelaSintatica {
    Stack<String> pilha = new Stack<>();
    private List<Token> tokens;
    Map<String, Map<String, String>> tabela;
    
    public TabelaSintatica(){
        
        
    }
    
    void montarTabela(){
        tabela = new HashMap<>();
        
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of("program", "erro"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of("int", "<declaração_de_variáveis> ; <declaração_de_variáveis'>"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of("boolean", "<declaração_de_variáveis> ; <declaração_de_variáveis'>"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of(".", "ε"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of(";", "ε"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of("procedure", "ε"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of("begin", "ε"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of("identificador", "erro")); // tratar os identificadores
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of(".", "erro"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of(":", "erro"));
        tabela.put("<parte_de_declarações_de_variáveis>", Map.of(",", "erro"));
        
        tabela.put("<declaração_de_variáveis'>", Map.of("program", "erro"));
        tabela.put("<declaração_de_variáveis'>", Map.of("int", "<declaração_de_variáveis> <declaração_de_variáveis'> ;"));
        tabela.put("<declaração_de_variáveis'>", Map.of("boolean", "<declaração_de_variáveis> <declaração_de_variáveis'> ;"));
        tabela.put("<declaração_de_variáveis'>", Map.of(".", "ε"));
        tabela.put("<declaração_de_variáveis'>", Map.of(";", "ε"));
        tabela.put("<declaração_de_variáveis'>", Map.of("procedure", "ε"));
        tabela.put("<declaração_de_variáveis'>", Map.of("begin", "ε"));
        tabela.put("<declaração_de_variáveis'>", Map.of("identificador", "erro"));
        
        
        
    }
}
