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
    private String type;
    private String value;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
    
    public String getType(){
        return this.type;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public String getValue(){
        return this.value;
    }
    
    public void setValue(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + type + ", " + value + ")";
    }
}
