package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author saraa
 */
public class AnalisadorSintatico {

    private final Stack<String> pilha = new Stack<>();
    private final List<Token> tokens;
    // Map<String, map<String, String>> não terminal / produção
    private final Map<String, Map<String, String>> tabela;
    // ponteiro para posição atual do símbolo na lista de tokens
    private int ponteiro = 0;
    // Guarda cada ação da análise sintática
    private final List<PassoSintatico> passos = new ArrayList<>();
    private final List<Erro> listaErros = new ArrayList<>();

    public AnalisadorSintatico(TabelaSintatica tabelaSintatica){
        this.tabela = tabelaSintatica.tabela;
        tokens = new ArrayList();
    }
    
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
    
    public Map<String, Map<String, String>> getTabela(){
        return tabela;
    }

    public void analisar() {
        pilha.push("$");
        pilha.push("<programa>");
        
        // ponteiro = 0 primeiro símbolo na lista de tokens
        Token lookahead = tokens.get(ponteiro);

        while (!pilha.isEmpty()) {
            // retorna o topo da pilha sem removê-lo
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
                            "Erro: A linguagem esperava o token '" + topo + "'. Mas, encontrou '" + lookahead.getLexema() + "'.",
                            lookahead.getLinha(),
                            lookahead.getColunaInicial()
                    );
                    listaErros.add(erro);
                    passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Erro: A linguagem esperava o token '" + topo + "'. Mas, encontrou '" + lookahead.getLexema() + "'."));
                    pilha.pop(); // Regra: remove o topo
                }
                if (topo.equals("end")) {
                    if (!lookahead.getLexema().equals("end")) {
                        Erro erro = new Erro(
                                "Fechamento ausente",
                                "Sintática",
                                "Esperado 'end' para fechar bloco 'begin'",
                                lookahead.getLinha(),
                                lookahead.getColunaInicial()
                        );
                        listaErros.add(erro);

                        // Sincronização: pula tokens até encontrar ';', 'end' ou '$'
                        while (!pilha.isEmpty() && !isSincronizador(lookahead.getLexema())) {
                            ponteiro++;
                            if (ponteiro < tokens.size()) {
                                lookahead = tokens.get(ponteiro);
                            } else {
                                lookahead = new Token("$", "$", 0, 0, 0);
                                break;
                            }
                        }
                        pilha.pop(); // Remove 'end' esperado
                    }
                }
                if (!pilha.isEmpty() && pilha.peek().equals("end") && lookahead.getLexema().equals("$")) {
                    listaErros.add(new Erro(
                        "Fechamento incompleto",
                        "Sintática",
                        "Bloco 'begin' não fechado corretamente",
                        tokens.get(tokens.size()-1).getLinha(),
                        tokens.get(tokens.size()-1).getColunaInicial()
                    ));
                }
                if (topo.equals("begin")) {
                    pilha.pop(); // Remove 'begin'
                    ponteiro++;

                    // Força verificação de pelo menos um comando
                    if (lookahead.getLexema().equals("end")) {
                        listaErros.add(new Erro(
                            "Bloco vazio",
                            "Sintática",
                            "Bloco 'begin' deve conter pelo menos um comando",
                            lookahead.getLinha(),
                            lookahead.getColunaInicial()
                        ));
                    }
                }
            } else {
                if (topo.equals("<identificador>") && (lookahead.getToken().equals("IDENTIFICADOR") || (lookahead.getLexema().equals("true") || lookahead.getLexema().equals("false")))) {
                    passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Consome token"));
                    pilha.pop();
                    ponteiro++;
                    if (ponteiro < tokens.size()) {
                        lookahead = tokens.get(ponteiro);
                    } else {
                        lookahead = new Token("$", "$", 0, 0, 0);
                    }
                } else if(topo.equals("<numero>") && lookahead.getToken().equals("NUMERO_INTEIRO")) {
                    passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Consome token"));
                    pilha.pop();
                    ponteiro++;
                    if (ponteiro < tokens.size()) {
                        lookahead = tokens.get(ponteiro);
                    } else {
                        lookahead = new Token("$", "$", 0, 0, 0);
                    }
                } else {
                    // A partir de um não terminal acessa suas produções
                    Map<String, String> producoes = tabela.get(topo);
                    // Se a produção for nula temos um erro e ele é adicionado ao passo sintático.
                    if (producoes == null) {
                        Erro erro = new Erro(
                                "Não-terminal desconhecido!",
                                "Sintática",
                                "Erro: O símbolo '" + topo + "' não pertence à gramática",
                                lookahead.getLinha(),
                                lookahead.getColunaInicial()
                        );
                        listaErros.add(erro);
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Erro: Não existe produção para '" + topo + "'"));
                        pilha.pop();
                        continue;
                    }
                    //Se existe produção para o não terminal seguimos a análise
                    String producao;
                    if (lookahead.getToken().equals("IDENTIFICADOR")) {
                        producao = producoes.get("IDENTIFICADOR");
                    } else if (lookahead.getToken().equals("NUMERO_INTEIRO")) {
                        producao = producoes.get("NUMERO_INTEIRO");
                    } else {
                        producao = producoes.get(lookahead.getLexema());
                    }
                    if (producao == null) {
                        Erro erro = new Erro(
                                "Produção inexistente!",
                                "Sintática",
                                "Erro: Símbolo inesperado: '" + lookahead.getLexema() + "' . Símbolo(s) esperado(s): " + producoes.keySet() + ".",
                                lookahead.getLinha(),
                                lookahead.getColunaInicial()
                        );
                        listaErros.add(erro);
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Erro: Símbolo inesperado: '" + lookahead.getLexema() + "' . Símbolo(s) esperado(s): " + producoes.keySet() + "."));

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
                                "Erro: Não foi encontrada produção para '" + lookahead.getLexema() + "' nessa posição. O símbolo " + topo + " será removido da pilha",
                                lookahead.getLinha(),
                                lookahead.getColunaInicial()
                        );

                        listaErros.add(erro);
                        passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Erro: Não foi encontrada produção para '" + lookahead.getLexema() + "' nessa posição. O símbolo " + topo + " será removido da pilha"));

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

        }

        if (listaErros.isEmpty()) {
            passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Análise sintática finalisada com sucesso!"));

        } else {
            passos.add(new PassoSintatico(pilhaString(), lookahead.getLexema() + " (" + lookahead.getToken() + ")", "Análise sintática finalizada com " + listaErros.size() + " erro(s)."));
        }
    }

    private boolean isTerminal(String simbolo) {
        if (!simbolo.equals("<>") && !simbolo.equals("<")) {
            return !simbolo.startsWith("<") && !simbolo.equals("ε");
        }
        return true;
    }

    private String pilhaString() {
        StringBuilder sb = new StringBuilder();
        for (String s : pilha) {
            sb.append(s).append(" ");
        }
        return sb.toString().trim();
    }
    
    private boolean isSincronizador(String token) {
        return token.equals(";") || token.equals("end") || token.equals("$");
    }

}
