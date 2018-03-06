package pt.isel.ls;

import pt.isel.ls.command.Cache.CommandMap;
import pt.isel.ls.command.Command;
import pt.isel.ls.command.GetMovies;
import pt.isel.ls.sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.command.Cache.CommandMap.*;

public class Main {
    public static void main(String[] args) {
        mapInitialize();
        try {
            String result = executeCommand("GET /movies");
            System.out.println(result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String executeCommand(String str) throws SQLException {
        Connection con = null;
        String result;
        try {
            con = Sql.CreateConnetion();
            Command com = mapSearch(str); //TODO: str might need "work" before doing mapSearch
            result = com.execute(con);
        }
        finally {
            if (con != null) {
                con.close();
            }
        }
        return result;
    }
}
