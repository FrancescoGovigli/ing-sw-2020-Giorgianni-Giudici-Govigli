package it.polimi.ingsw.PSP42.client;

public class ClientApp {

    /**
     * Main used to run CLI if args is specified else GUI.
     * @param args possible arguments from terminal
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-cli"))
                AppCLI.main(args);
        }
        else
            AppGUI.main(args);
    }
}
