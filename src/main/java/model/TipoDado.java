/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Bernardo
 */
public final class TipoDado {
    public static final TipoDado INT = new TipoDado("int");
    public static final TipoDado BOOLEAN = new TipoDado("boolean");
    public static final TipoDado VOID = new TipoDado("void");
    
    private final String nome;
    
    private TipoDado(String nome){
        this.nome = nome;
    }
    
    public String getNome(){
        return nome;
    }
    
    public static TipoDado[] values(){
        return new TipoDado[] {INT, BOOLEAN, VOID};
    }
    
    public static TipoDado fromString(String nome) {
        for(TipoDado tipo : values()){
            if(tipo.nome.equalsIgnoreCase(nome)){
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo desconhecido: "+nome);
    }
    
    @Override
    public String toString(){
        return nome;
    }
}
