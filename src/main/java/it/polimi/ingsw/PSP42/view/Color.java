package it.polimi.ingsw.PSP42.view;

public enum Color
{
    ANSI_SMILE("\u1F60"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[38;5;70m"),
    ANSI_WATER("\u001B[36m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_REVERSE("\u001B[7m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m");
    static final String RESET = "\u001B[0m"; // resetta il valore a un default
    //In questo modo il metodo values() (che Java genera in
    //automatico e restituisce un array di tutti gli elementi della
    //enum) non lo restituisce
    //In fondo,
    //“RESET” non è un colore!

    private String escape; // contiene il codice testo che modifica il colore
    Color(String escape)
    {
        this.escape = escape;
    }
    public String getEscape()
    {
        return escape;
    }

    /**
     * Il metodo toString() è speciale perché viene
     * chiamato da Java implicitamente quando serve
     * convertire un oggetto in stringa
     * @return
     */
    @Override
    public String toString()
    {
        return escape;
    }
}
