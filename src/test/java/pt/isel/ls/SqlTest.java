package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.sql.Sql;

class SqlTest {
    private static final Logger logger = LoggerFactory.getLogger(Sql.class);
    private static final String envVar = "JDBC_DATABASE_URL_TEST";

    static PGSimpleDataSource CreateConnetion() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        String jdbcUrl = System.getenv(envVar);
        if(jdbcUrl == null) {
            logger.error("JDBC_DATABASE_URL_TEST is not defined!");
        }
        else ds.setUrl(jdbcUrl);
        return ds;
    }
}