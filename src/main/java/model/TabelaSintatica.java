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

    Map<String, Map<String, String>> tabela;

    public TabelaSintatica() {
        tabela = new HashMap<>();
        montarTabela();
    }

    void montarTabela() {
        Map<String, String> producoes;

        // <programa> ::= program <identificador> ; <bloco>
        producoes = new HashMap<>();
        producoes.put("program", "program <identificador> ; <bloco>");
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
        producoes.put("boolean", "<tipo> <lista_de_identificadores>");
        producoes.put("int", "<tipo> <lista_de_identificadores>");
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
        producoes.put("procedure", "procedure <identificador> <parametros_formais_opcional> ; <bloco>");
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
        producoes.put("IDENTIFICADOR", "<lista_de_identificadores> : <tipo>");
        tabela.put("<seção_de_parâmetros_formais>", producoes);

        // <lista_de_identificadores> ::= <identificador> { , <identificador> }
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "<identificador> <lista_de_identificadores'>");
        tabela.put("<lista_de_identificadores>", producoes);

        // <lista_de_identificadores'> ::= ε | , <identificador> <lista_de_identificadores'>
        producoes = new HashMap<>();
        producoes.put(",", ", <identificador> <lista_de_identificadores'>");
        producoes.put(":", "ε");
        producoes.put(";", "ε");
        tabela.put("<lista_de_identificadores'>", producoes);

        // <comando_composto>
        producoes = new HashMap<>();
        producoes.put("begin", "begin <lista_de_comandos> end");
        tabela.put("<comando_composto>", producoes);

        // <lista_de_comandos>
        producoes = new HashMap<>();
        producoes.put("IDENTIFICADOR", "<comando> <lista_de_comandos'>");
        producoes.put("begin", "<comando> <lista_de_comandos'>");
        producoes.put("if", "<comando> <lista_de_comandos'>");
        producoes.put("while", "<comando> <lista_de_comandos'>");
        producoes.put("read", "<comando> <lista_de_comandos'>");
        producoes.put("write", "<comando> <lista_de_comandos'>");
        tabela.put("<lista_de_comandos>", producoes);

        // <lista_de_comandos'>
        producoes = new HashMap<>();
        producoes.put(";", "; <comando> <lista_de_comandos'>");
        producoes.put("end", "ε");
        tabela.put("<lista_de_comandos'>", producoes);
    }
}
