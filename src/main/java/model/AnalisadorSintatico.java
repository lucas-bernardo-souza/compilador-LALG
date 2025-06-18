package model;

import java.util.ArrayList;
import java.util.EmptyStackException;
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
            // Guarda o token anterior para dar contexto às açõe semânticas
            tokenAnterior = (ponteiro > 0 && ponteiro <= tokens.size()) ? tokens.get(ponteiro -1):null;
            if(topo.startsWith("@")){
                pilha.pop();
                executarAcaoSemantica(topo); // Executa ação
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


    private void executarAcaoSemantica(String acao){
        Simbolo simbolo;
        TipoDado tipo1, tipo2;
        
        switch(acao){
            case "@ADD_PROCEDURE" -> {
                simbolo = new Simbolo(tokenAnterior.getLexema(), TipoDado.PROCEDIMENTO, "void");
                if(!tabelaDeSimbolos.inserir(simbolo)){
                    listaErros.add(new Erro("Erro Semântico", "Semântica", "Identificador '" + tokenAnterior.getLexema() + "' já declarado neste escopo.", tokenAnterior.getLinha(), tokenAnterior.getColunaInicial()));
                }
            }
            // Ações de escopo e declaração
            case "@BEGIN_SCOPE" ->{
                tabelaDeSimbolos.entrarEscopo();
            }
            case "@END_SCOPE" -> {
                tabelaDeSimbolos.sairEscopo();
            }
            case "@SET_TYPE" -> {
                // O token do tipo (int/boolean) foi o último consumido
                if(tokenAnterior.getLexema().equals("int")){
                    this.ultimoTipoDeclarado = TipoDado.INT;
                } else {
                    this.ultimoTipoDeclarado = TipoDado.BOOLEAN;
                }
            }
            case "@ADD_VAR" -> {
                // O token do identificador foi o último consumido
                simbolo = new Simbolo(tokenAnterior.getLexema(),
                        ultimoTipoDeclarado, tokenAnterior.getLinha(), 
                        tokenAnterior.getColunaInicial());
                if(!tabelaDeSimbolos.inserir(simbolo)){
                    // Erro Semântico: Variável já declarada nesse escopo
                    listaErros.add(new Erro(
                            "Variável duplicada",
                            "Semântica",
                            "Variável '" + tokenAnterior.getLexema() +
                                    "' já declarada neste escopo", 
                            tokenAnterior.getLinha(),
                            tokenAnterior.getColunaInicial()
                    ));
                }
            }
            // Ações de verificação de tipos em expressões
            case "@PUSH_ID_TYPE" -> {
                simbolo = tabelaDeSimbolos.buscar(tokenAnterior.getLexema());
                if(simbolo == null){
                    listaErros.add(new Erro(
                            "",
                            "Semântica",
                            "Identificador '" + tokenAnterior.getLexema() + 
                                    "' não declarado.", tokenAnterior.getLinha(),
                            tokenAnterior.getColunaInicial()
                    ));
                    // Adiciona o tipo ERRO no topo da pilha
                    pilhaDeTipos.push(TipoDado.ERRO); 
                } else {
                    // Adiciona o tipo de dado no topo da pilha
                    pilhaDeTipos.push(simbolo.getTipoDado());
                }
            }
            case "@PUSH_INT_TYPE" -> {
                // Adiciona o tipo de dado int no topo da pilha
                pilhaDeTipos.push(TipoDado.INT);
            }
            case "@PUSH_BOOL_TYPE" -> {
                // Adciona o tipo de dado boolean no topo da pilha
                simbolo = tabelaDeSimbolos.buscar(tokenAnterior.getLexema());
                pilhaDeTipos.push(simbolo.getTipoDado());
            }
            // Ações de verificação de operadores
            case "@CHECK_ARITHMETIC_OP" -> {
                try {
                    // Remove dois tipos do topo da pilha
                    tipo1 = pilhaDeTipos.pop();
                    tipo2 = pilhaDeTipos.pop();
                    if(!(tipo1 == TipoDado.INT) || !(tipo2 == TipoDado.INT)){
                        listaErros.add(new Erro(
                                "",
                                "Semantíca",
                                "Tipos incompatíveis para operação aritimética. Esperado: (int, int),"
                                        + "Encontrado: ("+tipo1.getNome() + ", "+ tipo2.getNome() + ").",
                                tokenAnterior.getLinha(),
                                tokenAnterior.getColunaInicial()
                        ));
                        // Adiciona o tipo Erro no topo da pilha
                        pilhaDeTipos.push(TipoDado.ERRO);
                    } else {
                        // Adiciona o tipo inteiro no topo da pilha
                        pilhaDeTipos.push(TipoDado.INT);
                    }
                } catch(EmptyStackException e){
                    System.err.println("@CHECK_ARITHMETIC_OP - A pilha de tipos se encontra vazia!");
                }
            }
            case "@CHECK_LOGICAL_OP" -> {
                try {
                    // Remove dois tipos da pilha
                    tipo1 = pilhaDeTipos.pop();
                    tipo2 = pilhaDeTipos.pop();

                    if(!(tipo1 == TipoDado.BOOLEAN) || !(tipo2 == TipoDado.BOOLEAN)){
                        listaErros.add(new Erro(
                                "",
                                "Semântica",
                                "Tipos incompatíveis para operações lógicas. Esperado:"
                                        + " (boolean, boolean), Encontrado: (" +
                                        tipo2.getNome() + ", " + tipo1.getNome() +
                                        ").",
                                tokenAnterior.getLinha(),
                                tokenAnterior.getColunaInicial()
                        ));
                        // Adiciona o tipo Erro ao topo da pilha
                        pilhaDeTipos.push(TipoDado.ERRO);
                    } else {
                        // Adiciona o tipo boolean no topo da pilha
                        pilhaDeTipos.push(TipoDado.BOOLEAN);
                    }
                } catch(EmptyStackException e){
                    System.err.println("@CHECK_LOGICAL_OP - A pilha de tipos se encontra vazia!");
                } 
            }
            case "@CHECK_RELATIONAL_OP" -> {
                try{
                    // remove dois tipos da pilha
                    tipo1 = pilhaDeTipos.pop();
                    tipo2 = pilhaDeTipos.pop();
                    // Verifica se os dois tipos da relação são iguais se não for temos um erro
                    if(!(tipo1.equals(tipo2)) || tipo1 == TipoDado.ERRO){
                        listaErros.add(new Erro(
                                "",
                                "Semântica",
                                "Tipos incompatíveis para operação relacional: " + 
                                        tipo2.getNome() + " e " + tipo1.getNome() + ".",
                                tokenAnterior.getLinha(),
                                tokenAnterior.getColunaInicial()
                        ));
                        // Adciona o tipo erro no topo da pilha
                        pilhaDeTipos.push(TipoDado.ERRO);
                    } else {
                        // Adiciona o tipo boolean no topo da pilha
                        pilhaDeTipos.push(TipoDado.BOOLEAN);
                    }
                } catch(EmptyStackException e){
                    System.err.println("@CHECK_RELATIONAL_OP - A pilha de tipos se encontra vazia!");
                }              
            }
            case "@CHECK_NOT" -> {
                try {
                    tipo1 = pilhaDeTipos.pop();
                    if(!(tipo1 == TipoDado.BOOLEAN)){
                        listaErros.add(new Erro("Erro Semântico", "Semântica", "Operador 'not' só pode ser aplicado a expressões booleanas.", tokenAnterior.getLinha(), tokenAnterior.getColunaInicial()));
                        pilhaDeTipos.push(TipoDado.ERRO);
                    } else {
                        pilhaDeTipos.push(TipoDado.BOOLEAN);
                    }
                } catch(EmptyStackException e){
                    System.err.println("@CHECK_NOT - A pilha de tipos se encontra vazia!");
                }
            }
            case "@CHECK_ASSIGN" -> {
                try{
                    tipo2 = pilhaDeTipos.pop(); // Tipo da expressão (lado direito)
                    tipo1 = pilhaDeTipos.pop(); // Tipo da variável (lado esquerdo)
                    if (!tipo1.equals(tipo2)) {
                     listaErros.add(new Erro("", "Semântica", "Atribuição de tipo incompatível. Não é possível atribuir '" + tipo2 + "' a uma variável do tipo '" + tipo1 + "'.", tokenAnterior.getLinha(), tokenAnterior.getColunaInicial()));
                    }
                } catch(EmptyStackException e) {
                    System.err.println("@CHECK_ASSIGN - A pilha de tipos se encontra vazia!");
                }
            }
            case "@CHECK_CONDITION" -> {
                try{
                    tipo1 = pilhaDeTipos.pop();
                    if(!(tipo1 == TipoDado.BOOLEAN)){
                        listaErros.add(new Erro("Erro Semântico", "Semântica", "A expressão em um comando condicional ou repetitivo deve ser booleana.", tokenAnterior.getLinha(), tokenAnterior.getColunaInicial()));
                    }
                } catch(EmptyStackException e){
                    System.err.println("@CHECK_CONDITION - A pilha de tipos se encontra vazia!");
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
