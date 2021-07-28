package entidades;

import db.ConnectionFactory;
import relatorios.EmprestimoEmAndamento;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class EmprestimoDAO {

    public static String FIM_SEMESTRE_1 = "-07-10 23:59:59";
    public static String FIM_SEMESTRE_2 = "-12-17 23:59:59";

    public List<EmprestimoEmAndamento> obtemEmAndamento() {
        Map<Integer, EmprestimoEmAndamento> mapaEmprestimos = new HashMap<Integer, EmprestimoEmAndamento>();
        String query = "SELECT DISTINCT e.idEmprestimo,a.matricula,nome,sobrenome,nomeMaterial,dataSaida,dataDevolucao,tipoAtividade,renovacoes " +
                "from Emprestimo e inner join MaterialTemEmprestimo mte " +
                "ON e.idEmprestimo = mte.idEmprestimo inner join Material m ON " +
                "mte.idMaterial = m.idMaterial inner join Aluno a ON e.matricula = a.matricula " +
                "INNER JOIN Atividade at ON e.idAtividade = at.idAtividade where e.dataEntrega is NULL ORDER BY e.idEmprestimo";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idEmprestimo = rs.getInt("idEmprestimo");
                String nomeMaterial = rs.getString("nomeMaterial");

                if (mapaEmprestimos.containsKey(idEmprestimo)) {
                    mapaEmprestimos.get(idEmprestimo).adicionaMaterial(nomeMaterial);
                } else {
                    String nome = rs.getString("nome");
                    String sobrenome = rs.getString("sobrenome");
                    Timestamp dataSaida = rs.getTimestamp("dataSaida");
                    Timestamp dataDevolucao = rs.getTimestamp("dataDevolucao");
                    String atividade = rs.getString("tipoAtividade");
                    int renovacoes = rs.getInt("renovacoes");
                    int matricula = rs.getInt("matricula");

                    EmprestimoEmAndamento eae = new EmprestimoEmAndamento(idEmprestimo, atividade, matricula,
                            dataSaida, dataDevolucao, renovacoes, nome, sobrenome);
                    eae.adicionaMaterial(nomeMaterial);

                    mapaEmprestimos.put(idEmprestimo, eae);
                }
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("erro:" + e.toString());
        }

        return new ArrayList<EmprestimoEmAndamento>(mapaEmprestimos.values());
    }

    public List<Emprestimo> obtemEmprestimosPorAluno(int matricula, int limite) {
        Map<Integer, Emprestimo> mapaEmprestimos = new HashMap<Integer, Emprestimo>();
        String query = "SELECT * FROM Emprestimo NATURAL JOIN Atividade NATURAL JOIN MaterialTemEmprestimo " +
                "NATURAL JOIN Material WHERE matricula = ? ORDER BY idEmprestimo DESC LIMIT ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(query)) {

            stmt.setInt(1, matricula);
            stmt.setInt(2, limite);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idEmprestimo = rs.getInt("idEmprestimo");
                int idMaterial = rs.getInt("idMaterial");

                if (mapaEmprestimos.containsKey(idEmprestimo)) {
                    mapaEmprestimos.get(idEmprestimo).adicionaMaterial(idMaterial);
                } else {
                    String atividade = rs.getString("tipoAtividade");
                    Timestamp dataSaida = rs.getTimestamp("dataSaida");
                    Timestamp dataDevolucao = rs.getTimestamp("dataDevolucao");
                    Timestamp dataEntrega = rs.getTimestamp("dataEntrega");
                    int renovacoes = rs.getInt("renovacoes");
                    boolean penalidade = rs.getBoolean("penalidade");

                    Emprestimo emprestimo = new Emprestimo(idEmprestimo, atividade, matricula, dataSaida,
                            dataDevolucao, dataEntrega, renovacoes, penalidade);
                    emprestimo.adicionaMaterial(idMaterial);

                    mapaEmprestimos.put(idEmprestimo, emprestimo);
                }
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERRO:" + e.toString());
        }

        return new ArrayList<Emprestimo>(mapaEmprestimos.values());
    }

    private java.sql.Timestamp obtemDataDevolucao(int atividade) throws ParseException {
        // TCC
        if (atividade == 1) {
            String ano = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            if (Calendar.getInstance().get(Calendar.MONTH) <= 7)
                return java.sql.Timestamp.valueOf(ano + EmprestimoDAO.FIM_SEMESTRE_1);
            else
                return java.sql.Timestamp.valueOf(ano + EmprestimoDAO.FIM_SEMESTRE_2);
        }

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 15);
        return new java.sql.Timestamp(c.getTime().getTime());
    }

    public boolean realizaEmprestimo(int matricula, int atividade, List<Integer> materiais) {
        String query1 = "INSERT INTO Emprestimo(idAtividade, matricula, dataSaida, dataDevolucao, renovacoes) " +
                "VALUES (?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO MaterialTemEmprestimo(idMaterial, idEmprestimo) VALUES (?, ?)";
        boolean inseriu = false;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt1 = conexao.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmt2 = conexao.prepareStatement(query2)) {

            stmt1.setInt(1, atividade);
            stmt1.setInt(2, matricula);
            java.util.Date date = new Date();
            stmt1.setTimestamp(3, new java.sql.Timestamp(date.getTime()));
            stmt1.setTimestamp(4, obtemDataDevolucao(atividade));
            stmt1.setInt(5, 0);

            stmt1.executeUpdate();
            ResultSet rs = stmt1.getGeneratedKeys();
            int materiaisInseridos = 0;
            int idEmprestimo = 0;
            if (rs.next()) {
                idEmprestimo = rs.getInt(1);

                System.out.println("ID EMPRESTIMO: " + idEmprestimo);
                for (Integer m : materiais) {
                    stmt2.setInt(1, m);
                    stmt2.setInt(2, idEmprestimo);
                    if (stmt2.executeUpdate() < 0)
                        System.out.println("%% Material " + m + " não pode ser inserido.");
                    else
                        materiaisInseridos++;
                }
            } else {
                return false;
            }

            if (materiaisInseridos == 0) {
                String deleteQuery = "DELETE FROM Emprestimo WHERE idEmprestimo = " + idEmprestimo;
                PreparedStatement stmtDelete = conexao.prepareStatement(deleteQuery);
                stmtDelete.execute();
                return false;
            }

            inseriu = true;
        } catch (Exception e) {
            System.out.println("erro: " + e.toString());
        }

        return inseriu;
    }

    public Emprestimo obtemEmprestimoAluno(int matricula) {
        Emprestimo emprestimo = null;
        String query = "SELECT * FROM Emprestimo NATURAL JOIN Atividade NATURAL JOIN MaterialTemEmprestimo " +
                "NATURAL JOIN Material WHERE matricula = ? AND dataEntrega is NULL ORDER BY idEmprestimo DESC";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(query)) {

            stmt.setInt(1, matricula);
            ResultSet rs = stmt.executeQuery();
            int entradas = 0;
            while (rs.next()) {
                entradas++;
                int idMaterial = rs.getInt("idMaterial");
                if (entradas == 1) {
                    int idEmprestimo = rs.getInt("idEmprestimo");
                    String atividade = rs.getString("tipoAtividade");
                    Timestamp dataSaida = rs.getTimestamp("dataSaida");
                    Timestamp dataDevolucao = rs.getTimestamp("dataDevolucao");
                    Timestamp dataEntrega = rs.getTimestamp("dataEntrega");
                    int renovacoes = rs.getInt("renovacoes");
                    boolean penalidade = rs.getBoolean("penalidade");

                    emprestimo = new Emprestimo(idEmprestimo, atividade, matricula, dataSaida,
                            dataDevolucao, dataEntrega, renovacoes, penalidade);
                    emprestimo.adicionaMaterial(idMaterial);
                } else {
                    emprestimo.adicionaMaterial(idMaterial);
                }
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERRO:" + e.toString());
        }

        return emprestimo;
    }

    private java.sql.Timestamp obtemDataDevolucaoRenovada(Timestamp dataDevolucao, String atividade) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(dataDevolucao);

        // TCC
        if (atividade.equals("TCC")) {
            Date fimProximoSemestre = null;

            int ano = Calendar.getInstance().get(Calendar.YEAR);
            if (Calendar.getInstance().get(Calendar.MONTH) <= 7) {
                fimProximoSemestre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ano + EmprestimoDAO.FIM_SEMESTRE_2);
            }
            else {
                ano += 1;
                fimProximoSemestre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ano + EmprestimoDAO.FIM_SEMESTRE_1);
            }

            c.setTime(fimProximoSemestre);
        } else {
            c.add(Calendar.DATE, 15); // renova por mais 15 dias

            Date dateFimSemestre = null;
            int ano = Calendar.getInstance().get(Calendar.YEAR);
            if (Calendar.getInstance().get(Calendar.MONTH) <= 7)
                dateFimSemestre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ano + EmprestimoDAO.FIM_SEMESTRE_1);
            else
                dateFimSemestre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ano + EmprestimoDAO.FIM_SEMESTRE_2);

            Calendar fimSemestre = Calendar.getInstance();
            fimSemestre.setTime(dateFimSemestre);
            if (c.compareTo(fimSemestre) > 0)
                c.setTime(dateFimSemestre);

        }

        return new java.sql.Timestamp(c.getTime().getTime());
    }

    public Date renovaEmprestimo(Emprestimo emprestimo) {
        Date dataDevolucao = null;
        String query = "UPDATE Emprestimo SET " +
                "renovacoes = ?, " +
                "dataDevolucao = ?" +
                "where idEmprestimo = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(query)) {

            java.sql.Timestamp devolucao = obtemDataDevolucaoRenovada(emprestimo.getDataDevolucao(), emprestimo.getAtividade());
            stmt.setInt(1, emprestimo.getRenovacoes() + 1);
            stmt.setTimestamp(2, devolucao);
            stmt.setInt(3, emprestimo.getIdEmprestimo());

            stmt.executeUpdate();
            if (stmt.executeUpdate() > 0)
                dataDevolucao = new Date(devolucao.getTime());

        } catch (Exception e) {
            System.out.println("erro: " + e.toString());
        }

        return dataDevolucao;
    }

    public boolean finalizaEmprestimo(Emprestimo emprestimo) {
        boolean finalizado = false;
        String query = "UPDATE Emprestimo SET " +
                "dataEntrega = ?, " +
                "penalidade = ? " +
                "WHERE idEmprestimo = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(query)) {

            java.util.Date dataEntrega = new Date();
            stmt.setTimestamp(1, new java.sql.Timestamp(dataEntrega.getTime()));

            if (dataEntrega.compareTo(emprestimo.getDataDevolucao()) > 0)
                stmt.setBoolean(2, true);
            else
                stmt.setBoolean(2, false);

            stmt.setInt(3, emprestimo.getIdEmprestimo());
            stmt.executeUpdate();
            if (stmt.executeUpdate() > 0)
                finalizado = true;

        } catch (Exception e) {
            System.out.println("erro: " + e.toString());
        }

        return finalizado;
    }

}
