package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl {
    private ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess!");
        System.out.print(client.helpLoggedOut());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }



    private void printPrompt() {
        if(client.loggedIn()) {
            System.out.print("\n" + RESET_TEXT_COLOR + "[LOGGED IN] >>> ");
        } else if(client.inGame()){
            System.out.print("\n" + RESET_TEXT_COLOR + "[IN GAME] >>> ");
        } else {
            System.out.print("\n" + RESET_TEXT_COLOR + "[LOGGED OUT] >>> ");
        }

    }

}
