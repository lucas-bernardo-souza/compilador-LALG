package model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lucas-bernardo
 */
public final class TabelaSintatica {

    Map<String, Map<String, String>> tabela;

    public TabelaSintatica() {
        tabela = new HashMap<>();
        montarTabela();
    }

    void montarTabela() {
        Map<String, String> producoes;

        // <programa> ::= program <identificador> ; <bloco>
        producoes = new HashMap<>();
        producoes.put("program", "program <identificador> @ADD_PROGRAM ; <bloco> @END_SCOPE");
        producoes.put("$", "sinc");
        tabela.put("<programa>", producoes);

        // <identificador> ::= id
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "id");
        producoes.put(";", "sinc");
        producoes.put(",", "sinc");
        tabela.put("<identificador>", producoes);

        // <bloco> ::= [<parte_de_declaração_de_variáveis>] [<parte_de_declarações_de_sub-rotinas>] <comando_composto>
        producoes = new HashMap<>();
        producoes.put("boolean", "<parte_de_declaração_de_variáveis> <parte_de_declarações_de_sub-rotinas> <comando_composto>");
        producoes.put("int", "<parte_de_declaração_de_variáveis> <parte_de_declarações_de_sub-rotinas> <comando_composto>");
        producoes.put("procedure", "<parte_de_declarações_de_sub-rotinas> <comando_composto>");
        producoes.put("begin", "<comando_composto>");
        producoes.put("$", "sinc");
        tabela.put("<bloco>", producoes);

        // <parte_de_declaração_de_variáveis> ::= <declaração_de_variáveis> { ; <declaração_de_variáveis> }
        producoes = new HashMap<>();
        producoes.put("boolean", "<declaração_de_variáveis> ; <parte_de_declaração_de_variáveis'>");
        producoes.put("int", "<declaração_de_variáveis> ; <parte_de_declaração_de_variáveis'>");
        tabela.put("<parte_de_declaração_de_variáveis>", producoes);

        // <parte_de_declaração_de_variáveis'> ::= ε | <declaração_de_variáveis> ; <parte_de_declaração_de_variáveis'>
        producoes = new HashMap<>();
        producoes.put("boolean", "<declaração_de_variáveis> ; <parte_de_declaração_de_variáveis'>");
        producoes.put("int", "<declaração_de_variáveis> ; <parte_de_declaração_de_variáveis'>");
        producoes.put("procedure", "ε");
        producoes.put("begin", "ε");
        tabela.put("<parte_de_declaração_de_variáveis'>", producoes);

        // <declaração_de_variáveis> ::= <tipo> <lista_de_identificadores>
        producoes = new HashMap<>();
        producoes.put("boolean", "<tipo> @SET_TYPE <lista_de_identificadores>");
        producoes.put("int", "<tipo> @SET_TYPE <lista_de_identificadores>");
        producoes.put("$", "ε");
        tabela.put("<declaração_de_variáveis>", producoes);

        // <tipo> ::= int | boolean
        producoes = new HashMap<>();
        producoes.put("boolean", "boolean");
        producoes.put("int", "int");
        tabela.put("<tipo>", producoes);

        // <parte_de_declarações_de_sub-rotinas> ::= { <declaração_de_procedimento> ; }
        producoes = new HashMap<>();
        producoes.put("procedure", "<declaração_de_procedimento> ; <parte_de_declarações_de_sub-rotinas'>");
        producoes.put("begin", "ε");
        tabela.put("<parte_de_declarações_de_sub-rotinas>", producoes);

        // <parte_de_declarações_de_sub-rotinas'> ::= ε | <declaração_de_procedimento> ; <parte_de_declarações_de_sub-rotinas'>
        producoes = new HashMap<>();
        producoes.put("procedure", "<declaração_de_procedimento> ; <parte_de_declarações_de_sub-rotinas'>");
        producoes.put("begin", "ε");
        tabela.put("<parte_de_declarações_de_sub-rotinas'>", producoes);

