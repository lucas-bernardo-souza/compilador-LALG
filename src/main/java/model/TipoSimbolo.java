/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Bernardo
 * Os tipos de símbolo podem ser variável, procedimento
 * ou parâmetro
 */
public final class TipoSimbolo {
    public static final TipoSimbolo VARIAVEL = new TipoSimbolo("VARIÁVEL", 1);
    public static final TipoSimbolo PROCEDIMENTO = new TipoSimbolo("PROCEDIMENTO", 2);
    public static final TipoSimbolo PROGRAMA = new TipoSimbolo("PROGRAMA", 3);
    
    private final String nome;
    private final int codigo;
    
    private TipoSimbolo(String nome, int codigo){
        this.nome = nome;
        this.codigo = codigo;
    }
    
    // Retorna todas as instâncias pré-definidas
    public static TipoSimbolo[] values(){
        return new TipoSimbolo[]{
            VARIAVEL, PROCEDIMENTO, PROGRAMA
        };
    }
    
    // método para recuperar tipos por nome
    public static TipoSimbolo fromString(String text){
        for(TipoSimbolo tipo : values()){
            if(tipo.nome.equalsIgnoreCase(text)){
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo inválido: " + text);
    }
    
    public String getNome(){
        return nome;
    }
    
    public int getCodigo(){
        return codigo;
    }
    
    @Override
    public String toString(){
        return nome;
    }
}
