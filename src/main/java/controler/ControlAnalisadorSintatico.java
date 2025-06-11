/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

/**
 *
 * @author saraa
 */
    
import java.util.List;
import java.util.Map;
import model.TabelaSintatica;
import model.AnalisadorSintatico;
import model.Erro;
import model.PassoSintatico;
import model.Token;


public class ControlAnalisadorSintatico {
    private List<Token> tokens;
    private AnalisadorSintatico analisadorSintatico;

    public ControlAnalisadorSintatico(){
        TabelaSintatica tabelaSintatica = new TabelaSintatica();
        analisadorSintatico = new AnalisadorSintatico(tabelaSintatica);
    }
    
    public ControlAnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void analiseSintatica() {
        TabelaSintatica tabelaSintatica = new TabelaSintatica();
        analisadorSintatico = new AnalisadorSintatico(tokens, tabelaSintatica);
        analisadorSintatico.analisar();
    }

    public List<PassoSintatico> getPassos() {
        if (analisadorSintatico != null) {
            return analisadorSintatico.getPassos();
        }
        return List.of();
    }

    public List<Erro> getListaErros() {
        if (analisadorSintatico != null) {
            return analisadorSintatico.getListaErros();
        }
        return List.of();
    }
    
    public Map<String, Map<String, String>> getTabela(){
        if(analisadorSintatico != null){
            return analisadorSintatico.getTabela();
        }
        return null;
    }
}

