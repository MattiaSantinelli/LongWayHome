# 🏠 LongWayHome

Gioco di ruolo (RPG) a turni in cui il giocatore ha lo scopo di ritrovare la propria via per poter tornare a casa.
Il percorso però è disseminato di nemici e ostacoli di difficoltà crescente con il passare del tempo, che il giocatore 
dovrà affrontare e sconfiggere per poter proseguire.
Solo quando il giocatore riuscirà a ritrovare la propria via per tornare a casa, il gioco terminerà con la vittoria del giocatore.

Sviluppato in Java con interfaccia grafica JavaFX e sistema di build Gradle.

Autore: Mattia Santinelli |
Matricola: 130324 |
Progetto sviluppato per l'esame di Modellazione e Gestione della conoscenza |
Anno accademico: 2025/26 - Università di Camerino

## 🎮 Caratteristiche principali
- ✅ Sistema di combattimento a turni
- ✅ 5 tipi di nemici
- ✅ Logica di generazione casuale della mappa da gioco
- ✅ Combattimento con nemici
- ✅ Sviluppo logica di potenziamento dei personaggi
- ✅ Salvataggio punteggi su File come in una classifica
- ✅ Controlli da tastiera (WASD/frecce)

## 🚀 Come eseguire il progetto

### Prerequisiti
- **Java 21** o superiore
- **Gradle**

### Istruzioni
```bash
git clone https://github.com/MattiaSantinelli/LongWayHome.git
cd MattiaSantinelli
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

---

## 🤖 Uso di strumenti di AI

ChatGPT (OpenAI) utilizzato per:

    -Comprendere concetti teorici (pattern MVC, gestione eventi)
    -Generazione foto sfondo per la parte grafica del gicoo
    -Generazione foto personaggi per la grafica del gioco

GitHub Copilot (OpenAI) utilizzato per:

    -Autocomletamento di metodi ripetitivi
    -Generazione rapida di codice

Gemini:

    -Chiarire errori di compilazione (classi mancanti, import)
    -Suggerimenti su struttura del codice e organizzazione delle classi
    -Generazione bozze per metodi complessi (combattimento, salvataggio punteggi)
    -Debug e risoluzione di problemi