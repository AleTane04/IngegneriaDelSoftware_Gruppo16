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
public class LibroNonDisponibileException extends Exception {

    /**
     * Creates a new instance of <code>LibroNonDisponibileException</code>
     * without detail message.
     */
    public LibroNonDisponibileException() {
    }

    /**
     * Constructs an instance of <code>LibroNonDisponibileException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public LibroNonDisponibileException(String msg) {
        super(msg);
    }
}
