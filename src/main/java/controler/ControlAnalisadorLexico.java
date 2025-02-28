/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import java.util.List;
import model.AnalisadorLexico;
import model.Token;

/**
 *
 * @author Bernardo
 */
public class ControlAnalisadorLexico {
    private static AnalisadorLexico analisador;
    
    public ControlAnalisadorLexico(){
        if(analisador == null){
            analisador = new AnalisadorLexico();
        }
    }
    
    public static List<Token> tokenize(String input){
        List<Token> tokens;
        tokens = analisador.tokenize(input);
        return tokens;
    }
    
    public static String analiseLexica(List<Token> tokens){
        return analisador.analiseLexica(tokens);
    }
}
