package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Theater;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.command.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import static org.junit.Assert.assertEquals;



public class Cinema_tests {
    private Connection con = null;


    public void create_cinemas(Connection con){
        for(int i = 0; i < 3; i++){
            String name = "nameTest" + (i + 1);
            try {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO CINEMA VALUES('" + name +"', 'cityTest')");
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void insert_cinema() {
        LinkedList<Cinema> cinemas = new LinkedList<>();
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            for(int i = 0; i < 3; i++) {
                String name = "nameTest" + (i + 1);
                Main.executeBuildedCommand(con, new CommandBuilder(new String[]
                        {"POST", "/cinemas", "name=" + name + "&city=cityTest"}, new CommandUtils()));
            }

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cinemas.add(new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            int i = 0;
            for (Cinema c : cinemas) {
                assertEquals("nameTest" + (i + 1), c.getName());
                i++;
            }
        } catch (SQLException | CommandException e) {
            e.printStackTrace();
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
            con = Sql.getConnection();
            con.setAutoCommit(false);

            create_cinemas(con);


            GetCinemasView view = (GetCinemasView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]
                    {"GET", "/cinemas"}, new CommandUtils()));

            DataContainer data;
            data = view.getData();
            Cinema cinema;
            for(int i = 0; i < data.size(); i++){
                cinema = (Cinema) data.getData(i);
                String name = "nameTest" + (i + 1);
                assertEquals(name, cinema.getName());
                i++;
            }
        } catch (SQLException | CommandException e) {
            e.printStackTrace();
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
            con = Sql.getConnection();
            con.setAutoCommit(false);
            create_cinemas(con);

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA");
            ResultSet rs = stmt.executeQuery();

            int[] ids = new int[3];
            int i = 0;
            while(rs.next()){
                ids[i] = rs.getInt(1);
                i++;
            }

            GetCinemaIDView view = (GetCinemaIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]
                    {"GET", "/cinemas/" + ids[1]}, new CommandUtils()));
            DataContainer data;
            data = view.getData();
            Cinema cinema = (Cinema)data.getData(0);
            assertEquals(ids[1], cinema.getId());
        } catch (SQLException | CommandException e) {
            e.printStackTrace();
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
            con = Sql.getConnection();
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
                Main.executeBuildedCommand(con, new CommandBuilder(new String[]
                        {"POST", "/cinemas/" + ids[0] + "/theaters", "name=" + name + "&rows=10" + "&seats=" + seats}, new CommandUtils()));
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
        } catch (SQLException | CommandException e) {
            e.printStackTrace();
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
            con = Sql.getConnection();
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
                Main.executeBuildedCommand(con, new CommandBuilder(new String[]
                        {"POST", "/cinemas/" + ids[0] + "/theaters", "name=" + name + "&rows=10" + "&seats=" + seats}, new CommandUtils()));
                seats += 10;
            }

            GetCinemaIDTheatersView view = (GetCinemaIDTheatersView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]
                    {"GET", "/cinemas/" + ids[0] + "/theaters"}, new CommandUtils()));
            DataContainer data;
            data = view.getData();
            Theater theater;
            for(int k = 0; k < data.size(); k++){
                theater = (Theater) data.getData(k);
                String name = "theater";
                name += (k + 1);
                assertEquals(name, theater.getName());
                k++;
            }
        } catch (SQLException | CommandException e) {
            e.printStackTrace();
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
            con = Sql.getConnection();
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
                Main.executeBuildedCommand(con, new CommandBuilder(new String[]
                        {"POST", "/cinemas/" + ids[0] + "/theaters", "name=" + name + "&rows=10" + "&seats=" + seats}, new CommandUtils()));
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

            GetCinemaIDTheatersIDView view = (GetCinemaIDTheatersIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]
                    {"GET", "/cinemas/" + ids[0] + "/theaters/" + theaterIDs[0]}, new CommandUtils()));
            DataContainer data;
            data = view.getData();
            Theater theater = (Theater) data.getData(0);
            assertEquals("theater1", theater.getName());

        } catch (SQLException | CommandException e) {
            e.printStackTrace();
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
