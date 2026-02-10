package dbPG18;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbUtil {
	private static boolean useSUPABASE=false;
    public DbUtil() {}

    public static Connection getConnection() throws SQLException {
    	if (useSUPABASE) return getConnectionSupabase();
        return DriverManager.getConnection(
            DbConfig.URL,
            DbConfig.USER,
            DbConfig.PASSWORD
        );
    }
    public static Connection getConnectionSupabase() throws SQLException {
    	System.out.println("Supabase!");
        return DriverManager.getConnection(
            DbConfigSUPABASE.URL,
            DbConfigSUPABASE.USER,
            DbConfigSUPABASE.PASSWORD
        );
    }
}
