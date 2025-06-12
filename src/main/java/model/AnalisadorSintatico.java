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
    private final Map<String, Map<String, String>> tabela;
    private int ponteiro = 0;
    private final List<PassoSintatico> passos = new ArrayList<>();
    private final List<Erro> listaErros = new ArrayList<>();
    // Analise semantica
    private final TabelaDeSimbolos tabelaDeSimbolos = new TabelaDeSimbolos();
    private final Stack<TipoDado> pilhaDeTipos = new Stack<>(); // Para checagem de tipos em expressões
    private TipoDado ultimoTipoDeclarado; // Guarda o tipo (int/boolean) ao declarar vars
    private Token tokenAnterior; // Guarda o último token consumido
    
    public AnalisadorSintatico(List<Token> tokens, TabelaSintatica tabelaSintatica) {
        this.tokens = tokens;
        this.tabela = tabelaSintatica.tabela;
    }
    
    public AnalisadorSintatico(TabelaSintatica tabelaSintatica){
        this.tabela = tabelaSintatica.tabela;
        this.tokens = new ArrayList<>();
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

        Token lookahead = tokens.get(ponteiro);

        while (!pilha.isEmpty()) {
            String topo = pilha.peek();
            tokenAnterior = (ponteiro > 0) ? tokens.get(ponteiro -1):null;
            if(topo.startsWith("@")){
                pilha.pop();
                executarAcaoSemantica(topo, lookahead); // Executa ação
            } else if (isTerminal(topo) || topo.equals("$")) {
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
                    Map<String, String> producoes = tabela.get(topo);
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
                        // incrementa até o lexema que realiza a sincronização
                        while(!tokens.get(ponteiro).getLexema().equals(";")){
                            ponteiro++;
                        }
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

    private void executarAcaoSemantica(String acao, Token lookahead){
        switch(acao){
            case "@SET_TYPE" -> {
                // O token do tipo (int/boolean) foi o último consumido
                if(tokenAnterior.getToken().equals("NUMERO_INTEIRO")){
                    this.ultimoTipoDeclarado = TipoDado.INT;
                } else {
                    this.ultimoTipoDeclarado = TipoDado.BOOLEAN;
                }
            }
            case "@ADD_VAR" -> {
                // O token do identificador foi o último consumido
                Simbolo s = new Simbolo(tokenAnterior.getToken(),
                        ultimoTipoDeclarado, tokenAnterior.getLinha(), 
                        tokenAnterior.getColunaInicial());
                if(!tabelaDeSimbolos.inserir(s)){
                    // Erro Semântico: Variável já declarada nesse escopo
                    listaErros.add(new Erro(
                            "Variável duplicada",
                            "Semântica",
                            "Variável '" + tokenAnterior.getLexema() +
                                    "' não declarada", 
                            tokenAnterior.getLinha(),
                            tokenAnterior.getColunaInicial()
                    ));
                }
            }
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

}
