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
    private static AnalisadorLexico analisador;
    private List<Token> tokens;
    private List<Erro> erros;
    
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
        erros = analisador.buscaErrosLexicos(tokens);
    }
    
    public List<Token> tokenize(String input){
        tokens = analisador.tokenize(input);
        return tokens;
    }
    
    // Preciso pensar numa forma de mostrar o erro nos logs de erros da interface
    public String eliminaComentarios(String codigoFonte){
        Erro erroComentario = new Erro();
        String fonte = analisador.eliminaComentarios(codigoFonte, erroComentario);
        /* Verifica se o objeto erro está vazio, no caso afirmativo não foram
        encontrados erros */
        if(erroComentario.getNome() == null){
            return fonte;
        } else {
            /* Criar um método na interface que recebe um objeto erro para exibir
            o erro no jText de logs */
            return null;
        }
    }
}