        // <declaração_de_procedimento> ::= procedure <identificador> [ <parâmetros formais> ] ; <bloco>
        producoes = new HashMap<>();
        producoes.put("procedure", "procedure <identificador> @ADD_PROCEDURE @BEGIN_SCOPE <parametros_formais_opcional> ; <bloco> @END_SCOPE");
        tabela.put("<declaração_de_procedimento>", producoes);

        // <parametros_formais_opcional> ::= ε | ( <seção de parâmetros formais> { ; <seção de parâmetros formais> } )
        producoes = new HashMap<>();
        producoes.put("(", "( <seção_de_parâmetros_formais> <parametros_formais_opcional'> )");
        producoes.put(";", "ε");
        producoes.put("begin", "ε");
        tabela.put("<parametros_formais_opcional>", producoes);

        // <parametros_formais_opcional'> ::= ε | ; <seção de parâmetros formais> <parametros_formais_opcional'>
        producoes = new HashMap<>();
        producoes.put(";", "; <seção_de_parâmetros_formais> <parametros_formais_opcional'>");
        producoes.put(")", "ε");
        tabela.put("<parametros_formais_opcional'>", producoes);

        // <seção_de_parâmetros_formais> ::= [var] <lista_de_identificadores> : <identificador>
        producoes = new HashMap<>();
        producoes.put("var", "var <lista_de_identificadores> : <tipo>");
        producoes.put("IDENTIFICADOR", "<lista_de_identificadores> @ADD_PARAM : <tipo>");
        tabela.put("<seção_de_parâmetros_formais>", producoes);

