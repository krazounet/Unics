package dbPG18;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbUtil {
	//private static boolean useSUPABASE=false;
    public DbUtil() {}

    public static Connection getConnection() throws SQLException {
    	
        
    	Connection conn = DriverManager.getConnection(
    	        System.getenv("DB_URL"),
    	        System.getenv("DB_USER"),
    	        System.getenv("DB_PASSWORD")
    	    );

    	  //  conn.setAutoCommit(true);   // 🔥 FORÇAGE EXPLICITE

    	    return conn;
    }

}
