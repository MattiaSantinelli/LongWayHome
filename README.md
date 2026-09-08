# 🏠 Long Way Home

---
Long Way Home è un videogioco RPG a griglia 2D sviluppato in Java e JavaFX. 
Il giocatore interpreta un eroe che deve farsi strada in una mappa esplorabile, affrontando nemici via via più forti 
per ritrovare la via di casa.

Il progetto è stato realizzato applicando i principi della programmazione orientata agli oggetti (OOP),
il pattern architetturale MVC (Model-View-Controller) e la persistenza dei dati tramite formato JSON.

Autore: Mattia Santinelli  
Matricola: 130324  
Esame: Progetto sviluppato per l'esame di Modellazione e Gestione della conoscenza  
Anno accademico: 2025/26 - Università di Camerino

## 🎮 Caratteristiche principali

---
- ✅ Mappa esplorabile(10x10): generazione della mappa con posizionamento dinamico di nemici, ostacoli e obiettivo finale.
- ✅ Sistema di combattimento in tempo reale: incontri a turni dinamici con timer automatici per l'attaccco nemico. 
Opzioni di attacco, difesa per il giocatore.
- ✅ Aumento di difficoltà dinamico: i nemici diventano più forti ogni 30 secondi di gioco trascorsi.
- ✅ Progressione eroe: l'eroe guadagna statistiche extra (HP e attacco) ogni 3 nemici sconfitti.
- ✅ Classifica e Persistenza JSON: salvataggio automatico solo in caso di vittoria. La classifica ordina i giocatori 
in base ai nemici sconfitti (decrescente) e al tempo impiegato (crescente).
- ✅ Interfaccia grafica: styling personalizzato tramite CSS JavaFX con temi scuri/fantasy, animazioni ed effetti glow.
- 5 tipi di nemici
- ✅ Controlli per movimento(WASD/frecce): controlli per curare il movimento dell'eroe protagonista all'interno del gioco,
frecce direzionali o WASD.

## 🛠️ Tecnologie utilizzate

---
-  **Java 25** (LTS)
-  **JavaFX** (UI, Layouts, CSS, Timelines)
-  **Gson** (Serializzazione e deserializzazione JSON)
-  **Maven/Gradle** (Gestione delle dipendenze e build)

## 🚀 Come eseguire il progetto

---
### Prerequisiti
- **JDK 25** (LTS)
- **Gradle**

### 1 - Istruzioni
```bash
git clone https://github.com/MattiaSantinelli/LongWayHome.git
cd LongWayHome
```

### 2 - Build del progetto
```bash
./gradlew build
```

### 3 - Esecuzione
```bash
./gradlew run
```

---
## 🤖 Uso di strumenti di AI

### 1 - ChatGPT (OpenAI) utilizzato per:

    -Comprensione concetti teorici (pattern MVC, gestione eventi)
    -Generazione foto sfondo per la parte grafica del gioco
    -Generazione foto personaggi per la grafica del gioco

### 2- GitHub Copilot (OpenAI) utilizzato per:

    -Autocomletamento di metodi ripetitivi
    -Generazione rapida di codice

### 3 - Gemini:

    -Chiarire errori di compilazione (classi mancanti, import)
    -Suggerimenti su struttura del codice e organizzazione delle classi
    -Generazione bozze per metodi complessi (combattimento, classifica)
    -Debug e risoluzione di problemi

L'AI è stata utilizzata solo come supporto e per la risoluzione di problemi, tutto il codice suggerito è stato analizzato,
testato, modificato e integrato manualmente dall'autore del progetto affinché rispettasse le specifiche del progetto.

⚠️ **NOTA BENE!**  
Progetto universitario sviluppato per l'esame di Programmazione Avanzata / Modellazione e Gestione della Conoscenza (UNICAM).

---


📌 Per una descrizione più dettagliata dell’uso dell’AI, utilizzare la [Wiki del repository](https://github.com/MattiaSantinelli/LongWayHome/wiki/git).


