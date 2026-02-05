package unics.ExecUtil;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbTest {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5433/unics";
        String user = "postgres";     // adapte si besoin
        String password = "postgres";

        try(
		Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connexion PostgreSQL OK !");
            conn.close();
        }
    }
}
