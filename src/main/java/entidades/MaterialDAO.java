package entidades;

import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public List<Material> obtemMateriais() {
        List<Material> materiais = new ArrayList<>();
        String query = "SELECT idMaterial,nomeMaterial,nomeTipoMaterial,reserva,GROUP_CONCAT(idMaterial in (" +
                "select idMaterial from MaterialTemEmprestimo)) AS temEmprestimo from Material NATURAL JOIN " +
                "TipoMaterial GROUP BY idMaterial";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idMaterial = rs.getInt("idMaterial");
                int reserva = rs.getInt("reserva");
                String nomeMaterial = rs.getString("nomeMaterial");
                String tipoMaterial = rs.getString("nomeTipoMaterial");
                boolean temEmprestimo = rs.getBoolean("temEmprestimo");
                Material material = new Material(idMaterial, nomeMaterial, tipoMaterial, reserva, temEmprestimo);
                materiais.add(material);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERRO:" + e.toString());
        }
        return materiais;
    }

    public boolean verificaMaterialEmprestado(int idMaterial) {
        boolean existe = false;
        String query = "SELECT idMaterial FROM MaterialTemEmprestimo where idMaterial = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(query)) {

            stmt.setInt(1, idMaterial);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                existe = true;
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("ERRO:" + e.toString());
        }
        return existe;
    }
}
