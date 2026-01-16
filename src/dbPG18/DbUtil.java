package dbPG18;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbUtil {

    private DbUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            DbConfig.URL,
            DbConfig.USER,
            DbConfig.PASSWORD
        );
    }
}
