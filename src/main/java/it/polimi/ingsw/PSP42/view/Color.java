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
    public static final String RESET = "\u001B[0m"; // reset to default value
    //In this case "values()" method
    // (automatically generate in Java),
    // that returns an array of all elements contains in this Enum,
    // doesn't return the elements.
    // At the end,
    // “RESET” isn't a color!

    private String escape; // contains text code to modify the color
    Color(String escape)
    {
        this.escape = escape;
    }
    public String getEscape()
    {
        return escape;
    }

    /**
     * Used, implicitly, to return an object in String.
     * @return a string
     */
    @Override
    public String toString()
    {
        return escape;
    }
}
