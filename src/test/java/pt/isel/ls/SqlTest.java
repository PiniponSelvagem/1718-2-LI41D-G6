package pt.isel.ls;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

/**
 * Created by Nuno on 27/02/2018.
 */
public class SqlTest {

    @Test
    public void insert_students() throws SQLException{
        Connection con = Sql.CreateConnetion();
        int number = 10;
        PreparedStatement stmt = con.prepareStatement("insert into Students values(?, ?)");
        stmt.setString(1, "Name");
        stmt.setInt(2, number);
        int x = stmt.executeUpdate();
        assertTrue(x>0);
        PreparedStatement stmt1 = con.prepareStatement("delete from Students where number = ?");
        stmt1.setInt(1, number);
        stmt1.execute();
        con.close();
    }

}
