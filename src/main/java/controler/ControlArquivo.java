/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import model.AbrirArquivo;
import model.SalvarArquivo;

/**
 *
 * @author Bernardo
 */
public class ControlArquivo {
    
    public static void salvarArquivo(String conteudo){
        SalvarArquivo.salvarTexto(conteudo);
    }
    
    public static String abrirArquivo(){
        return AbrirArquivo.abrirTexto();
    }
}
