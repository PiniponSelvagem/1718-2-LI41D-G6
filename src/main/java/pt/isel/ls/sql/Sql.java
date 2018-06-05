package pt.isel.ls.sql;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.postgresql.PGConnection;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by Nuno on 27/02/2018.
 */
public class Sql {
    //private static Connection con;
    private static final Logger logger = LoggerFactory.getLogger(Sql.class);


    public static PGSimpleDataSource CreateConnetion(String envVar) {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        String jdbcUrl = System.getenv(envVar);
        if(jdbcUrl == null) {
            logger.error("JDBC_DATABASE_URL is not defined, ending");
        }
        else ds.setUrl(jdbcUrl);
        return ds;
    }
    /*static {
        try {
            CreateConnetion(String envVar);
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }*/
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