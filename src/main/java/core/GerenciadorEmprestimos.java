package core;

import entidades.*;
import relatorios.EmprestimoEmAndamento;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GerenciadorEmprestimos {

    private Map<Integer, Material> printMateriaisERetornaMap(List<Material> materiais) {
        Map<Integer,Material> materiaisMap = new HashMap<Integer, Material>();
        System.out.println("  ID  |       MATERIAL       |    TIPO    | EMPRESTADO |  RESERVA");
        System.out.println("-----------------------------------------------------------------");
        for (Material m : materiais) {
            materiaisMap.put(m.getIdMaterial(), m);
            String line = String.format("%1$-6s", Integer.toString(m.getIdMaterial())) +
                    "|" + String.format("%1$-22s", m.getNomeMaterial()) +
                    "|" + String.format("%1$-12s", m.getTipoMaterial()) +
                    "|" + String.format("%1$-12s", m.isTemEmprestimo() ? "     SIM" : "     NÃO") +
                    "|" + String.format("%1$6s", m.getReservaBoolean() ? "SIM" : "NÃO");
            System.out.println(line);
        }
        return materiaisMap;
    }

    private List<Integer> parseIdMateriais(String materiaisStr) {
        List<Integer> materiais = new ArrayList<>();
        List<String> idsString = Arrays.asList(materiaisStr.split("\\s*,\\s*"));
        for (String ids : idsString)
            materiais.add(Integer.valueOf(ids));

        return materiais;
    }

    public void efetuarEmprestimo() {
        MaterialDAO materialDAO = new MaterialDAO();
        List<Material> materiaisList = materialDAO.obtemMateriais();
        Map<Integer, Material> materiais = printMateriaisERetornaMap(materiaisList);

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nInforme os IDs dos materiais separado por vírgula (Ex.: 10,12)");
        String materiaisStr = scanner.nextLine();
        List<Integer> idsMateriais = parseIdMateriais(materiaisStr);

        for (Integer id: idsMateriais) {
            if (!materiais.containsKey(id)) {
                System.out.println("ID informado " + id + " é invalido.");
                return;
            }

            Material material = materiais.get(id);
            if (material.getReservaBoolean()) {
                System.out.println("O material já " + material.getNomeMaterial() + " (" + id + ")" +  " já possui uma reserva.");
                return;
            }

            // Verifica se o material ja esta emprestado
            if (material.isTemEmprestimo()) {
                System.out.println("O material " + material.getNomeMaterial() + " (" + id + ")" + " já está emprestado.");
                return;
            }

        }

        System.out.println("\nInforme a matricula: ");
        int matricula = scanner.nextInt();
        AlunoDAO alunoDAO = new AlunoDAO();
        Aluno aluno = alunoDAO.obtemAluno(matricula);

        if (aluno == null) {
            System.out.println("Matricula inválida. Aluno não encontrado ou inativo.");
            return;
        }

        // Verifica se o aluno está ativo.
        if (!aluno.isAtivo()) {
            System.out.println("Aluno inativo. Não pode efetuar empréstimo.");
            return;
        }

        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        List<Emprestimo> emprestimos = emprestimoDAO.obtemEmprestimosPorAluno(matricula, 1);
        Emprestimo emprestimo = emprestimos.size() > 0 ? emprestimos.remove(0) : null;

        if (emprestimo != null) {
            // Verifica se o aluno possui emprestimo em andamento
            if (emprestimo.getDataEntrega() == null) {
                System.out.println("Aluno já possui emprestimo em andamento.");
                return;
            }

            // Verifica se existe penalidade para o aluno
            if (emprestimo.isPenalidade()) {
                long tempoPenalidade = 3*(emprestimo.getDataEntrega().getTime() - emprestimo.getDataDevolucao().getTime());
                Date fimPenalidade = new Date(emprestimo.getDataEntrega().getTime() + tempoPenalidade);
                Date agora = new Date();
                if (fimPenalidade.compareTo(agora) > 0) {
                    System.out.println("Aluno com penalidade.");
                    return;
                }
            }
        }

        System.out.println("\nInforme a atividade (1-TCC, 2-Ensino, 3-Pesquisa, 4-Extensao");
        int atividade = scanner.nextInt();

        if (emprestimoDAO.realizaEmprestimo(matricula, atividade, idsMateriais))
            System.out.println("Emprestimo efetuado!");

    }


    public void renovarEmprestimo() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\nInforme a matricula: ");
            int matricula = scanner.nextInt();

            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno aluno = alunoDAO.obtemAluno(matricula);

            if (aluno == null) {
                System.out.println("Matricula inválida. Aluno não encontrado ou inativo.");
                return;
            }

            EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
            Emprestimo emprestimo = emprestimoDAO.obtemEmprestimoAluno(matricula);
            if (emprestimo == null) {
                System.out.println("Aluno não possui empréstimos a renovar");
                return;
            }

            if (emprestimo.getRenovacoes() > 2) {
                System.out.println("Emprestimo excedeu o número máximo de renovações (3).");
                return;
            }

            Date fimSemestre = null;
            int ano = Calendar.getInstance().get(Calendar.YEAR);
            if (Calendar.getInstance().get(Calendar.MONTH) <= 7)
                fimSemestre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ano + EmprestimoDAO.FIM_SEMESTRE_1);
            else
                fimSemestre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ano + EmprestimoDAO.FIM_SEMESTRE_2);

            Date devolucao = emprestimo.getDataDevolucao();
            if (devolucao.compareTo(fimSemestre) >= 0 && !emprestimo.getAtividade().equals("TCC")) {
                System.out.println("Somente empréstimos para TCCs são permitidos ultrapassarem o fim do semestre");
                return;
            }

            Date novaDevolucao = emprestimoDAO.renovaEmprestimo(emprestimo);
            if (novaDevolucao != null)
                System.out.println("Empréstimo renovado até " + novaDevolucao.toString());
        } catch (Exception e) {
            System.out.println("ERRO:" + e.toString());
        }


    }


    public void finalizaEmprestimo() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\nInforme a matricula: ");
            int matricula = scanner.nextInt();

            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno aluno = alunoDAO.obtemAluno(matricula);

            if (aluno == null) {
                System.out.println("Matricula inválida. Aluno não encontrado ou inativo.");
                return;
            }

            EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
            Emprestimo emprestimo = emprestimoDAO.obtemEmprestimoAluno(matricula);
            if (emprestimo == null) {
                System.out.println("Aluno não possui empréstimos a renovar");
                return;
            }

            if (emprestimoDAO.finalizaEmprestimo(emprestimo))
                System.out.println("Empréstimo finalizado.");

        } catch (Exception e) {
            System.out.println("ERRO:" + e.toString());
        }
    }


    public void geraRelatorioEmAndamento() {
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        List<EmprestimoEmAndamento> listaEae = emprestimoDAO.obtemEmAndamento();

        System.out.println("ID    |MATRICULA|    NOME    |  SOBRENOME  |      DATA SAIDA     |   DATA DEVOLUCAO    |ATIVIDADE|   MATERIAIS");
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        for (EmprestimoEmAndamento eae : listaEae) {
            String material = eae.getMateriais().remove(0);
            String line = String.format("%1$-6s", eae.getIdEmprestimo()) +
                    "|" + String.format("%1$-9s", eae.getMatricula()) +
                    "|" + String.format("%1$-12s", eae.getNome()) +
                    "|" + String.format("%1$-13s", eae.getSobrenome()) +
                    "|" + String.format("%1$-21s", eae.getDataSaida().toString()) +
                    "|" + String.format("%1$-21s", eae.getDataDevolucao().toString()) +
                    "|" + String.format("%1$-9s", eae.getAtividade()) +
                    "|" + material;
            System.out.println(line);

            int inicio = line.indexOf(material);
            for (String m : eae.getMateriais()) {
                String formato = "%1$" + inicio + "s";
                String lineM = String.format(formato, "|") + m;
                System.out.println(lineM);
            }

        }
    }
}
