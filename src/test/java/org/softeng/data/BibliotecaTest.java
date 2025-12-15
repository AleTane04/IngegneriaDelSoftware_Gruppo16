package org.softeng.data;

import java.time.LocalDate;
import static java.time.LocalDate.of;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.softeng.data.*;
import org.softeng.exceptions.*;
import org.softeng.model.*;


public class BibliotecaTest{
  private Biblioteca b;

  @BeforeEach
  public void setUp()
  {
      b = new Biblioteca();
    ///< Input: liste vuote per testare isolatamente
    b.getLibri().clear();
    b.getUtenti().clear();
    b.getPrestiti().clear();
    
  }

  ///< Oracolo: le liste non devono mai essere null dopo l'inizializzazione
  @Test
  public void testConsturctorEGetters(){
    assertNotNull(b.getLibri());
    assertNotNull(b.getUtenti());
    assertNotNull(b.getPrestiti());
  }


  
  /** 
   *  Test Case: Aggiungi Libro con successo
   *  Input: Libro valido non presente
   *  Oracolo: Dimensione +1, libro presente
   */
  @Test
  public void testAggiungiLibro() throws LibroGiaPresenteException
  {
      Libro newLibro = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);
      b.aggiungiLibro(newLibro);
      assertTrue(b.getLibri().contains(newLibro));
      assertEquals(1, b.getLibri().size());
  }


  /** 
   *  Test Case: Aggiungi libro duplicato (eccezione)
   *  Input: due libri con stesso ISBN
   *  Oracolo: solleva LibroGiaPresenteException
   */
  @Test
  public void testAggiungiLibroDuplicato() throws LibroGiaPresenteException
  {
      Libro libro1 = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);
      b.aggiungiLibro(libro1);

      Libro libro2 = new Libro("Struttura e progetto dei calcolatori", "Patterson", LocalDate.of(2005,3,4), "978-8871929385", 4);

      assertThrows(LibroGiaPresenteException.class, () -> {
          b.aggiungiLibro(libro2);
      });
  }

  /** 
   *  Test Case: Rimuovi libro libero da prestiti
   *  Oracolo: il libro viene rimosso dalla lista 
   */
  @Test
  public void testRimuoviLibroLibero() throws Exception{
    Libro libro = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);
    b.aggiungiLibro(libro);

    b.rimuoviLibro(libro);
    assertEquals(0, b.getLibri().size());
    assertFalse(b.getLibri().contains(libro));
  }

  /** 
   *  Test Case: Rimuovi libro in prestito
   *  Input: Libro prestato a un utente
   *  Oracolo: solleva Exception
   */
  @Test
  public void testRimuoviLibroInPrestito() throws Exception{
    Utente u = new Utente("Marco", "Zambi", "0612709987", "m.zambi@studenti.unisa.it");
    Libro l = new Libro("Struttura e progetto dei calcolatori", "Patterson", LocalDate.of(2005,3,4), "978-8871929385", 4);
    b.aggiungiUtente(u);
    b.aggiungiLibro(l);
    b.registraPrestito(u, l, LocalDate.now().plusDays(30));

      assertThrows(Exception.class, () -> {
          b.rimuoviLibro(l);
      });
  }

  /** 
   * Test Case: Aggiungi Utente con successo
   * Input: Utente valido non presente
   * Oracolo: Dimensione lista +1, utente presente
   */
  @Test
  public void testAggiungiUtente() throws UtenteGiaPresenteException{
    Utente u = new Utente("Antonio", "Banderas", "0612709678", "a.banderas@studenti.unisa.it");

    b.aggiungiUtente(u);

    assertEquals(1, b.getUtenti().size());
    assertTrue(b.getUtenti().contains(u));
  }

  /**
   * Test Case: Aggiungi Utente duplicato (eccezione)
   * Input: due utenti con stessa matricola
   * Oracolo: solleva UtenteGiaPresenteException
   */
  @Test
  public void testAggiungiUtenteDuplicato() throws UtenteGiaPresenteException{
    Utente u1 = new Utente("Luigi", "Verdi", "0612708872", "l.verdi@studenti.unisa.it");
    b.aggiungiUtente(u1);

    Utente u2 = new Utente("Luigio", "Verdila", "0612708872", "l.verdila@studenti.unisa.it");

      assertThrows(UtenteGiaPresenteException.class, () -> {
          b.aggiungiUtente(u2);
      });
  }

  /**
   * Test Case: Rimozione utente con un prestito attivo
   * Oracolo: solleva Exception
   */
  @Test
  public void testRimuoviUtenteConPrestitoAttivo() throws Exception{
    Utente u = new Utente("Mario", "Rossi", "0612708872", "m.rossi@studenti.unisa.it");
    Libro l = new Libro("Struttura e progetto dei calcolatori", "Patterson", LocalDate.of(2005,3,4), "978-8871929385", 4);
    b.aggiungiUtente(u);
    b.aggiungiLibro(l);
    b.registraPrestito(u, l, LocalDate.now().plusDays(15));

      Exception exception = assertThrows(Exception.class, () -> {
          b.rimuoviUtente(u);
      });
      assertTrue(exception.getMessage().contains("Impossibile eliminare"));
  }

  /**
   * Test Case: Rimozione utente senza prestiti
   * Input: Utente che non ha nessun libro in prestito
   * Oracolo: Utente rimosso
   */
  @Test
  public void testRimuoviUtenteLibero() throws Exception{
    Utente u = new Utente("Teresa", "Dattolo", "0612708989", "t.dattolo@studenti.unisa.it");
    b.aggiungiUtente(u);
    b.rimuoviUtente(u);
    assertFalse(b.getUtenti().contains(u));
  }

  /**
   * Test Case: Registrazione Prestito valido
   * Oracolo: Prestito aggiunto alla lista e copie del libro decrementate
   */
  @Test
  public void tetsregistraPrestito() throws Exception{
    Libro l = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);
    Utente u = new Utente("Lorena", "Luciano", "0612707777", "l.luciano@studenti.unisa.it");
    b.aggiungiUtente(u);
    b.aggiungiLibro(l);
    b.registraPrestito(u, l, LocalDate.now().plusDays(15));
    assertEquals(1, b.getPrestiti().size());
    assertEquals(4, l.getNumeroCopieDisponibili());
  }

  /**
   * Test Case: Prestito con copie esaurite.
   * Oracolo: solleva LibroNonDisponibileException
   */
  @Test
  public void testRegistraPrestitoCopieEsaurite() throws Exception{
      Utente u1 = new Utente("Daniele", "Montefusco", "0612709023", "d.montefusco@studenti.unisa.it");
      Utente u2 = new Utente("Giuseppe", "Verdi", "0612709024", "g.verdi@studenti.unisa.it");
      Utente u3 = new Utente("Tizio", "Caio", "0612709025", "t.caio@studenti.unisa.it");
      Utente u4 = new Utente("Sempronio", "Rossi", "0612709026", "s.rossi@studenti.unisa.it");
      Utente u5 = new Utente("Pippo", "Franco", "0612709027", "p.franco@studenti.unisa.it");

      Libro l = new Libro("Struttura e progetto dei calcolatori", "Patterson", LocalDate.of(2005,3,4), "978-8871929385", 4); // 4 copie

      b.aggiungiUtente(u1);
      b.aggiungiUtente(u2);
      b.aggiungiUtente(u3);
      b.aggiungiUtente(u4);
      b.aggiungiUtente(u5);

      b.aggiungiLibro(l);


      b.registraPrestito(u1, l, LocalDate.now().plusDays(15));
      b.registraPrestito(u2, l, LocalDate.now().plusDays(15));
      b.registraPrestito(u3, l, LocalDate.now().plusDays(15));
      b.registraPrestito(u4, l, LocalDate.now().plusDays(15));


      assertThrows(LibroNonDisponibileException.class, () -> {
          b.registraPrestito(u5, l, LocalDate.now().plusDays(15));
      });
  }

  /**
   * Test Case: Limite 3 prestiti superato
   * Input: Utente che prende 4 libri
   * Oracolo: solleva LimitePrestitiSuperatoException 
   */
  @Test
  public void testLimitePrestitiSuperato() throws LimitePrestitiSuperatoException, UtenteGiaPresenteException, LibroGiaPresenteException, LibroNonDisponibileException{
    Utente u = new Utente("Federico", "Olivieri", "0612708674", "f.olivieri@studenti.unisa.it");
    b.aggiungiUtente(u);
    Libro l1 = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);
    Libro l2 = new Libro("Ingegneria del software", "Sommerville", LocalDate.of(2017, 2, 2), "978-8891902245", 3);
    Libro l3 = new Libro("UML Distilled", "Fowler", LocalDate.of(2018, 1, 19), "978-8891907820", 6);
    Libro l4 = new Libro("Struttura e progetto dei calcolatori", "Patterson", LocalDate.of(2005,3,4), "978-8871929315", 4);

    b.aggiungiLibro(l1);
    b.aggiungiLibro(l2);
    b.aggiungiLibro(l3);
    b.aggiungiLibro(l4);

    b.registraPrestito(u, l1, LocalDate.now().plusDays(10));
    b.registraPrestito(u, l2, LocalDate.now().plusDays(10));
    b.registraPrestito(u, l3, LocalDate.now().plusDays(10));


      assertThrows(LimitePrestitiSuperatoException.class, () -> {
          b.registraPrestito(u, l4, LocalDate.now().plusDays(10));
      });
  }


  /**
     * Test Case: Restituzione prestito.
     * Oracolo: Data restituzione settata e numero di copie incrementate.
     */
  @Test
  public void testRestituisiciPrestito() throws Exception{
    Libro l = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);
    Utente u = new Utente("Luigi", "Viola", "0612708883", "l.viola@studenti.unisa.it");

    b.aggiungiLibro(l);
    b.aggiungiUtente(u);

    b.registraPrestito(u, l, LocalDate.now().plusDays(30));
    Prestito p = b.getPrestiti().get(0);

    b.restituisciPrestito(p);

    assertNotNull(p.getDataRestituzioneEffettiva());
    assertEquals(5, l.getNumeroCopieDisponibili());
    assertEquals(StatoPrestito.RESTITUITO, p.getStatoPrestito());
  }

  /**
   * Test Case: Salvataggio dati.
   */
  @Test
  public void testSaveAll() {
    try {  
        b.aggiungiLibro(new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5));
        b.saveAll();
    } catch (Exception e) {
        fail("Il metodo saveAll ha lanciato un'eccezione imprevista: " + e.getMessage());
    }
  }
  
}
