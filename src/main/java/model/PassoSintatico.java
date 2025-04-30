/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author saraa
 */

public class PassoSintatico {
    private String pilha;
    private String lookahead;
    private String acao;

    public PassoSintatico (String pilha, String lookahead, String acao) {
        this.pilha = pilha;
        this.lookahead = lookahead;
        this.acao = acao;
    }

    public String getPilha() {
        return pilha;
    }

    public void setPilha(String pilha) {
        this.pilha = pilha;
    }

    public String getLookahead() {
        return lookahead;
    }

    public void setLookahead(String lookahead) {
        this.lookahead = lookahead;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }
}

