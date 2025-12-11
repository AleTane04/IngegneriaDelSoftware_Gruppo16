/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/** 
 * @file LibroNonDisponibileException.java
 * @brief Il file contiene l'implementazione dell'eccezione LibroNonDisponibileException
 * 
 * informazioni sul file
 */

package org.softeng.exceptions;

/**
 *
 * @author 39392
 */
public class LibroNonDisponibileException extends Exception {

    /**
     * @brief Costruttore predefinito per LibroNonDisponibileException.
     *
     * Crea una nuova istanza di LibroNonDisponibileException senza un
     * messaggio di dettaglio. Questa eccezione deve essere sollevata
     * quando si tenta di eseguire un'operazione un prestito
     * su un libro che non è attualmente disponibile (esaurito o già in prestito).
     */
    public LibroNonDisponibileException() {
    }

    /**
     * @brief Costruttore predefinito per LibroNonDisponibileException con messaggio.
     *
     * Crea una nuova istanza di LibroNonDisponibileException con un
     * messaggio di dettaglio. Questa eccezione deve essere sollevata
     * quando si tenta di eseguire un prestito
     * su un libro che non è attualmente disponibile (esaurito o già in prestito).
     *
     * @param[in] msg Il messaggio di dettaglio
     */
    public LibroNonDisponibileException(String msg) {
        super(msg);
    }
}
