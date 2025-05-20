import Menu.*;
import java.util.Scanner;

public class Principal {

public static void main(String[] args) {

    Scanner console;

    try {
        console = new Scanner(System.in);
        int opcao;
        do {

            System.out.println("\n\nPUCFlix");
            System.out.println("-------");
            System.out.println("> Inicio");
            System.out.println("\n1 - Series");
            System.out.println("2 - Episodios");
            System.out.println("3 - Atores");
            System.out.println("0 - Sair");

            System.out.print("\nOpcao: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    new MenuSeries().menu();
                    break;
                case 2:
                    new MenuEpisodio().menu();
                break;
                case 3:
                    new MenuAtores().menu();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }

        } while (opcao != 0);



    } catch(Exception e) {
        e.printStackTrace();
    }

}

}