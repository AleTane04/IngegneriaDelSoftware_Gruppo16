/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionebiblioteca;


import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author angel
 */
public class Biblioteca {
    private final String descrizione;
    private Set<Utente> utenti;
    private Set<Libro> libri;
    
    public Biblioteca(String descrizione){
        this.descrizione=descrizione;
        this.utenti=new TreeSet<>();
        this.libri=new TreeSet<>();
    }
    
    public String getDescrizione(){
        return descrizione;
    }
    
    
    
}
