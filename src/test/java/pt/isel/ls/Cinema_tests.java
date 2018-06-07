package pt.isel.ls;

import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.core.exceptions.CommonException;
import pt.isel.ls.core.utils.CommandRequest;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static pt.isel.ls.SqlTest.CreateConnetion;
import static pt.isel.ls.core.common.headers.HeadersAvailable.TEXT_HTML;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


@SuppressWarnings("Duplicates")
public class Cinema_tests {
    private static final PGSimpleDataSource ds = CreateConnetion();
    private Connection con = null;
    private final static CommandUtils cmdUtils = new CommandUtils(TEXT_HTML.toString());


    public void create_cinemas(Connection con){
        for(int i = 0; i < 3; i++){
            String name = "nameTest" + (i + 1);
            try {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO CINEMA (Name, City) VALUES('" + name +"', 'cityTest')");
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    @Test
    public void insert_cinema() {
        LinkedList<Cinema> cinemas = new LinkedList<>();
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            for(int i = 0; i < 3; i++) {
                String name = "nameTest" + (i + 1);
                new CommandRequest(new String[] {"POST", "/cinemas", "name=" + name + "&city=cityTest"}, cmdUtils, ds).executeCommand(con);
            }

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cinemas.add(new Cinema(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            int i = 0;
            for (Cinema c : cinemas) {
                assertEquals("nameTest" + (i + 1), c.getName());
                i++;
            }
        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
            if (con != null) {
                try {
                    con.rollback();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void get_cinemas() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            create_cinemas(con);

            DataContainer data = new CommandRequest(new String[] {"GET", "/cinemas"}, cmdUtils, ds).executeCommand(con);

            LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
            Cinema cinema;
            for(int i = 0; i < cinemas.size(); i++){
                cinema = cinemas.get(i);
                String name = "nameTest" + (i + 1);
                assertEquals(name, cinema.getName());
                i++;
            }
        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
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

    @Test
    public void get_cinema_by_id(){
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            create_cinemas(con);

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA");
            ResultSet rs = stmt.executeQuery();

            String[] ids = new String[3];
            int i = 0;
            while(rs.next()){
                ids[i] = rs.getString(1);
                i++;
            }

            DataContainer data = new CommandRequest(new String[] {"GET", "/cinemas/" + ids[1]}, cmdUtils, ds).executeCommand(con);
            Cinema cinema = (Cinema) data.getData(D_CINEMA);
            assertEquals(ids[1], cinema.getId());
        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
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

    @Test
    public void insert_cinemaID_theater(){
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            create_cinemas(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA");
            ResultSet rs = stmt.executeQuery();
            int[] ids = new int[3];
            int i = 0;
            while(rs.next()){
                ids[i] = rs.getInt(1);
            }

            int seats = 30;
            for(int j = 0; j < 3 ; j++){
                String name = "theater";
                name += (j + 1);
                new CommandRequest(new String[] {"POST", "/cinemas/" + ids[0] + "/theaters", "name=" + name + "&row=10" + "&seat=" + seats}, cmdUtils, ds).executeCommand(con);
                seats += 10;
            }

            stmt = con.prepareStatement("SELECT theater.Theater_Name FROM THEATER WHERE cid = " + ids[0]);
            ResultSet rs2 = stmt.executeQuery();
            int k = 0;
            while(rs2.next()){
                String name = "theater";
                assertEquals(name + (k + 1), rs2.getString(1));
                k++;
            }
        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
            if (con != null){
                try {
                    con.rollback();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void get_cinemasID_theater(){
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            create_cinemas(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA");
            ResultSet rs = stmt.executeQuery();
            int[] ids = new int[3];
            int i = 0;
            while(rs.next()){
                ids[i] = rs.getInt(1);
            }

            int seats = 30;
            for(int j = 0; j < 3 ; j++){
                String name = "theater";
                name += (j + 1);
                new CommandRequest(new String[] {"POST", "/cinemas/" + ids[0] + "/theaters", "name=" + name + "&row=10" + "&seat=" + seats}, cmdUtils, ds).executeCommand(con);
                seats += 10;
            }

            DataContainer data = new CommandRequest(new String[]
                    {"GET", "/cinemas/" + ids[0] + "/theaters"}, cmdUtils, ds).executeCommand(con);
            Theater theater;
            LinkedList<Theater> theaters = (LinkedList<Theater>) data.getData(D_THEATERS);
            for(int k = 0; k < theaters.size(); k++){
                theater = theaters.get(k);
                String name = "theater";
                name += (k + 1);
                assertEquals(name, theater.getName());
                k++;
            }
        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
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

    @Test
    public void get_cinemaID_theaterID(){
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            create_cinemas(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA");
            ResultSet rs = stmt.executeQuery();
            int[] ids = new int[3];
            int i = 0;
            while(rs.next()){
                ids[i] = rs.getInt(1);
            }

            int seats = 30;
            for(int j = 0; j < 3 ; j++){
                String name = "theater";
                name += (j + 1);
                new CommandRequest(new String[]
                        {"POST", "/cinemas/" + ids[0] + "/theaters", "name=" + name + "&row=10" + "&seat=" + seats}, cmdUtils, ds).executeCommand(con);
                seats += 10;
            }

            int[] theaterIDs = new int[3];
            stmt = con.prepareStatement("SELECT theater.tid FROM THEATER WHERE cid=" + ids[0]);
            ResultSet rs2 = stmt.executeQuery();
            int k = 0;
            while(rs2.next()){
                theaterIDs[k] = rs2.getInt(1);
                k++;
            }

            DataContainer data = new CommandRequest(new String[]
                    {"GET", "/cinemas/" + ids[0] + "/theaters/" + theaterIDs[0]}, cmdUtils, ds).executeCommand(con);
            Theater theater = (Theater) data.getData(D_THEATER);
            assertEquals("theater1", theater.getName());

        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
            if (con != null)
                try {
                    con.rollback();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
}
