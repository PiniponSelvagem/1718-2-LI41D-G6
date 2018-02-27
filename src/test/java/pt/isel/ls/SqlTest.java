package pt.isel.ls;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


/**
 * Created by Nuno on 27/02/2018.
 */
public class SqlTest {

    @Test
    public void insert_students() throws SQLException{
        Connection con = Sql.CreateConnetion();
        con.setAutoCommit(false);
        int number = 10;
        String name = "NomeTeste";
        ResultSet rs;
        PreparedStatement stmt = con.prepareStatement("insert into students values(?, ?)");
        stmt.setString(1, name);
        stmt.setInt(2, number);
        stmt.executeUpdate();


        PreparedStatement stmt2 = con.prepareStatement("select number from students where name = ?");
        stmt2.setString(1, name);
        rs = stmt2.executeQuery();
        int n = 0;
        if(rs.next()) n = rs.getInt(1);
        assertEquals(n, number);

        con.rollback();
        con.close();
    }

}
