package fr.marvinfm.lobotomie;

import java.util.Scanner;

/* Pas encore d'interface graphique pour l'instant, 
// la première version sera en ligne de commande.
import javafx.application.Application;
import javafx.stage.Stage;
*/

public class LobotomieApp /*extends Application*/ {

    /*@Override
    public void start(Stage stage) {
        // new fr.marvinfm.lobotomie.ui.MainWindow().show(stage);
    }*/

    public static void main(String[] args) {
        
        System.out.println("\n");
        System.out.println("==================");
        System.out.println("   LobotomieApp   ");
        System.out.println("==================");

        Scanner sc = new Scanner(System.in);

        boolean exit = false;

        System.out.println("Entrez une valeur..");
        
        do {
            String value = sc.nextLine();
            
            boolean result[] = handle(value);

            exit = result[0];

        } while(!exit);
        
        sc.close();
    }

    private static boolean[] handle(String input) {

        boolean exit = false;

        switch(input){

            case "test":
                System.out.println("Test...");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                eraseLines(3);
                defaultLine();
                break;

            case "exit":
                exit = true;
                break;

            default:
                break;
        }
        
        boolean instructions[] = {exit};
        return instructions;
    }

    private static void eraseLines(int count) {
        if (count <= 0) {
            return;
        }
    
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("\033[1A\033[2K");
        }
        sb.append("\r");
    
        System.out.print(sb.toString());
        System.out.flush();
    }

    private static void defaultLine(){
        System.out.println("Entrez une valeur..");
        return;
    }
}
