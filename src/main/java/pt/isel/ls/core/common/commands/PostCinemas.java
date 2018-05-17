package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class PostCinemas extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO CINEMA VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, cmdBuilder.getParameter(NAME.toString()));
        stmt.setString(2, cmdBuilder.getParameter(CITY.toString()));
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        int id = 0;
        if(rs.next()) id = rs.getInt(1);

        return new PostView<>(true, "Cinema ID: ", id);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
