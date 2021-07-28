import core.GerenciadorEmprestimos;

import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        GerenciadorEmprestimos gerenciadorEmprestimos = new GerenciadorEmprestimos();
        boolean sair = false;
        int opcao = 0;
        Scanner scanner = new Scanner(System.in);

        while (!sair) {
            System.out.println(" ### Selecione uma das Opções");
            System.out.println("\n\t## EMPRÉSTIMOS");
            System.out.println("\t\t1 - Efetuar empréstimo");
            System.out.println("\t\t2 - Renovar empréstimo");
            System.out.println("\t\t3 - Finalizar empréstimo");
            System.out.println("\n\t## RELATÓRIOS");
            System.out.println("\t\t4 - Empréstimos em andamento");
            System.out.println("\t\t5 - Empréstimos por equipamento");
            System.out.println("\t\t6 - Equipamentos emprestados por determinado aluno");
            System.out.println("\t\t7 - Empréstimos em andamento já vencidos");
            System.out.println("\n\t\t0 - Sair");
            System.out.println("Opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    gerenciadorEmprestimos.efetuarEmprestimo();
                    break;
                case 2:
                    gerenciadorEmprestimos.renovarEmprestimo();
                    break;
                case 3:
                    gerenciadorEmprestimos.finalizaEmprestimo();
                    break;
                case 4:
                    gerenciadorEmprestimos.geraRelatorioEmAndamento();
                    break;
                case 5:
                case 6:
                case 7:
                    System.out.println("Digitou " + opcao);
                    break;
                case 0:
                    sair = true;
                    break;
                default:
                    System.out.println("Opção Inválida.");
                    break;

            }
        }




    }

}

