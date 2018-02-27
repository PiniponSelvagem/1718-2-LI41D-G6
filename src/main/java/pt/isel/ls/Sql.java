package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Nuno on 27/02/2018.
 */
public class Sql {
    public static Connection CreateConnetion() throws SQLServerException {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser( System.getenv("LS-user"));
        ds.setPassword(System.getenv("LS-pass"));
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setDatabaseName(System.getenv("LS-server"));

        return ds.getConnection();
    }

}

