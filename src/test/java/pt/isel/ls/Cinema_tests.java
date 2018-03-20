package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.junit.Test;
import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.command.utils.CommandUtils;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.sql.Sql;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import static org.junit.Assert.assertEquals;



public class Cinema_tests {
    Connection con = null;

    @Test
    public void insert_cinema(){
        LinkedList<Cinema> cinemas = new LinkedList<>();
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            for(int i = 0; i < 3; i++){
                String name = "nameTest" + (i + 1);
                Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas", "name="+name+"&city=cityTest"}, new CommandUtils()));
            }

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                cinemas.add(new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            int i = 0;
            for(Cinema c : cinemas){
                assertEquals("nameTest" + (i + 1), c.getName());
                i++;
            }
        } catch (SQLException | CommandNotFoundException | InvalidCommandParametersException e) {
            e.printStackTrace();
        }finally {
            if(con != null){
                try {
                    con.rollback();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
