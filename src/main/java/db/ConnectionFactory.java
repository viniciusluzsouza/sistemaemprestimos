package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class ConnectionFactory {

    private static Connection conexao;

    public static synchronized Connection getConnection() {

        String host = "127.0.0.1";
        String port = "3306";
        String dbName = "bcd_projeto1";
        String user = "projeto1_user";
        String password = "1234";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

        try {
            conexao = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conexao;
    }

}
