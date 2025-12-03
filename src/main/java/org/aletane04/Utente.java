/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionebiblioteca;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author angel
 */
public class Utente {
    private final String nome;
    private final String cognome;
    private final String matricola;
    private String email;
    private Map<LocalDate, Libro> prestiti;
    
    public Utente(String nome, String cognome, String matricola, String email){
        this.nome=nome;
        this.cognome=cognome;
        this.matricola=matricola;
        this.email=email;
        this.prestiti=new TreeMap<>();
    }
    
    //metodi getter
   public String getNome(){
       return nome;
   }
   
   public String getCognome(){
       return cognome;
   }
   
   public String getMatricola(){
       return matricola;
   }
   
   public String getEmail(){
       return email;
   }
   
   //metodi setter
   public void setEmail(String email){
       this.email=email;
   }
   
   
}
