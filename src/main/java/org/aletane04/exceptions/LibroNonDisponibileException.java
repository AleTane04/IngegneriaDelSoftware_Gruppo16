/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.exceptions;

/**
 *
 * @author 39392
 */
public class LibroNonDisponibileException extends Exception {

    /**
     * Crea un nuova istanza di LibroNonDisponibileException
     * senza un messaggio dettagliato
     */
    public LibroNonDisponibileException() {
    }

    /**
     * 
     * Crea un nuova istanza di LibroNonDisponibileException 
     * con un messaggio dettagliato
     * 
     * 
     * @param[in] msg il messaggio dettagliato
     * 
     */
    public LibroNonDisponibileException(String msg) {
        super(msg);
    }
}
