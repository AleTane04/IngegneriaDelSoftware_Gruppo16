/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.softeng.model;

import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.time.Month;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author chiac
 */
public class LibroTest {
    
    private Libro libro;
    
    @Test
    public void testCostruttore() {
    Libro l = new Libro("Delitto e castigo","Fedor Dostoevskij", LocalDate.of(1866, 1, 1), "9781234567890", 5 );

    
             assertEquals("Delitto e castigo", l.getTitolo());
             assertEquals("Fedor Dostoevskij", l.getAutori());
             assertEquals(LocalDate.of(1866, 1, 1), l.getAnnoPubblicazione());
             assertEquals("9781234567890", l.getCodiceISBN());
             assertEquals(5, l.getNumeroCopieDisponibili());
    
}
    
    @Test
    public void testCostruttoreCSV(){
        
        String rigaCSV = "Delitto e castigo;Fedor Dostoevskij;1866-01-01;9781234567890;5";
        
        Libro libroCSV = new Libro(rigaCSV);
        
             assertEquals("Delitto e castigo", libroCSV.getTitolo());
             assertEquals("Fedor Dostoevskij", libroCSV.getAutori());
             assertEquals(LocalDate.of(1866, 1, 1), libroCSV.getAnnoPubblicazione());
             assertEquals("9781234567890", libroCSV.getCodiceISBN());
             assertEquals(5, libroCSV.getNumeroCopieDisponibili());  
        
        
    }
        
        
    
    
    @BeforeAll
    public static void setUpClass() {
        
        System.out.println("Inizio test classe LibroTest");

    }
    
    @AfterAll
    public static void tearDownClass() {
        
        System.out.println("Fine test classe LibroTest");
    }
    
    
    @BeforeEach
    public void setUp() {
        
         libro = new Libro("Delitto e castigo", "Fedor Dostoevskij", LocalDate.of(1866, 1, 1), "9781234567890", 5);
    }
    
    @AfterEach
    public void tearDown() {
        
        libro = null;
    }

    /**
     * Test of getTitolo method, of class Libro.
     */
    @Test
    public void testGetTitolo() {
       
        
        assertEquals("Delitto e castigo", libro.getTitolo());
    }

    /**
     * Test of getAutori method, of class Libro.
     */
    @Test
    public void testGetAutori() {
        
          assertEquals("Fedor Dostoevskij", libro.getAutori());
        
    }

    /**
     * Test of getAnnoPubblicazione method, of class Libro.
     */
    @Test
    public void testGetAnnoPubblicazione() {
        
           assertEquals(LocalDate.of(1866, 1, 1), libro.getAnnoPubblicazione());

    }

    /**
     * Test of getCodiceISBN method, of class Libro.
     */
    @Test
    public void testGetCodiceISBN() {
       
        assertEquals("9781234567890", libro.getCodiceISBN());
    }

    /**
     * Test of getNumeroCopieDisponibili method, of class Libro.
     */
    @Test
    public void testGetNumeroCopieDisponibili() {
        
        assertEquals(5, libro.getNumeroCopieDisponibili());
    }

    /**
     * Test of setTitolo method, of class Libro.
     */
    @Test
    public void testSetTitolo() {
        
        libro.setTitolo("Nuovo Titolo");
        assertEquals("Nuovo Titolo", libro.getTitolo());
    }

    /**
     * Test of setAutori method, of class Libro.
     */
    @Test
    public void testSetAutori() {
      
        libro.setAutori("Altro Autore");
        assertEquals("Altro Autore", libro.getAutori());
    }

    /**
     * Test of setAnnoPubblicazione method, of class Libro.
     */
    @Test
    public void testSetAnnoPubblicazione() {
        
          
     libro.setAnnoPubblicazione(LocalDate.of(1867, 1, 1));
    
    
      assertEquals(LocalDate.of(1867, 1, 1), libro.getAnnoPubblicazione());
       
    }

    /**
     * Test of setNumeroCopieDisponibili method, of class Libro.
     */
    @Test
    public void testSetNumeroCopieDisponibili() {
       
        libro.setNumeroCopieDisponibili(10);
        assertEquals(10, libro.getNumeroCopieDisponibili());
    }

    @Test
    public void testSetNumeroCopieDisponibiliNegativo(){

        assertThrows(IllegalArgumentException.class, () -> {

            libro.setNumeroCopieDisponibili(-1);

        });

    }

    /**
     * Test of decrementaNumeroCopieDisponibili method, of class Libro.
     */
    @Test
    public void testDecrementaNumeroCopieDisponibili() {
        
        libro.decrementaNumeroCopieDisponibili();
        assertEquals(4, libro.getNumeroCopieDisponibili());
    }

    /**
     * Test of incrementaNumeroCopieDisponibili method, of class Libro.
     */
    @Test
    public void testIncrementaNumeroCopieDisponibili() {
       
        libro.incrementaNumeroCopieDisponibili();
        assertEquals(6, libro.getNumeroCopieDisponibili());
    }

    /**
     * Test of equals method, of class Libro.
     */
    @Test
    public void testEqualsTrue() {
        
        Libro secondoLibro = new Libro( " titolo2", "autore2",LocalDate.of(2000, 3, 2),"9781234567890",1);
        assertTrue(libro.equals(secondoLibro));
    }

        
     @Test
    public void testEqualsFalse() {
        
        Libro terzoLibro = new Libro( " titolo3", "autore3",LocalDate.of(2000, 3 ,2),"1234567890123",1);
        assertFalse(libro.equals(terzoLibro));
    }

    /**
     * Test of hashCode method, of class Libro.
     */
    @Test
    public void testHashCode() {
        
        assertEquals("9781234567890".hashCode(), libro.hashCode());
    
        
    }

    /**
     * Test of toString method, of class Libro.
     */
    @Test
    public void testToString() {
        String l = libro.toString();
        assertTrue(l.contains("Delitto e castigo"));
        assertTrue(l.contains("Fedor Dostoevskij"));
        assertTrue(l.contains("9781234567890"));
   
    
    }

    /**
     * Test of toCSV method, of class Libro.
     */
    @Test
    public void testToCSV() {
        
        assertEquals( "Delitto e castigo;Fedor Dostoevskij;1866-01-01;9781234567890;5",libro.toCSV());
        
    }
    
}
