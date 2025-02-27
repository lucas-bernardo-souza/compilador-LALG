/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bernardo
 */
public class Main {
    
    public static void main(String args[]){
        AnalisadorLexico analisador = new AnalisadorLexico();
        String input = "3 + 3";
        List<Token> tokens;
        tokens = analisador.tokenize(input);
        for(int i = 0; i < tokens.size(); i++){
            System.out.println(tokens.get(i));
        }
        
    }
}
