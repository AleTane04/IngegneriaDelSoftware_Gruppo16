/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.softeng.exceptions;

/**
 *
 * @author 39392
 */
public class LimitePrestitiSuperatoException extends Exception {

    /**
     * @brief Costruttore predefinito per LimitePrestitiSuperatoException.
     *
     * Crea una nuova istanza di LimitePrestitiSuperatoException senza un
     * messaggio di dettaglio. Questa eccezione viene tipicamente sollevata
     * quando un'operazione di prestito fallisce perché è stato raggiunto
     * o superato il limite massimo di prestiti consentiti per un utente o
     * risorsa.
     */
    public LimitePrestitiSuperatoException() {
    }

    /**
     * @brief Costruttore predefinito per LimitePrestitiSuperatoException con messaggio.
     *
     * Crea una nuova istanza di LimitePrestitiSuperatoException con un
     * messaggio di dettaglio. Questa eccezione viene tipicamente sollevata
     * quando un'operazione di prestito fallisce perché è stato raggiunto
     * o superato il limite massimo di prestiti consentiti per un utente o
     * risorsa.
     *
     * @param[in] msg Il messaggio di dettaglio
     */
    public LimitePrestitiSuperatoException(String msg) {
        super(msg);
    }
}
