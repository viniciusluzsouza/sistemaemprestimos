package entidades;

import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AlunoDAO {

    public Aluno obtemAluno(int matricula) {
        Aluno aluno = null;
        String query = "SELECT * FROM Aluno NATURAL JOIN AlunoFazCurso WHERE matricula = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(query)) {

            stmt.setInt(1, matricula);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nome = rs.getString("nome");
                String sobrenome = rs.getString("sobrenome");
                int idCurso = rs.getInt("idCurso");
                boolean ativo = rs.getBoolean("ativo");
                aluno = new Aluno(matricula, nome, sobrenome, idCurso, ativo);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERRO:" + e.toString());
        }

        return aluno;
    }

}
