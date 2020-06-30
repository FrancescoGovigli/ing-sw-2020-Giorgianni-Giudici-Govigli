Santorini42 - Prova Finale di Ingegneria del Software - a.a. 2019-2020
======================================================================
![alt text](https://2.bp.blogspot.com/-YHuiYPBEHKs/WVKpXTzu5KI/AAAAAAAAYCs/pTVyng97P3EDoLq9PMqVv18ECzBD4K2CwCLcBGAs/s1600/copertina_santorini_2016.jpg)

Il progetto richiedeva di implementare il gioco da tavola
[*Santorini*](https://tabletopia.com/games/santorini).

Esso è stato sviluppato da: 
- [**Daniele Giorgianni**](https://github.com/DanieleGiorgianni) 
- [**Luca Giudici**](https://github.com/LucaGiudiciPoliMi)
- [**Francesco Govigli**](https://github.com/FrancescoGovigli)

Link Utili
----------
### UML
I diagrammi UML sono reperibili presso la cartella [*UML*](https://github.com/FrancescoGovigli/ing-sw-2020-Giorgianni-Giudici-Govigli/tree/master/deliveries/final/uml).

### JavaDoc
I JavaDoc sono reperibili presso la cartella
[*Javadoc*](https://github.com/FrancescoGovigli/ing-sw-2020-Giorgianni-Giudici-Govigli/tree/master/deliveries/final/javadoc).

### JAR
I file JAR sono reperibili presso la cartella
[*JAR*](https://github.com/FrancescoGovigli/ing-sw-2020-Giorgianni-Giudici-Govigli/tree/master/deliveries/final/jar).

Funzionalità sviluppate
-----------------------
Il progetto sviluppato implementa le seguenti funzionalità facendo
riferimento ai requisiti specificati dai docenti: 
- Regole Complete 
- Command Line Interface 
- Graphic User Interface 
- Socket 
- 1 Funzionalità Avanzata (Undo)

Istruzioni su come eseguire i JAR
---------------------------------
Il gioco può essere eseguito sia in locale che tramite rete. Nel primo
caso sarà sufficiente seguire le istruzioni **(1)**, nel secondo caso
le istruzioni **(2)**.

Il gioco potrà essere avviato in due modalità,
tramite linea di comando oppure con interfaccia grafica. Le istruzioni
sono riportate di seguito.

### Server
-   **(1)** Una volta scaricato il JAR del server (vedi link sopra),
    sarà sufficiente aprire il terminale da Windows o MacOs,
    posizionarsi nella cartella dov'è stato scaricato il JAR e digitare
    la seguente istruzione:

        java -jar serverApp.jar

    In questa circostanza l'indirizzo del Server sarà quello del Local
    Host.

-   **(2)** Una volta scaricato il JAR del server (vedi link sopra),
    sarà sufficiente aprire il terminale del dispositivo collegato alla
    rete, posizionarsi nella cartella dov'è stato scaricato il JAR e
    digitare la seguente istruzione:

        java -jar serverApp.jar

    In questa circostanza l'indirizzo del Server sarà quello di chi
    eseguirà il serverApp.jar.

Note:
- Il Server non gode dell'interfaccia grafica. 
- Il Server, se viene lanciato cliccando 2 volte sul corrispettivo file .jar, verrà eseguito in background.

### CLI
-   **(1)** Una volta scaricato il JAR del client (vedi link sopra),
    sarà sufficiente aprire il terminale da Windows o MacOs,
    posizionarsi nella cartella dov'è stato scaricato il JAR e digitare
    la seguente istruzione:

        java -jar clientApp.jar -cli

    Quando viene richiesto l'inserimento dell'indirizzo IP, sarà
    sufficiente premere *"Enter"* per usare in automatico l'indirizzo
    del Local Host.

-   **(2)** Una volta scaricato il JAR del client (vedi link sopra),
    sarà sufficiente aprire il terminale da Windows o MacOs,
    posizionarsi nella cartella dov'è stato scaricato il JAR e digitare
    la seguente istruzione:

        java -jar clientApp.jar -cli

    Quando viene richiesto l'inserimento dell'indirizzo IP, sarà
    necessario inserire l'indirizzo IP del Server e successivamente
    premere *"Enter"*.

### GUI
Prerequisiti: è necessario avere installato sul computer il Java SE
Development Kit 8 di Oracle (reperibile presso il link [*JDKDownload*](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html))

-   **(1)** Una volta scaricato il JAR del client (vedi link sopra),
    sarà sufficiente fare doppio click sul file clientApp.jar, sia su
    Windows che su MacOs. Quando viene richiesto l'inserimento
    dell'indirizzo IP, sarà sufficiente premere sul bottone *"Connect &
    Play"*, lasciando vuota la casella di testo.

-   **(2)** Una volta scaricato il JAR del client (vedi link sopra),
    sarà sufficiente fare doppio click sul file clientApp.jar, sia su
    Windows che su MacOs. Quando viene richiesto l'inserimento
    dell'indirizzo IP, bisognerà inserire nella casella di testo
    l'indirizzo IP del Server e successivamente premere sul bottone
    *"Connect & Play"*.

### Consigli per Architettura Distribuita
Nel caso si volesse utilizzare un Personal Computer come Server si
consiglia di:
 - Scaricare l'applicativo gratuito [*LogMeInHamachi*](https://www.vpn.net/) 
- Creare un account (seguendo le indicazioni fornite dall'applicazione)
- Creare una stanza ("Rete" --> "Crea nuova rete...")
- Farvi accedere i partecipanti ("Rete" --> "Partecipa a rete esistente...") 
- Una volta dentro la stanza, uno dei partecipanti esegue il punto **(2)** per il Server. 
- Chi vorrà partecipare come client eseguirà il punto **(2)** per CLI o GUI, a seconda dell'interfaccia che preferisce.

L'indirizzo del Server sarà quello indicato su LogMeIn Hamachi del partecipante che ha eseguito il punto **(2)** per il Server.
