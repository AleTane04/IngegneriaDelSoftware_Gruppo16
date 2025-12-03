/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionebiblioteca;

import java.time.LocalDate;

/**
 *
 * @author angel
 */
public class Libro {
    private String titolo;
    private String[] autori;
    private LocalDate annoPublicazione;
    private final int codice;
    private int quantità;
    
    public Libro(String titolo, String[] autori, int annoPub, int mesePub, int giornoPub, int codice, int quantità){
        this.titolo=titolo;
        this.autori=autori;
        this.annoPublicazione=LocalDate.of(annoPub, mesePub, giornoPub);
        this.codice=codice;
        this.quantità=quantità;
    }
    
    //metodi getter
    public String getTitolo(){
        return titolo;
    }
    
    public String[] getAutori(){
        return autori;
    }
    
    public LocalDate getAnnoPublicazione(){
        return annoPublicazione;
    }
    
    public int getCodice(){
        return codice;
    }
    
    public int getQuantità(){
        return quantità;
    }
    
    
    //metodi setter
    public void setTitolo(String titolo){
        this.titolo=titolo;
    }
    
    public void setAutori(String[] autori){
        this.autori=autori;
    }
    
    public void setAnnoPublicazione(int annoPub, int mesePub, int giornoPub){
        this.annoPublicazione=LocalDate.of(annoPub, mesePub, giornoPub);
    }
    
    public void setQuantità(int quantità){
        this.quantità=quantità;
    }
    
    
    
}
