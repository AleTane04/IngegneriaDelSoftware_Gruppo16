/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.model;

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
    
    
    public Utente(String nome, String cognome, String matricola, String email){
        this.nome=nome;
        this.cognome=cognome;
        this.matricola=matricola;
        this.email=email;
        
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
   
   @Override
    public boolean equals(Object obj) {
        /* Verifica dei casi degeneri */
        if (obj == null) return false;
        if (this == obj) return true;

        /* Verifica che appartengano alla stessa classe */
        if (this.getClass() != obj.getClass()) 
            return false;
        /* Downcast sicuro; in obj ho un riferimento di un libro */
        Utente myUtente = (Utente) obj;

        /* Essendo all'interno della stessa classe, accedo all'attributo "codice", che è privato, di myLibro */
        return this.matricola == myUtente.matricola;
    }

    @Override
    public int hashCode(){
        return matricola == null ? 0 : matricola.hashCode();

       
    }
}
