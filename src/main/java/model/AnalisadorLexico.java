/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Bernardo
 */
public class AnalisadorLexico {
       
    private static final String KEYWORDS = "\\b(if|else|while|for|return|int|float|char)\\b";
    private static final String IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String NUMBER = "\\d+(\\.\\d+)?";
    private static final String OPERATOR = "[+\\-*/=<>!]";
    private static final String DELIMITER = "[(){};]";
    
    private static final Pattern PATTERN = Pattern.compile(
        KEYWORDS + "|" + IDENTIFIER + "|" + NUMBER + "|" + OPERATOR + "|" + DELIMITER
    );

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(input);
        
        int linha = 1;
        int colunaAtual = 1;
        Map<Integer, Integer> mapaLinhas = new HashMap<>();
        int posicaoAtual = 0;
        
        for(int i = 0; i < input.length();i++){
            if(input.charAt(i) == '\n'){
                linha++;
                mapaLinhas.put(linha, i+1); // proximo caracter comeca uma nova linha
            }
        }
        
        linha = 1;
        
        while (matcher.find()) {
            
            String lexema = matcher.group();
            int posicaoInicio = matcher.start();
            int posicaoFim = matcher.end();
            
            // Atualiza a linha atual com base no mapa
            for(Map.Entry<Integer, Integer> entry : mapaLinhas.entrySet()){
                if(posicaoInicio >= entry.getValue()){
                    linha = entry.getKey();
                } else {
                    break;
                }
            }
            
            
            // Calcula a coluna inicial
            int colInicio = posicaoInicio - mapaLinhas.getOrDefault(linha, 0) + 1;
            int colFinal = posicaoFim - mapaLinhas.getOrDefault(linha, 0) + 1;
            String tipo;
            if (lexema.matches(KEYWORDS)) {
                tipo = "KEYWORD";
            } else if (lexema.matches(IDENTIFIER)) {
                tipo = "IDENTIFIER";
            } else if (lexema.matches(NUMBER)) {
                tipo = "NUMBER";
            } else if (lexema.matches(OPERATOR)) {
                tipo = "OPERATOR";
            } else if (lexema.matches(DELIMITER)) {
                tipo = "DELIMITER";
            } else{
                tipo = "UNKNOWN";
            }
            
            tokens.add(new Token(tipo, lexema, linha, colInicio, colFinal));
        }
        tokens.forEach(System.out::println);
        return tokens;
    }
}
