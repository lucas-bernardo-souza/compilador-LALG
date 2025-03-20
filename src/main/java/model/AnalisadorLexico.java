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
       
    //private static final String KEYWORDS = "\\b(if|else|while|for|return|int|float|char)\\b";
    //private static final String NUMBER = "-?\\d*\\.\\d+";
    //private static final String DELIMITER = "[()]";
    private static final String finalDeComando = "\\;";
    private static final String atribuicaDeVariavel = "\\:=";
    
    private static final String palavraReservadaProgram = "\\b(program)\\b";
    private static final String palavraReservadaProcedure = "\\b(procedure)\\b";
    private static final String palavraReservadaVar = "\\b(var)\\b";
    private static final String palavraReservadaBegin = "\\b(begin)\\b";
    private static final String palavraReservadaEnd = "\\b(end)\\b";
    private static final String palavraReservadaIf = "\\b(if)\\b";
    private static final String palavraReservadaThen = "\\b(then)\\b";
    private static final String palavraReservadaElse = "\\b (else)\\b";
    private static final String palavraReservadaWhile = "\\b(while)\\b";
    private static final String palavraReservadaDo = "\\b(do)\\b";
    
    private static final String sinalIgualdade = "\\=";
    private static final String sinalMaior = "\\>";
    private static final String sinalMenor = "\\<";
    private static final String sinalDiferente = "\\<>";
    private static final String sinalMenorIgual = "\\<=";
    private static final String sinalMaiorIgual = "\\>=";
    private static final String operadorSoma = "\\+";
    private static final String operadorSubtracao = "\\-";
    private static final String operadorMultiplicacao = "\\*";
    private static final String operadorDivisao = "\\b(div)\\b";
    private static final String operadorLogicoOr = "\\b(or)\\b";
    private static final String operadorLogicoAnd = "\\b(and)\\b";
    private static final String operadorLogicoNot = "\\b(not)\\b";
    
    private static final String numeroInteiro = "-?\\d+";
    
    private static final String identificador = "[a-zA-Z_][a-zA-Z0-9_]*";
    
    private static final String tipoDeDadoInt = "\\b(int)\\b";
    private static final String tipoDeDadoBoolean = "\\b(boolean)\\b";
    
    private static List<String> fonte;
    
    private static final Pattern PATTERN = Pattern.compile(
        palavraReservadaBegin + "|" + palavraReservadaEnd + "|" + palavraReservadaIf + "|" + 
                palavraReservadaThen + "|" + palavraReservadaElse + "|" + palavraReservadaWhile + "|" +
                palavraReservadaDo + "|" + sinalIgualdade + "|" + sinalMaior + "|" + sinalMenor + "|" +
                sinalDiferente + "|" + sinalMenorIgual + "|" + sinalMaiorIgual + "|" + operadorSoma + "|" +
                operadorSubtracao + "|" + operadorMultiplicacao + "|" + operadorDivisao + "|" +
                numeroInteiro + "|" + finalDeComando + "|" + atribuicaDeVariavel + "|" + 
                palavraReservadaProgram + "|" + palavraReservadaProcedure + "|" + palavraReservadaVar + "|" +
                tipoDeDadoInt + "|" + tipoDeDadoBoolean + "|" +
                operadorLogicoOr + "|" + operadorLogicoAnd + "|" + operadorLogicoNot + "|" + identificador 
                
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
            if (lexema.matches(palavraReservadaBegin)) {
                tipo = "PALAVRA RESERVADA 'BEGIN'";
            } else if(lexema.matches(palavraReservadaEnd)){
                tipo = "PALAVRA RESERVADA 'END'";
            } else if(lexema.matches(palavraReservadaIf)){
                tipo = "PALAVRA RESERVADA 'IF'";
            } else if(lexema.matches(palavraReservadaThen)){
                tipo = "PALAVRA RESERVADA 'THEN'";
            } else if(lexema.matches(palavraReservadaElse)){
                tipo = "PALAVRA RESERVADA 'ELSE'";
            } else if(lexema.matches(palavraReservadaWhile)){
                tipo = "PALAVRA RESERVADA 'WHILE'";
            } else if(lexema.matches(palavraReservadaDo)){
                tipo = "PALAVRA RESERVADA 'DO'";
            } else if(lexema.matches(sinalIgualdade)){
                tipo = "SINAL DE IGUALDADE";
            } else if(lexema.matches(sinalMaior)){
                tipo = "SINAL DE MAIOR";
            } else if(lexema.matches(sinalMenor)){
                tipo = "SINAL DE MENOR";
            } else if(lexema.matches(sinalDiferente)){
                tipo = "SINAL DE DIFERENTE";
            } else if(lexema.matches(sinalMenorIgual)){
                tipo = "SINAL DE MENOR IGUAL";
            } else if(lexema.matches(sinalMaiorIgual)){
                tipo = "SINAL DE MAIOR IGUAL";
            } else if(lexema.matches(operadorSoma)){
                tipo = "OPERADOR DE SOMA";
            } else if(lexema.matches(operadorSubtracao)){
                tipo = "OPERADOR DE SUBTRACAO";
            } else if(lexema.matches(operadorMultiplicacao)){
                tipo = "OPERADOR DE MULTIPLICACAO";
            } else if(lexema.matches(operadorDivisao)){
                tipo = "OPERADOR DE DIVISAO";
            } else if(lexema.matches(operadorLogicoOr)){
                tipo = "OPERADOR LOGICO 'OR'";
            } else if(lexema.matches(operadorLogicoAnd)){
                tipo = "OPERADOR LOGICO 'AND'";
            } else if(lexema.matches(operadorLogicoNot)){
                tipo = "OPERADOR LOGICO NOT";
            } else if(lexema.matches(numeroInteiro)){
                tipo = "NUMERO INTEIRO";
            } else if(lexema.matches(tipoDeDadoBoolean)){
                tipo = "TIPO DE DADO 'BOOLEANO'";
            } else if(lexema.matches(finalDeComando)){
                tipo = "FINAL DO COMANDO";
            } else if(lexema.matches(atribuicaDeVariavel)){
                tipo = "ATRIBUIÇÃO DE VARIÁVEL";
            } else if(lexema.matches(palavraReservadaProgram)){
                tipo = "PALAVRA RESERVADA 'PROGRAM'";
            } else if(lexema.matches(palavraReservadaProcedure)){
                tipo = "PALAVRA RESERVADA 'PROCEDURE'";
            } else if(lexema.matches(palavraReservadaVar)){
                tipo = "PALAVRA RESERVADA 'VAR'";
            } else if(lexema.matches(tipoDeDadoInt)){
                tipo = "TIPO DE DADO 'INTEIRO'";
            } else if(lexema.matches(identificador)){
                tipo = "IDENTIFICADOR";
            }
            else{
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
