package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Analisador sintático - com passo a passo e erros formatados usando a classe
 * Erro
 *
 * @author saraa
 */
public class AnalisadorSintatico {

    private Stack<String> pilha = new Stack<>();
    private List<Token> tokens;
    private Map<String, Map<String, String>> tabela;
    private int ponteiro = 0;
    private List<PassoSintatico> passos = new ArrayList<>();
    private List<Erro> listaErros = new ArrayList<>();

    public AnalisadorSintatico(List<Token> tokens, TabelaSintatica tabelaSintatica) {
        this.tokens = tokens;
        this.tabela = tabelaSintatica.tabela;
    }

    public List<PassoSintatico> getPassos() {
        return passos;
    }

    public List<Erro> getListaErros() {
        return listaErros;
    }

    public void analisar() {
        pilha.push("$");
        pilha.push("<programa>");

        Token lookahead = tokens.get(ponteiro);

        while (!pilha.isEmpty()) {
            String topo = pilha.peek();

            if (isTerminal(topo) || topo.equals("$")) {
                if (topo.equals(lookahead.getLexema())) {
                    passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Consome token"));
                    pilha.pop();
                    ponteiro++;
                    if (ponteiro < tokens.size()) {
                        lookahead = tokens.get(ponteiro);
                    } else {
                        lookahead = new Token("$", "$", 0, 0, 0);
                    }
                } else {
                    Erro erro = new Erro(
                            "Token inesperado",
                            "Sintática",
                            "Esperava '" + topo + "', mas encontrou '" + lookahead.getLexema() + "'",
                            lookahead.getLinha(),
                            lookahead.getColunaInicial()
                    );
                    listaErros.add(erro);
                    passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Erro: esperava '" + topo + "'"));
                    pilha.pop(); // Regra: remove o topo
                }
            } else {
                if (topo.equals("<identificador>") && lookahead.getToken().equals("IDENTIFICADOR")) {
                    passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Consome token"));
                    pilha.pop();
                    ponteiro++;
                    if (ponteiro < tokens.size()) {
                        lookahead = tokens.get(ponteiro);
                    } else {
                        lookahead = new Token("$", "$", 0, 0, 0);
                    }
                } else {
                    Map<String, String> producoes = tabela.get(topo);
                    if (producoes == null) {
                        Erro erro = new Erro(
                                "Não-terminal desconhecido",
                                "Sintática",
                                "Não existe produção para '" + topo + "'",
                                lookahead.getLinha(),
                                lookahead.getColunaInicial()
                        );
                        listaErros.add(erro);
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Erro: Não existe produção para '" + topo + "'"));
                        pilha.pop();
                        continue;
                    }
                    String producao;
                    if (lookahead.getToken() == "IDENTIFICADOR") {
                        producao = producoes.get("IDENTIFICADOR");
                    } else {
                        producao = producoes.get(lookahead.getLexema());
                    }
                    if (producao == null) {
                        Erro erro = new Erro(
                                "Produção ausente",
                                "Sintática",
                                "Nenhuma produção para '" + topo + "' com lookahead '" + lookahead.getLexema() + "'",
                                lookahead.getLinha(),
                                lookahead.getColunaInicial()
                        );
                        listaErros.add(erro);
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")",   "Erro: Nenhuma produção para '" + topo + "' com lookahead '" + lookahead.getLexema() + "'"));

                        ponteiro++;
                        if (ponteiro < tokens.size()) {
                            lookahead = tokens.get(ponteiro);
                        } else {
                            lookahead = new Token("$", "$", 0, 0, 0);
                        }
                    } else if (producao.equals("erro")) {
                        Erro erro = new Erro(
                                "Erro grave",
                                "Sintática",
                                "Produção inválida para '" + topo + "' com token '" + lookahead.getLexema() + "'",
                                lookahead.getLinha(),
                                lookahead.getColunaInicial()
                        );
                        listaErros.add(erro);
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Erro: Produção inválida para '" + topo + "' com token '" + lookahead.getLexema() + "'"));
                        ponteiro++;
                        if (ponteiro < tokens.size()) {
                            lookahead = tokens.get(ponteiro);
                        } else {
                            lookahead = new Token("$", "$", 0, 0, 0);
                        }
                    } else if (producao.equals("sinc")) {
                        Erro erro = new Erro(
                                "Sincronização",
                                "Sintática",
                                "Sincronizando ao remover '" + topo + "'",
                                lookahead.getLinha(),
                                lookahead.getColunaInicial()
                        );
                        
                        listaErros.add(erro);
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Sincronizando ao remover '" + topo + "'"));

                        pilha.pop();
                    } else if (producao.equals("ε")) {
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", topo + " → ε"));
                        pilha.pop();
                    } else {
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Expande: " + topo + " → " + producao));
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
            if (pilha.peek().equals("<parte_de_declarações_de_sub-rotinas>")) {
                break;
            }
        }

        if (listaErros.isEmpty()) {
            passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Análise sintática finalisada com sucesso!"));

        } else {
            passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Análise sintática finalizada com " + listaErros.size() + " erro(s)."));
        }
    }

    private boolean isTerminal(String simbolo) {
        return !simbolo.startsWith("<") && !simbolo.equals("ε");
    }

    private String pilhaString() {
        StringBuilder sb = new StringBuilder();
        for (String s : pilha) {
            sb.append(s).append(" ");
        }
        return sb.toString().trim();
    }

}
