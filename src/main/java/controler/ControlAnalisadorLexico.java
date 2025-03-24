/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import java.util.ArrayList;
import java.util.List;
import model.AnalisadorLexico;
import model.Erro;
import model.Token;

/**
 *
 * @author Bernardo
 */
public class ControlAnalisadorLexico {
    private AnalisadorLexico analisador = new AnalisadorLexico();
    private List<Token> tokens;
    private List<Erro> erros = new ArrayList();
    
    public ControlAnalisadorLexico(){
        if(analisador == null){
            analisador = new AnalisadorLexico();
        }
    }
    
    public List<Token> getTokens(){
        return tokens;
    }
    
    public List<Erro> getErros(){
        return erros;
    }
    
    public void analiseLexica(String codFonte){
        String codigoSemComentarios = eliminaComentarios(codFonte);
        tokens = tokenize(codigoSemComentarios);
        erros.addAll(analisador.buscaErrosLexicos(tokens));
    }
    
    public List<Token> tokenize(String input){
        tokens = analisador.tokenize(input);
        return tokens;
    }
    
   
    public String eliminaComentarios(String codigoFonte){
        
        String fonte = analisador.eliminaComentarios(codigoFonte);
        if(analisador.getErroComentario().getNome() != null){
            erros.add(analisador.getErroComentario());
        }
        return fonte;
        
    }
}
