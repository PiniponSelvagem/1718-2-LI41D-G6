package pt.isel.ls.sql;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

/**
 * Created by Nuno on 27/02/2018.
 */
public class Sql {
    private static SQLServerDataSource ds = null;
    private static void CreateConnetion() throws SQLServerException {
        ds = new SQLServerDataSource();
        ds.setUser( System.getenv("LS-user"));
        ds.setPassword(System.getenv("LS-pass"));
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setDatabaseName(System.getenv("LS-server"));
        //return ds.getConnection();
    }
    static {
        try {
            CreateConnetion();
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLServerException {
        return ds.getConnection();
    }
}

