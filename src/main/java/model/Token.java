/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Bernardo
 */
public class Token {
    private String token;
    private String lexema;

    public Token(String type, String value) {
        this.token = type;
        this.lexema = value;
    }
    
    public String getToken(){
        return this.token;
    }
    
    public void setToken(String type){
        this.token = type;
    }
    
    public String getLexema(){
        return this.lexema;
    }
    
    public void setLexema(String value){
        this.lexema = value;
    }

    @Override
    public String toString() {
        return "(" + token + ", " + lexema + ")";
    }
}
