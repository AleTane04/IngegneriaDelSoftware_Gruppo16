/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.model;

/**
 *
 * @author 39392
 */
public enum StatoPrestito 
{
    ATTIVO,
    IN_SCADENZA,
    SCADUTO,
    RESTITUITO;
    
    /* Mostro nella tabella dei caratteri più chiari e rappresentativi;
        ESEMPIO: ATTIVO -> Prestito attivo */
    @Override
    public String toString() 
    {
        switch(this) 
        {
            case ATTIVO: return "Prestito attivo";
            case IN_SCADENZA: return "Prestito in scadenza";
            case SCADUTO: return "Prestito scaduto";
            case RESTITUITO: return "Prestito concluso";
            default: return "Stato del Prestito";
        }
    }
    
}

