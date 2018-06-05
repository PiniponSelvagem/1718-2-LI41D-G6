package pt.isel.ls.sql;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

/**
 * Created by Nuno on 27/02/2018.
 */
public class Sql {
    private static Connection con;
    private static void CreateConnetion() throws SQLServerException {
        String url = "jdbc:postgresql://localhost:5432/"+System.getenv("LS-server");
        String user = System.getenv("LS-user");
        String password = System.getenv("LS-pass");
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLServerException {
        CreateConnetion();
        return con;
    }
}

/*
* public class Sql {
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
*/