/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.aletane04.data;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aletane04.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author angel
 */
public class Biblioteca {
    
    private static final String FILE_LIBRI = "elenco_libri.csv";
    private static final String FILE_UTENTI = "elenco_utenti.csv";
    private static final String FILE_PRESTITI = "elenco_libri.csv";
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
            //gestioneio
            
        }
    }
    
    public void salvaCSVLibro(String nomeFile){
        
        try( PrintWriter pw = new PrintWriter(new FileWriter(nomeFile)) ){
            
            pw.println("TITOLO;AUTORI;ANNO PUBLICAZIONE;CODICE ISBN;QUANTITA'");
            for(Libro l : this.listaLibri){
                pw.append(l.getTitolo());
                pw.append(';');
                pw.append(l.getAutori());
                pw.append(';');
                pw.append(l.getAnnoPublicazione().toString());
                pw.append(';');
                pw.append(l.getCodice());
                pw.append(';');
                pw.println(l.getQuantità());
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void salvaCSVUtente(String nomeFile){
        
        try( PrintWriter pw = new PrintWriter(new FileWriter(nomeFile)) ){
            
            pw.println("NOME;COGNOME;MATRICOLA;EMAIL ISTITUZIONALE");
            for(Utente u : this.listaUtenti){
                pw.append(u.getNome());
                pw.append(';');
                pw.append(u.getCognome());
                pw.append(';');
                pw.append(u.getMatricola());
                pw.append(';');
                pw.append(u.getEmail());
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getDescrizione(){
        return descrizione;
    }
    
    
    
}
