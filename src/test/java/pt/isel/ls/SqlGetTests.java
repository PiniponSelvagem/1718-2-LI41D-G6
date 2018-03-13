package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.junit.Test;
import pt.isel.ls.sql.Sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SqlGetTests {

    @Test
    public void insert_cinemas() throws SQLException{
        Connection con = null;
        try {
            con = Sql.CreateConnetion();
            con.setAutoCommit(false);

            PreparedStatement stmt = con.prepareStatement("INSERT INTO CINEMA VALUES (?, ?)");
            stmt.setString(1, "testName");
            stmt.setString(2, "testCity");
            stmt.execute();

            ResultSet rs;
            PreparedStatement stmt2 = con.prepareStatement("SELECT * from CINEMA");
            rs = stmt2.executeQuery();
            if(rs.next()){
                String name = rs.getString(2);
                String city = rs.getString(3);
                assertEquals("testName", name);
                assertEquals("testCity", city);
            }
        }finally {
            con.rollback();
            con.close();
        }

    }

}
