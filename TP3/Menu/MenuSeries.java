package Menu;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import Arquivo.*;
import Entidades.*;


public class MenuSeries {
    
    ArquivoSeries arqSeries;
    ArquivoAtor arqAtores;
    private static Scanner console = new Scanner(System.in);

    public MenuSeries() throws Exception {
        arqSeries = new ArquivoSeries();
        arqAtores = new ArquivoAtor();
    }

    public void menu() {

        int opcao;
        do {
            System.out.println("PUCFlix 1.0");
            System.out.println("-------");
            System.out.println("> Início > Séries");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Incluir Atores");
            System.out.println("0 - Voltar ao menu anterior");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarSerie();
                    break;
                case 2:
                    incluirSerie();
                    break;
                case 3:
                    alterarSerie();
                    break;
                case 4:
                    excluirSerie();
                    break;
                case 5:
                    updateAtor();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }


    public void buscarSerie() {
        System.out.println("\nBusca de Série");
        String nome;
        boolean nomeValido = false;

        do {
            System.out.print("\nNome: ");
            nome = console.nextLine();  

            if(nome.isEmpty())
                return;
            else nomeValido = true;
        } while (!nomeValido);

        try {
            boolean encontrou = false;
            Series[] Serie = arqSeries.readNome(nome);  
            if (Serie != null) {
                for(int i = 0; i < Serie.length; i++) {
                    if (Serie[i].getNome().equals(nome)) {
                        System.out.println("Serie encontrada");
                        encontrou = true;
                        System.out.println("Atores que pertecem a serie");
                        int num = Serie[i].getId();
                        console.nextLine();
                        Ator[] d = arqAtores.readSeriesAtor(num);
                        for (int j = 0; j < d.length; j++) {
                            System.out.println(j + " " + d[j].getNome());
                        }
                        
                    }
                }   
                if (!encontrou) {
                    System.out.println("Serie não encontrada");
                } 
            }
        } catch(Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar a Serie!");
            e.printStackTrace();
        }
    }   


    public void incluirSerie() {
        System.out.println("\nInclusão de Serie");
        String nome = "";
        String sinopse = "";
        LocalDate dataNascimento = null;
        String stream = "";
        boolean dadosCorretos = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        do {
            System.out.print("\nNome (min. de 4 letras ou vazio para cancelar): ");
            nome = console.nextLine();
            if(nome.length()==0)
                return;
            if(nome.length()<4)
                System.err.println("O nome da Serie deve ter no mínimo 4 caracteres.");
        } while(nome.length()<4);

        do {
            System.out.print("Sinopse: ");
            sinopse = console.nextLine();
            if(sinopse.length()!=0)
                System.err.println("a sinopse deve conter pelo menos um caracter.");
        } while(sinopse.length()!=0);

        do {
            System.out.print("Stream: ");
            stream = console.nextLine();
            if(stream.length()!=0)
                System.err.println("Porfavor, informe onde se encontra a série.");
        } while(stream.length()!=0);

        do {
            System.out.print("Data de lançamento (DD/MM/AAAA): ");
            String dataStr = console.nextLine();
            dadosCorretos = false;
            try {
                dataNascimento = LocalDate.parse(dataStr, formatter);
                dadosCorretos = true;
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato DD/MM/AAAA.");
            }
        } while(!dadosCorretos);

        System.out.print("\nConfirma a inclusão da Serie? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if(resp=='S' || resp=='s') {
            try {
                Series c = new Series(nome, sinopse, dataNascimento, stream);
                arqSeries.create(c);
                System.out.println("Série incluída com sucesso.");
            } catch(Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir a Série!");
            }
        }
    }

    public void alterarSerie() {
        System.out.println("\nAlteração de Série");

        System.out.print("Nome da Série: ");
        String nome = console.nextLine();
        System.out.println();

        try {
            Series[] serie = arqSeries.readNome(nome);
            if (serie != null) {
                
                for (int i=0; i < serie.length; i++) {
                    System.out.println("\t[" + i + "]");
                    mostraSerie(serie[i]);
                }

                System.out.print("Digite o número da série a ser atualizada: ");
                int num = console.nextInt();
                console.nextLine();

                //testar se o numero digitado e' valido
                if (num >= 0 && serie[num] != null) {

                    //------------- Dados a serem atualizados ----------------//
                    System.out.print("Novo nome (ou Enter para manter): ");
                    String novoNome = console.nextLine();
                    if (!novoNome.isEmpty()) {
                        serie[num].setNome(novoNome);
                    }

                    System.out.print("Novo ano de lançamento (ou Enter para manter): ");
                    String ano = console.nextLine();
                    if (!ano.isEmpty()) {
                        LocalDate anoS = LocalDate.parse(ano + "-01-01"); // Apenas o ano
                        serie[num].setLancamento(anoS);
                    }

                    System.out.print("Nova sinopse (ou Enter para manter): ");
                    String novaSinopse = console.nextLine();
                    if (!novaSinopse.isEmpty()) {
                        serie[num].setSinopse(novaSinopse);
                    }

                    System.out.print("Novo streaming (ou Enter para manter): ");
                    String novoStreaming = console.nextLine();
                    if (!novoStreaming.isEmpty()) {
                        serie[num].setStream(novoStreaming);
                    }


                    System.out.print("\nConfirma as alterações? (S/N) ");
                    char resp = console.nextLine().charAt(0);

                    if (resp == 'S' || resp == 's') {
                        boolean alterado = arqSeries.update(serie[num]);
                        if (alterado) {
                            System.out.println("Série alterada com sucesso.");
                        } else {
                            System.out.println("Erro ao alterar a série.");
                        }
                    } else {
                        System.out.println("Alterações canceladas.");
                    }
                } else {
                    System.out.println("Não há serie associada a esse número.");
                }
            } else {
                System.out.println("Série não encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar série.");
        }
    }


    public void excluirSerie() {
        System.out.println("\nExclusão de Serie");
        String nome;
        boolean nomeValido = false;

        do {
            System.out.print("\nnome: ");
            nome = console.nextLine();  

            if(nome.isEmpty())
            return; 
            else {
                nomeValido = true;
            }
        } while (!nomeValido);

        try {
            Series[] serie = arqSeries.readNome(nome);
            if (serie != null) {
                for(int i = 0; i < serie.length; i++) {
                    System.out.println(i + " " + serie[i].getNome());
                }
                System.out.println("Digite o numero: ");
                int numSerie = console.nextInt();

                boolean excluido = arqSeries.delete(serie[numSerie].getId());

                if (excluido) {
                    System.out.println("Serie excluida com êxito!");
                }
                else {
                    System.out.println("Nao foi possivel excluir a série.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir a Serie!");
            e.printStackTrace();
        }
    }
    
    public void updateAtor() {
        System.out.println("\nInclução de Ator");

        System.out.print("Nome da Série: ");
        String nome = console.nextLine();
        System.out.println();

        try {
            Series[] serie = arqSeries.readNome(nome);
            if (serie != null) {
                
                for (int i=0; i < serie.length; i++) {
                    System.out.println("\t[" + i + "]");
                    mostraSerie(serie[i]);
                }

                System.out.print("Digite o número da série a ser atualizada: ");
                int num = console.nextInt();
                console.nextLine();

                //testar se o numero digitado e' valido
                if (num >= 0 && serie[num] != null) {
                    System.out.print("Nome do Ator: ");
                    String nomes = console.nextLine();
                    System.out.println();
                    Ator[] ator = arqAtores.readNome(nomes);
                    if (ator != null) {
                
                            for (int i=0; i < ator.length; i++) {
                                System.out.print("\t[" + i + "]\t");
                                System.out.println(ator[i].nome);
                            }
                            System.out.print("Digite o número do ator a ser inserido: ");
                            int numa = console.nextInt();
                            console.nextLine();
                        if (numa >= 0 && serie[num] != null) {
                            boolean alterado = arqSeries.updateAtor(serie[num], ator[numa]); 
                            if (alterado) {
                                System.out.println("Série alterada com sucesso.");
                            } else {
                                System.out.println("Erro ao alterar a Ator.");
                            }
                        }
                        }
                }
            }
                
        } catch (Exception e) {
            System.out.println("Erro ao alterar série.");
        }
    }
    


    public void mostraSerie(Series Serie) {
    if (Serie != null) {
        System.out.println("\nDetalhes da Serie:");
        System.out.println("----------------------");
        System.out.printf("Nome......: %s%n", Serie.nome);
        System.out.printf("Sinopse.......: %s%n", Serie.sinopse);
        System.out.printf("Stream.......: %s%n", Serie.stream);
        System.out.printf("Lançamento: %s%n", Serie.lancamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("----------------------");
    }
}
}
