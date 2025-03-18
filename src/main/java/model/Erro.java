/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Bernardo
 */
public class Erro {
    private String nome;
    // em qual fase do processo de análise o erro foi encontrado
    private String fase;
    private String descricao;
    private int linha;
    private int coluna;
    
    public Erro(String nome, String fase, String descricao, int linha, int coluna){
        this.nome = nome;
        this.fase = fase;
        this.descricao = descricao;
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public Erro(){
        
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return "Erro{" + "nome=" + nome + ", fase=" + fase + ", descricao=" + descricao + ", linha=" + linha + ", coluna=" + coluna + '}';
    }
    
}
