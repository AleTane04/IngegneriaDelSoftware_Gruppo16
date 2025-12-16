# 📚 Gestione Biblioteca Universitaria

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=for-the-badge)

Un'applicazione per la gestione di una biblioteca, sviluppata in **Java** con interfaccia grafica **JavaFX**.

## 📋 Indice
- [Introduzione](#-introduzione)
- [Funzionalità Chiave](#-funzionalità-chiave)
- [Architettura e Tecnologie](#-architettura-e-tecnologie)
- [Requisiti e Analisi](#-requisiti-e-analisi)
- [Installazione e Avvio](#-installazione-e-avvio)
  

---

## 📖 Introduzione
Il **Sistema di Gestione di una Libreria** è progettato gestire una biblioteca universitaria, permettendo la gestione digitalizzata del catalogo libri, dell'anagrafica utenti e del monitoraggio dei prestiti. 

Il sistema opera in osservanza delle direttive ricevute, come il limite massimo di libri per utente e il controllo della disponibilità delle copie in tempo reale.

---

## 🚀 Funzionalità Chiave

### 📕 Gestione Catalogo (Libri)
- **Inserimento/Modifica:** Aggiunta di nuovi libri con dettagli (Titolo, Autori, Anno, ID, Copie) e modifica delle giacenze.
- **Ricerca Avanzata:** Filtri per Titolo, Autore o Codice Identificativo univoco.
- **Rimozione:** Cancellazione sicura dei libri dal sistema.

### 👥 Gestione Utenza
- **Registrazione:** Inserimento dati anagrafici (Nome, Cognome, Matricola, Email).
- **Storico:** Visualizzazione dei libri attualmente in prestito per ogni utente.
- **Manutenzione:** Modifica e cancellazione profili utente.

### 🔄 Gestione Prestiti 
- **Check-out (Prestito):** - Controllo automatico disponibilità copie.
    - Controllo limite massimo (Max 3 libri per utente).
    - Decremento automatico delle scorte.
- **Check-in (Restituzione):** - Registrazione rientro libro.
    - Ripristino disponibilità copia nel catalogo.
- **Monitoraggio:** Visualizzazione prestiti attivi ordinati per data di restituzione.

---

## 🛠 Architettura e Tecnologie

| Componente | Tecnologia / Descrizione |
| :--- | :--- |
| **Linguaggio** | Java (JDK 8) |
| **GUI** | JavaFX (FXML) |
| **Persistenza Dati** | File System (CSV)|
| **Pattern Architetturale** | MVC (Model-View-Controller) |
| **Sistemi Operativi** | Cross-platform |

---


## 💻 Installazione e Avvio

### Prerequisiti
* Java JDK 8+.
* Maven.

### Passaggi
1. **Clonare la repository**
   ```bash
   git clone https://github.com/AleTane04/IngegneriaDelSoftware_Gruppo16.git
2. **Spostarsi nella cartella del Progetto**
   ```bash   
   cd IngegneriaDelSoftware_Gruppo16
3. **Adoperare Maven per il build automatico**
   ```bash   
   mvn package
4. **Eseguire il file .jar generato più grande**
   ```bash   
      java -jar target/  IngSW_GestioneBiblioteca-1.0-SNAPSHOT.jar

5. **Caricare i .csv su cui si desidera operare nella root directory del tool (il tool viene fornito con dei .csv di prova)**

6. **Buon utilizzo! P.S. per salvare, utilizzare la funzione "Salva" nel menù contestuale in alto a sinistra. In caso si voglia uscire senza salvare, chiudere la finestra o premere "Esci" nel menù contestuale in alto a sinistra, poi, in entrambi i casi confermare, l'uscita dal tool**
  
  