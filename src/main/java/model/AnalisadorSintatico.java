/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author saraa
 */
public class AnalisadorSintatico {

    private Stack<String> pilha = new Stack<>();
    private List<Token> tokens;
    private int ponteiro = 0;
    private Map<String, Map<String, String>> tabela;

    public AnalisadorSintatico(List<Token> tokens, Map<String, Map<String, String>> tabela) {
        this.tokens = tokens;
        this.tabela = tabela;
    }

    public void analisar() {
        pilha.push("$"); // fim da pilha
        pilha.push("<parte_de_declarações_de_variáveis>"); // símbolo inicial (ajuste conforme o uso)

        Token lookahead = tokens.get(ponteiro);

        while (!pilha.isEmpty()) {
            String topo = pilha.peek();

            if (isTerminal(topo) || topo.equals("$")) {
                if (topo.equals(lookahead.getToken())) {
                    pilha.pop();
                    ponteiro++;
                    if (ponteiro < tokens.size()) {
                        lookahead = tokens.get(ponteiro);
                    } else {
                        lookahead = new Token("$", "$", 0, 0, 0);
                    }
                } else {
                    erro("Token inesperado: " + lookahead.getLexema() + " esperado: " + topo);
                    return;
                }
            } else {
                String producao = tabela.getOrDefault(topo, Map.of()).getOrDefault(lookahead.getToken(), "erro");
                if (producao.equals("erro")) {
                    erro("Erro de sintaxe: " + lookahead.getLexema());
                    return;
                } else if (producao.equals("TOKEN_SYNC")) {
                    System.out.println("Sincronização em: " + topo);
                    pilha.pop(); // sincroniza descartando o topo
                } else if (producao.equals("ε")) {
                    pilha.pop(); // ignora epsilon
                } else {
                    pilha.pop();
                    String[] simbolos = producao.trim().split(" ");
                    for (int i = simbolos.length - 1; i >= 0; i--) {
                        if (!simbolos[i].isEmpty()) {
                            pilha.push(simbolos[i]);
                        }
                    }
                }
            }
        }

        if (lookahead.getLexema().equals("$")) {
            System.out.println("Análise sintática concluída com sucesso!");
        } else {
            erro("Tokens restantes após análise.");
        }
    }

    private boolean isTerminal(String simbolo) {
        return !simbolo.startsWith("<") && !simbolo.equals("ε");
    }

    private void erro(String msg) {
        System.err.println(msg);
    }
}
