/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.aletane04.data;
import org.aletane04.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableList;
import org.aletane04.model.*;

/**
 *
 * @author angel
 */
public class Biblioteca {
    private final String descrizione;
    private ObservableList<Utente> listaUtenti;
    private ObservableList<Libro> listaLibri;
    private ObservableList<Prestito> listaPrestiti;
    
    public Biblioteca(String descrizione){
        this.descrizione=descrizione;
        this.listaLibri = FXCollections.observableArrayList();
        this.listaUtenti = FXCollections.observableArrayList();
        this.listaPrestiti = FXCollections.observableArrayList();
        
        caricaDati();
        
    }
    public void caricaDati() {
    
    
    }
    
    public void aggiungiLibro(Libro newLibro) {
    
        if(listaLibri.contains(newLibro) == false) {
            listaLibri.add(newLibro);
        } else {
            //Lancio una eccezione;
        }
    }
    
  
    
    public String getDescrizione(){
        return descrizione;
    }
    
    
    
}
