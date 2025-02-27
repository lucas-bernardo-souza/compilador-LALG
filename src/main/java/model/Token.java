/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Bernardo
 */
public class Token {
    private String token;
    private String lexema;
    private int linha;
    private int colunaInicial;
    private int colunaFinal;

    public Token(String type, String value, int linha, int colunaInicial, int colunaFinal) {
        this.token = type;
        this.lexema = value;
        this.linha = linha;
        this.colunaInicial = colunaInicial;
        this.colunaFinal = colunaFinal;
    }
    
    public String getToken(){
        return this.token;
    }
    
    public void setToken(String type){
        this.token = type;
    }
    
    public String getLexema(){
        return this.lexema;
    }
    
    public void setLexema(String value){
        this.lexema = value;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColunaInicial() {
        return colunaInicial;
    }

    public void setColunaInicial(int colunaInicial) {
        this.colunaInicial = colunaInicial;
    }

    public int getColunaFinal() {
        return colunaFinal;
    }

    public void setColunaFinal(int colunaFinal) {
        this.colunaFinal = colunaFinal;
    }

    @Override
    public String toString() {
        return "(" + token + ", " + lexema + ")";
    }
}
