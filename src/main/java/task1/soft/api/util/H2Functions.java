package task1.soft.api.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;



public class H2Functions {
    public static void truncateAllTables(Connection conn) throws SQLException {
        conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS=0");
        ResultSet rs = conn.createStatement().
                executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = SCHEMA()");
        while (rs.next()) {
            String tableName = rs.getString(1);
            conn.createStatement().executeUpdate("TRUNCATE TABLE \"" + tableName + "\" RESTART IDENTITY");
        }
        conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS=1");
    }
}