        // <lista_de_identificadores> ::= <identificador> { , <identificador> }
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "<identificador> @ADD_VAR <lista_de_identificadores'>");
        tabela.put("<lista_de_identificadores>", producoes);

        // <lista_de_identificadores'> ::= ε | , <identificador> <lista_de_identificadores'>
        producoes = new HashMap<>();
        producoes.put(",", ", <identificador> @ADD_VAR <lista_de_identificadores'>");
        producoes.put(":", "ε");
        producoes.put(";", "ε");
        tabela.put("<lista_de_identificadores'>", producoes);

        // <comando_composto>
        producoes = new HashMap<>();
        producoes.put("begin", "begin <comando> <lista_de_comandos> end");
        producoes.put("end", "sinc");
        producoes.put(";", "sinc");
        producoes.put("$", "sinc");
        tabela.put("<comando_composto>", producoes);

        // <lista_de_comandos>
        producoes = new HashMap<>();
        producoes.put(";", "; <comando> <lista_de_comandos>");
        producoes.put("end", "ε");
        producoes.put("$", "ε");
        tabela.put("<lista_de_comandos>", producoes);

        //<comando>
        producoes = new HashMap<>();
        producoes.put("begin", "<comando_composto>");
        producoes.put(";", "sinc");
        producoes.put("procedure", "sinc");
        producoes.put("IDENTIFICADOR", "<identificador> @PUSH_ID_TYPE <comando'>");
        producoes.put("end", "sinc");
        producoes.put("$", "sinc");
        producoes.put("if", "<comando_condicional_1>");
        producoes.put("while", "<comando_repetitivo_1>");
        producoes.put("write", "<comando_escrita>");
        producoes.put("read", "<comando_leitura>");
        tabela.put("<comando>", producoes);

        // <comando'>
        producoes = new HashMap<>();
        producoes.put("begin", "sinc");
        producoes.put(";", "sinc");
        producoes.put("procedure", "sinc");
        producoes.put("(", "<chamada_de_procedimento>");
        producoes.put("end", "sinc");
        producoes.put("$", "sinc");
        producoes.put(":=", "@STORE_VAR := <expressao> @CHECK_ASSIGN");
        tabela.put("<comando'>", producoes);
        
        producoes = new HashMap<>();
        producoes.put("write", "write <chamada_de_procedimento>");
        producoes.put("$", "sinc");
        tabela.put("<comando_escrita>", producoes);
        
        producoes = new HashMap<>();
        producoes.put("read", "read <chamada_de_procedimento>");
        producoes.put("$", "sinc");
        tabela.put("<comando_leitura>", producoes);

        // <atribuicao>
        producoes = new HashMap<>();
        producoes.put(":=", ":= <expressao> @CHECK_ASSIGN");
        producoes.put(";", "sinc");
        producoes.put("end", "sinc");
        producoes.put("$", "sinc");
        tabela.put("<atribuicao>", producoes);

        // <chamada_de_procedimento>
        producoes = new HashMap<>();
        producoes.put("(", "( <lista_de_expressoes> )");
        producoes.put(";", "sinc");
        producoes.put("end", "sinc");
        producoes.put("$", "sinc");
        tabela.put("<chamada_de_procedimento>", producoes);

       
        // <comando_condicional_1>
        producoes = new HashMap<>();
        producoes.put(".", "sinc");
        producoes.put(";", "sinc");
        producoes.put("procedure", "sinc");
        producoes.put("begin", "sinc");
        producoes.put("end", "sinc");
        producoes.put("else", "sinc");
        producoes.put("$", "sinc");
        producoes.put("if", "if <expressao> @CHECK_CONDITION then <comando> <else>");
        tabela.put("<comando_condicional_1>", producoes);

        // <else>
        producoes = new HashMap<>();
        producoes.put(".", "ε");
        producoes.put(";", "ε");
        producoes.put("procedure", "ε");
        producoes.put("begin", "ε");
        producoes.put("end", "ε");
        producoes.put("$", "ε");
        producoes.put("else", "else <comando>");
        tabela.put("<else>", producoes);

        // <comando_repetitivo_1>
        producoes = new HashMap<>();
        producoes.put(".", "sinc");
        producoes.put(";", "sinc");
        producoes.put("procedure", "sinc");
        producoes.put("begin", "sinc");
        producoes.put("end", "sinc");
        producoes.put("else", "sinc");
        producoes.put("$", "sinc");
        producoes.put("while", "while <expressao> @CHECK_CONDITION do <comando>");
        tabela.put("<comando_repetitivo_1>", producoes);

        //<expressao>
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "<expressao_simples> <expressao'>");
        producoes.put("true", "<identificador>");
        producoes.put("false", "<identificador>");
        producoes.put(",", "sinc");
        producoes.put("(", "<expressao_simples> <expressao'>");
        producoes.put(")", "sinc");
        producoes.put("+", "<expressao_simples> <expressao'>");
        producoes.put("-", "<expressao_simples> <expressao'>");
        producoes.put("NUMERO_INTEIRO", "<expressao_simples> <expressao'>");
        producoes.put("not", "<expressao_simples> <expressao'>");
        producoes.put("then", "sinc");
        producoes.put("do", "sinc");
        tabela.put("<expressao>", producoes);

        //<expressao'>
        producoes = new HashMap<>();
        producoes.put(";", "ε");
        producoes.put("procedure", "ε");
        producoes.put("begin", "ε");
        producoes.put(",", "ε");
        producoes.put(")", "ε");
        producoes.put("end", "ε");
        producoes.put("else", "ε");
        producoes.put("=", "<relacao> <expressao_simples> @CHECK_RELATIONAL_OP");
        producoes.put("<>", "<relacao> <expressao_simples> @CHECK_RELATIONAL_OP");
        producoes.put("<", "<relacao> <expressao_simples> @CHECK_RELATIONAL_OP");
        producoes.put("<=", "<relacao> <expressao_simples> @CHECK_RELATIONAL_OP");
        producoes.put(">", "<relacao> <expressao_simples> @CHECK_RELATIONAL_OP");
        producoes.put(">=", "<relacao> <expressao_simples> @CHECK_RELATIONAL_OP");
        producoes.put("then", "ε");
        producoes.put("do", "ε");
        tabela.put("<expressao'>", producoes);

        //<relacao>
        producoes = new HashMap<>();
        producoes.put(";", "sinc");
        producoes.put("procedure", "sinc");
        producoes.put("begin", "sinc");
        producoes.put("IDENTIFICADOR", "sinc");
        producoes.put(",", "sinc");
        producoes.put("(", "sinc");
        producoes.put(")", "sinc");
        producoes.put("end", "sinc");
        producoes.put("else", "sinc");
        producoes.put("+", "sinc");
        producoes.put("-", "sinc");
        producoes.put("NUMERO_INTEIRO", "sinc");
        producoes.put("not", "sinc");
        producoes.put("=", "=");
        producoes.put("<>", "<>");
        producoes.put("<", "<");
        producoes.put("<=", "<=");
        producoes.put(">", ">");
        producoes.put(">=", ">=");
        producoes.put("then", "sinc");
        producoes.put("do", "sinc");
        tabela.put("<relacao>", producoes);

        // <expressao_simples>
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "<op> <termo> <expressao_simples'>");
        producoes.put(",", "sinc");
        producoes.put("(", "<op> <termo> <expressao_simples'>");
        producoes.put(")", "sinc");
        producoes.put("end", "ε");
        producoes.put("else", "ε");
        producoes.put("+", "<op> <termo> <expressao_simples'>");
        producoes.put("-", "<op> <termo> <expressao_simples'>");
        producoes.put("NUMERO_INTEIRO", "<op> <termo> <expressao_simples'>");
        producoes.put("not", "<op> <termo> <expressao_simples'>");
        producoes.put("=", "sinc");
        producoes.put("<>", "sinc");
        producoes.put("<", "sinc");
        producoes.put("<=", "sinc");
        producoes.put(">=", "sinc");
        producoes.put(">", "sinc");
        producoes.put("then", "sinc");
        producoes.put("do", "sinc");
        tabela.put("<expressao_simples>", producoes);

        // <op>
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "ε");
        producoes.put("(", "ε");
        producoes.put("+", "+");
        producoes.put("-", "-");
        producoes.put("NUMERO_INTEIRO", "ε");
        producoes.put("not", "ε");
        tabela.put("<op>", producoes);

        //<expressao_simples'>
        producoes = new HashMap<>();
        producoes.put(";", "ε");
        producoes.put(")", "ε");
        producoes.put("end", "ε");
        producoes.put("else", "ε");
        producoes.put("+", "+ <termo> @CHECK_ARITHMETIC_OP <expressao_simples'>");
        producoes.put("-", "- <termo> @CHECK_ARITHMETIC_OP <expressao_simples'>");
        producoes.put("or", "or <termo> @CHECK_ARITHMETIC_OP <expressao_simples'>");
        producoes.put("=", "ε");
        producoes.put("<>", "ε");
        producoes.put("<", "ε");
        producoes.put("<=", "ε");
        producoes.put(">=", "ε");
        producoes.put(">", "ε");
        producoes.put("then", "ε");
        producoes.put("do", "ε");
        tabela.put("<expressao_simples'>", producoes);

        //<op2>
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "sinc");
        producoes.put("(", "sinc");
        producoes.put("+", "+");
        producoes.put("-", "-");
        producoes.put("NUMERO_INTEIRO", "sinc");
        producoes.put("not", "sinc");
        producoes.put("or", "or");
        tabela.put("<op2>", producoes);

        // <termo>
        producoes = new HashMap<>();
        producoes.put(".", "sinc");
        producoes.put(";", "sinc");
        producoes.put("procedure", "sinc");
        producoes.put("begin", "sinc");
        producoes.put("IDENTIFICADOR", "<fator> <termo'>");
        producoes.put(",", "sinc");
        producoes.put("(", "<fator> <termo'>");
        producoes.put(")", "sinc");
        producoes.put("end", "sinc");
        producoes.put("else", "sinc");
        producoes.put("+", "sinc");
        producoes.put("-", "sinc");
        producoes.put("NUMERO_INTEIRO", "<fator> <termo'>");
        producoes.put("not", "<fator> <termo'>");
        producoes.put("=", "sinc");
        producoes.put("<>", "sinc");
        producoes.put("<", "sinc");
        producoes.put("<=", "sinc");
        producoes.put(">=", "sinc");
        producoes.put(">", "sinc");
        producoes.put("then", "sinc");
        producoes.put("do", "sinc");
        producoes.put("]", "sinc");
        producoes.put("or", "sinc");
        tabela.put("<termo>", producoes);

        // <termo'>
        producoes = new HashMap<>();
        producoes.put(";", "ε");
        producoes.put("procedure", "ε");
        producoes.put("begin", "ε");
        producoes.put(",", "ε");
        producoes.put(")", "ε");
        producoes.put("end", "ε");
        producoes.put("else", "ε");
        producoes.put("+", "ε");
        producoes.put("-", "ε");
        producoes.put("=", "ε");
        producoes.put("<>", "ε");
        producoes.put("<", "ε");
        producoes.put("<=", "ε");
        producoes.put(">=", "ε");
        producoes.put(">", "ε");
        producoes.put("then", "ε");
        producoes.put("do", "ε");
        producoes.put("or", "ε");
        producoes.put("*", "<op3> <fator> @CHECK_ARITHMETIC_OP <termo'>");
        producoes.put("div", "<op3> <fator> @CHECK_ARITHMETIC_OP <termo'>");
        producoes.put("and", "<op3> <fator> @CHECK_LOGICAL_OP <termo'>");
        tabela.put("<termo'>", producoes);

        // <op3>
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "sinc");
        producoes.put("(", "sinc");
        producoes.put("NUMERO_INTEIRO", "sinc");
        producoes.put("not", "sinc");
        producoes.put("*", "*");
        producoes.put("div", "div");
        producoes.put("and", "and");
        tabela.put("<op3>", producoes);

        //<fator> ::= <identificador> | <numero> | ( <expressao> ) | not <fator>
        producoes = new HashMap<>();
        producoes.put(".", "sinc");
        producoes.put(";", "sinc");
        producoes.put("procedure", "sinc");
        producoes.put("begin", "sinc");
        producoes.put("IDENTIFICADOR", "<identificador> @PUSH_ID_TYPE");
        producoes.put(",", "sinc");
        producoes.put("(", "( <expressao> )");
        producoes.put(")", "sinc");
        producoes.put("end", "sinc");
        producoes.put("else", "sinc");
        producoes.put("+", "sinc");
        producoes.put("-", "sinc");
        producoes.put("NUMERO_INTEIRO", "<numero> @PUSH_INT_TYPE");
        producoes.put("not", "not <fator> @CHECK_NOT");
        producoes.put("=", "sinc");
        producoes.put("<>", "sinc");
        producoes.put("<", "sinc");
        producoes.put("<=", "sinc");
        producoes.put(">=", "sinc");
        producoes.put(">", "sinc");
        producoes.put("then", "sinc");
        producoes.put("do", "sinc");
        producoes.put("]", "sinc");
        producoes.put("or", "sinc");
        producoes.put("*", "sinc");
        producoes.put("div", "sinc");
        producoes.put("and", "sinc");
        tabela.put("<fator>", producoes);


        // <lista_de_expressoes>
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "<expressao> <lista_de_expressoes'>");
        producoes.put("true", "<expressao> <lista_de_expressoes'>");
        producoes.put("false", "<expressao> <lista_de_expressoes'>");
        producoes.put("(", "<expressao> <lista_de_expressoes'>");
        producoes.put(")", "ε");
        producoes.put("+", "<expressao> <lista_de_expressoes'>");
        producoes.put("-", "<expressao> <lista_de_expressoes'>");
        producoes.put("NUMERO_INTEIRO", "<expressao> <lista_de_expressoes'>");
        producoes.put("not", "<expressao> <lista_de_expressoes'>");
        tabela.put("<lista_de_expressoes>", producoes);

        // <lista_de_expressoes'>
        producoes = new HashMap<>();
        producoes.put(",", ", <expressao> <lista_de_expressoes'>");
        producoes.put(")", "ε");
        tabela.put("<lista_de_expressoes'>", producoes);

 
    }
}
