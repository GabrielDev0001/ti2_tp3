package Menu;

import Arquivo.*;
import Entidades.*;
import java.util.Scanner;

public class MenuAtores {
    ArquivoAtor arqAtor;
    ArquivoSeries arqSeries;
    private static Scanner console = new Scanner(System.in);


    public MenuAtores() throws Exception {
        arqAtor = new ArquivoAtor();
        arqSeries = new ArquivoSeries();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("PUCFlix 1.0");
            System.out.println("----------");
            System.out.println("> Início > Atores");
            System.out.println("\n1) Incluir");
            System.out.println("2) Ver Series feitas");
            System.out.println("3) Alterar");
            System.out.println("4) Excluir");
            System.out.println("0) Voltar ao menu anterior");

            System.out.print("\nOpção: ");
            try {
                opcao = console.nextInt();
            } catch(NumberFormatException e) {
                opcao = -1;
            }
            switch(opcao) {
                case 1: 
                    incluirAtor();
                    break;
                case 2: 
                    buscarAtor();
                    break;
                case 3: 
                    alterarAtor();
                    break;
                case 4: 
                    excluirAtor();
                    break;
                case 0:
                    break;
                default:
                    break;
            }
        } while (opcao != 0);
    }

    public void incluirAtor() {
        System.out.println("\nInclusão de Ator");
        String nome = "";

        System.out.println("\nBusca de Ator");
        String descobre;
        boolean nomeAtorValido;
        int numSerie = 0;
     
        do {
            System.out.print("\nDigite o nome: ");
            descobre = console.nextLine();  
     
            if(descobre.isEmpty())
                return;
            else nomeAtorValido = true;
        } while (!nomeAtorValido);
     
        try {
            Ator[] s = arqAtor.readNome(descobre);
            for (int i = 0; i < s.length; i++) {
                System.out.println(i + " " + s[i].getNome());
            }
            System.out.println("Digite o numero(Digite 0 caso não esteja na lista): ");
            numSerie = console.nextInt();
            if(numSerie != 0){
                return;
            }
        }catch (Exception e) {
            System.out.println("Erro ao buscar a Ator: " + e.getMessage());
        }
     

        do {
            System.out.print("\nNome (min. de 4 letras ou vazio para cancelar): ");
            nome = console.nextLine();
            if(nome.length()==0)
                return;
            if(nome.length()<4)
                System.err.println("O nome do Ator deve ter no mínimo 4 caracteres.");
        } while(nome.length()<4);

        System.out.println("\nConfirma a inclusão do Ator? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if(resp=='S' || resp=='s') {
            try {

                Ator c = new Ator(nome);

                arqAtor.criarAtor(c);
                System.out.println("Ator incluído com sucesso.");
            } catch(Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir o ator!");
            }
        }else System.out.println("Inclusão cancelada");
    }

    public void excluirAtor() {
        System.out.println("\nExclusão do Ator");
        System.out.print("\nDigite o nome da Ator: ");

        String nome = console.nextLine();

        try {
            Ator[] s = arqAtor.readNome(nome);
            for (int i = 0; i < s.length; i++) {
                System.out.println(i + " " + s[i].getNome());
            }
            Ator[] e = arqAtor.readNome(nome);

            for (int i = 0; i < e.length; i++) {
                System.out.println(i + " " + e[i].getNome());
            }

            int numEp = console.nextInt();

            boolean excluido = arqAtor.excluirAtor(e[numEp].getId());
            if (excluido) {
                System.out.println("Exclusão efetuada com sucesso!");
            } else {
                System.out.println("Exclusão não efetuada.");
            }
        } 
        catch (Exception e) {
            System.out.println("Não foi possivel abrir o arquivo de Atores");
        }  
    }

    public void buscarAtor() {
        System.out.println("\nBusca de Ator");
        String nome;
        boolean nomeValido;

        do {
            System.out.print("\nDigite o nome: ");
            nome = console.nextLine();  

            if(nome.isEmpty())
                return;
            else nomeValido = true;
        } while (!nomeValido);

        try {
            Ator[] s = arqAtor.readNome(nome);
            for (int i = 0; i < s.length; i++) {
                int d = i+1;
                System.out.println(d + " " + s[i].getNome());
            }
            System.out.println("Digite o numero(Digite 0 caso não esteja na lista): ");
            int num = console.nextInt();
            console.nextLine();
            if (num > 0 && s[num] != null) {
                Series[] d = arqSeries.readAtoSeries(num);
                for (int i = 0; i < d.length; i++) {
                    System.out.println(i + " " + s[i].getNome());
                }
            }

        } catch(Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o Ator!");
            e.printStackTrace();
        }
    } 

    public void alterarAtor() {
        System.out.println("\nAlteração do Ator");
        String nome;
        boolean nomeValido;
        do {
            System.out.print("\nnome da Ator: ");
            nome = console.nextLine();  
            if(nome.isEmpty())
                return; 
            else {
                nomeValido = true;
            }
        } while (!nomeValido);
        try {
            Ator[] s = arqAtor.readNome(nome);
            for (int i = 0; i < s.length; i++) {
                System.out.println(i + " " + s[i].getNome());
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar o Ator!");
            e.printStackTrace();
        }
    }
     
    public void mostraAtores(Ator Ator) {
        if (Ator != null) {
            System.out.println("\nDetalhes do Ator:");
            System.out.println("----------------------");
            System.out.printf("Nome......: %s%n", Ator.getNome());
            System.out.printf("ID do Ator: %s%n", Ator.getId());
            //Implementar chave estrangeira

            //
            System.out.println("----------------------");
        }
    }

}
