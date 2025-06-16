/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Bernardo
 */
public class TabelaDeSimbolos {
    private Stack<Map<String, Simbolo>> pilhaDeEscopos;
    private int nivelAtual;
    
    public TabelaDeSimbolos(){
        this.pilhaDeEscopos = new Stack<>();
        this.nivelAtual = -1;
        entrarEscopo(); // Inicia o escopo global
    }
    
    public void entrarEscopo(){
        pilhaDeEscopos.push(new HashMap<>());
        nivelAtual++;
    }
    
    public void sairEscopo(){
        pilhaDeEscopos.pop();
        nivelAtual--;
    }
    
    public boolean inserir(Simbolo simbolo){
        Map<String, Simbolo> escopoAtual = pilhaDeEscopos.peek();
        if(escopoAtual.containsKey(simbolo.getNome())){
            return false; // Erro: Dupla declaração
        }
        simbolo.setEscopo(nivelAtual);
        escopoAtual.put(simbolo.getNome(), simbolo);
        return true;
    }
    
    public Simbolo buscar(String lexema){
        for(int i = pilhaDeEscopos.size()-1; i >= 0; i--){
            if(pilhaDeEscopos.get(i).containsKey(lexema)){
                return pilhaDeEscopos.get(i).get(lexema);
            }
        }
        return null; // Erro: Identificador não declarado
    }
}
