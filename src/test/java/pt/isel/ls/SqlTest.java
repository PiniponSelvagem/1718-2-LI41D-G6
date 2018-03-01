package pt.isel.ls;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
        stmt.execute();

        PreparedStatement stmt2 = con.prepareStatement("select number from students where name = ?");
        stmt2.setString(1, name);
        rs = stmt2.executeQuery();
        int n = 0;
        if(rs.next()) n = rs.getInt(1);
        assertEquals(n, number);

        con.rollback();
        con.close();
    }

    @Test
    public void update_students() throws SQLException{
        Connection con = Sql.CreateConnetion();
        con.setAutoCommit(false);
        int number = 10;
        String name = "NomeTeste";
        ResultSet rs;
        PreparedStatement stmt = con.prepareStatement("insert into students values(?, ?)");
        stmt.setString(1, name);
        stmt.setInt(2, number);
        stmt.execute();

        PreparedStatement stmt2 = con.prepareStatement("update students set name = 'NewName' where number = ?");
        stmt2.setInt(1, number);
        stmt2.execute();

        PreparedStatement stmt3 = con.prepareStatement("select name from students where number = ?");
        stmt3.setInt(1, number);
        rs = stmt3.executeQuery();
        String res = "";
        if(rs.next()) res = rs.getString(1);
        assertEquals("NewName", res);

        con.rollback();
        con.close();
    }

    @Test
    public void delete_student() throws SQLException{
        Connection con = Sql.CreateConnetion();
        con.setAutoCommit(false);
        int number = 10;
        String name = "NomeTeste";
        ResultSet rs;
        PreparedStatement stmt = con.prepareStatement("insert into students values(?, ?)");
        stmt.setString(1, name);
        stmt.setInt(2, number);
        stmt.execute();

        PreparedStatement stmt2 = con.prepareStatement("delete from students where number = ?");
        stmt2.setInt(1, number);
        int x = stmt2.executeUpdate();
        assertTrue(x > 0);
        con.close();
    }
}
