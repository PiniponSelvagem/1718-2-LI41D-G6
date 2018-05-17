package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class PostCinemaIDTheaters extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL+DIR_SEPARATOR+THEATERS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        int seats = Integer.parseInt(cmdBuilder.getParameter(ROWS.toString()))
                * Integer.parseInt(cmdBuilder.getParameter(String.valueOf(SEATS_ROW)));
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO THEATER VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, seats);
        stmt.setInt(2, Integer.parseInt(cmdBuilder.getParameter(ROWS.toString())));
        stmt.setInt(3, Integer.parseInt(cmdBuilder.getParameter(SEATS_ROW.toString())));
        stmt.setString(4, cmdBuilder.getParameter(NAME.toString()));
        stmt.setInt(5, Integer.parseInt(cmdBuilder.getId(CINEMA_ID.toString())));
        stmt.execute();

        ResultSet rs = stmt.getGeneratedKeys();
        int id = 0;
        if(rs.next()) id = rs.getInt(1);

        return new PostView<>(true, "Theater ID: ", id);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
