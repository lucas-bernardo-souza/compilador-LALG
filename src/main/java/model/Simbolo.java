/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author Bernardo
 */
public class Simbolo {
    private String nome; // identificador do símbolo (x, calculaMedia)
    private TipoSimbolo categoria; // programa, variavel ou procedimento
    private TipoDado tipoDado; // int ou boolean
    private int escopo; // nível aninhamento (escopo global = 0)
    private List<Simbolo> parametros; // lista de parâmetros de procedimentos
    private int linhaDeclaracao;
    private int colunaDeclaracao;
    
    // Contrutor para variáveis
    public Simbolo(String nome, TipoDado tipo, int linha, int coluna){
        this.nome = nome;
        this.tipoDado = tipo;
        
        this.linhaDeclaracao = linha;
        this.colunaDeclaracao = coluna;
    }
    
    // Construtir para procedimentos
    public Simbolo(String nome, TipoDado retorno, List<Simbolo> params, int escopo){
        this.nome = nome;
        this.tipoDado = retorno;
        this.parametros = params;
        this.escopo = escopo;
        this.categoria = TipoSimbolo.PROCEDIMENTO;
    }

    public String getNome() {
        return nome;
    }

    public TipoSimbolo getCategoria() {
        return categoria;
    }

    public TipoDado getTipoDado() {
        return tipoDado;
    }

    public int getEscopo() {
        return escopo;
    }

    public List<Simbolo> getParametros() {
        return parametros;
    }

    public int getLinhaDeclaracao() {
        return linhaDeclaracao;
    }

    public int getColunaDeclaracao() {
        return colunaDeclaracao;
    }
    
    public void setEscopo(int nivel){
        this.escopo = nivel;
    }
}
