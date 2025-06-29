/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Bernardo
 */
public class TabelaDeSimbolos {

    private Stack<Map<String, Simbolo>> pilhaDeEscopos;
    private int nivelAtual;

    public TabelaDeSimbolos() {
        this.pilhaDeEscopos = new Stack<>();
        this.nivelAtual = -1;
        entrarEscopo(); // Inicia o escopo global
    }

    public void entrarEscopo() {
        pilhaDeEscopos.push(new HashMap<>());
        nivelAtual++;
    }

    public void sairEscopo() {
        pilhaDeEscopos.pop();
        nivelAtual--;
    }

    public boolean inserir(Simbolo simbolo) {
        Map<String, Simbolo> escopoAtual = pilhaDeEscopos.peek();
        if (escopoAtual.containsKey(simbolo.getNome())) {
            return false; // Erro: Dupla declaração
        }
        simbolo.setEscopo(nivelAtual);
        escopoAtual.put(simbolo.getNome(), simbolo);
        return true;
    }

    public Simbolo buscar(String lexema) {
        for (int i = pilhaDeEscopos.size() - 1; i >= 0; i--) {
            if (pilhaDeEscopos.get(i).containsKey(lexema)) {
                return pilhaDeEscopos.get(i).get(lexema);
            }
        }
        return null; // Erro: Identificador não declarado
    }

    public Simbolo buscarNoEscopoAtual(String nome) {
        if (!pilhaDeEscopos.isEmpty()) {
            Map<String, Simbolo> escopoAtual = pilhaDeEscopos.peek();
            return escopoAtual.getOrDefault(nome, null);
        }
        return null;
    }

    public List<Simbolo> getEscopoAtual() {
        if (!pilhaDeEscopos.isEmpty()) {
            return new ArrayList<>(pilhaDeEscopos.peek().values());
        }
        return new ArrayList<>();
    }
    
    public void marcarSimboloComoUsado(String nome) {
    for (int i = pilhaDeEscopos.size() - 1; i >= 0; i--) {
        Map<String, Simbolo> escopo = pilhaDeEscopos.get(i);
        if (escopo.containsKey(nome)) {
            Simbolo simbolo = escopo.get(nome);
            simbolo.marcarComoUsada();
            return;
        }
    }
}

}
