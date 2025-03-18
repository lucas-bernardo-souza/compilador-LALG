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
    private static final String NUMBER = "-?\\d*\\.\\d+";
    private static final String NUMBERINT = "-?\\d+";
    private static final String OPERADORSOMA = "\\+";
    private static final String OPERADORSUBTRACAO = "\\-";
    private static final String OPERADORDIVISAO = "\\/";
    private static final String OPERADORMULTIPLICACAO = "\\*";
    private static final String DELIMITER = "[()]";
    private static List<String> fonte;
    
    private static final Pattern PATTERN = Pattern.compile(
        KEYWORDS + "|" + IDENTIFIER + "|" + NUMBER + "|" + OPERADORSOMA +"|" + OPERADORSUBTRACAO + "|" +
                OPERADORDIVISAO + "|" + OPERADORMULTIPLICACAO + "|" + DELIMITER + "|" + NUMBERINT
    );
    
    public static String eliminaComentarios(String fonte, Erro erroEncontrado){
        
        // remover coment´rios de linha
        StringBuilder filtrado = new StringBuilder();
        String[] linhas = fonte.split("\n");
        for(String linha : linhas){
            int indiceComentario = linha.indexOf("//");
            if(indiceComentario != -1){
                linha = linha.substring(0, indiceComentario);
            }
            filtrado.append(linha).append("\n");
        }
        
        String temp = filtrado.toString();
        StringBuilder resultadoFinal = new StringBuilder();
        
        // Remover texto entre chaves {}
        int abreChaves = temp.indexOf('{');
        int fechaChaves = temp.lastIndexOf('}');
        
        if(abreChaves != -1 && fechaChaves == -1){
            erroEncontrado.setNome("Comentário sem fechamento");
            erroEncontrado.setFase("Léxica");
            erroEncontrado.setDescricao("Não foi possível encontrar o final do arquivo");
            erroEncontrado.setLinha(abreChaves);
            erroEncontrado.setColuna(0);
            return fonte;
        }
        
        boolean dentroDeChaves = false;
        for(char c : temp.toCharArray()){
            if(c == '{'){
                dentroDeChaves = true;
            } else if (c == '}'){
                dentroDeChaves = false;
            } else if(!dentroDeChaves){
                resultadoFinal.append(c);
            }
        }
        
        
        return resultadoFinal.toString().trim();
        
    }

    public static List<Token> tokenize(String codFonte) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(codFonte);
        
        int linha = 1;
        Map<Integer, Integer> mapaLinhas = new HashMap<>();
        
        for(int i = 0; i < codFonte.length();i++){
            if(codFonte.charAt(i) == '\n'){
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
            if (lexema.matches(NUMBER)) {
                tipo = "NÚMERO REAL";
            } else if(lexema.matches(NUMBERINT)){
                tipo = "NÚMERO INTEIRO";
            } else if (lexema.matches(OPERADORSOMA)) {
                tipo = "OPERADOR DE SOMA";
            } else if(lexema.matches(OPERADORSUBTRACAO)){
                tipo = "OPERADOR DE SUBTRAÇÃO";
            } else if(lexema.matches(OPERADORMULTIPLICACAO)){
                tipo = "OPERADOR DE MULTIPLICAÇÃO";
            } else if(lexema.matches(OPERADORDIVISAO)){
                tipo = "OPERADOR DE DIVISÃO";
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
    
    public static List<Erro> buscaErrosLexicos(List<Token> tokens){
        List<Erro> errosEncontrados = new ArrayList<>();
        for(Token token : tokens){
            System.out.println(token.getToken());
            if("UNKNOWN".equals(token.getToken())){
                Erro erroAlfabeto = new Erro("Caracter fora do alfabeto", "Léxica", 
                        "O caracter: " + token.getLexema()+ ", não pertence ao alfabeto da linguagem.",
                        token.getLinha(), token.getColunaInicial());
                errosEncontrados.add(erroAlfabeto);
            }
            if("NUMBER".equals(token.getToken())){
                String lexema = token.getLexema();
                if(lexema.contains("..") || lexema.contains(",")){
                    Erro erroNumero = new Erro("Numero mal formado", "Léxica",
                            "O número real: " + token.getLexema()+ ", foi mal formulado.",
                            token.getLinha(), token.getColunaInicial());
                    errosEncontrados.add(erroNumero);
                }
            }
        }
        return errosEncontrados;
    }
}
