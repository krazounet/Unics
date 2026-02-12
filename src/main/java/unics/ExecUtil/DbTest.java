package unics.ExecUtil;

import java.sql.Connection;
import java.sql.DriverManager;

import dbPG18.DbUtil;

public class DbTest {

    public static void main(String[] args) throws Exception {
       /*
    	String url = "jdbc:postgresql://localhost:5433/unics";
        String user = "postgres";     // adapte si besoin
        String password = "postgres";
*/
        try(
		Connection conn = DbUtil.getConnection()) {
            System.out.println("Connexion PostgreSQL OK !");
            conn.close();
        }
        
        
        
    }
}
