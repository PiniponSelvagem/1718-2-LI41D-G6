package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import pt.isel.ls.command.Command;
import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.command.utils.CommandUtils;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        Connection con = null;
        try {
            if (args.length >= 1 && args.length <= 3) {
                con = Sql.getConnection();
                con.setAutoCommit(false);
                executeBuildedCommand(con, new CommandBuilder(args, new CommandUtils())).printAllInfo();
                con.commit();
            }
            else
                throw new CommandNotFoundException();
        } catch (CommandNotFoundException | InvalidCommandParametersException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Executes and validates the user command.
     *
     * @param cmdBuilder Builded command ready to be searched.
     * @return Returns the SQL string that should be queried to the SQL server.
     * @throws SQLException
     * @throws CommandNotFoundException
     * @throws InvalidCommandParametersException
     */
    public static CommandView executeBuildedCommand(Connection con, CommandBuilder cmdBuilder) throws CommandNotFoundException, InvalidCommandParametersException {

        CommandView cmdView = null;

        try {
            Command cmd = cmdBuilder.getCmdUtils().getCmdTree().search(cmdBuilder);
            if (cmd == null)
                throw new CommandNotFoundException();
            cmdView = cmd.execute(cmdBuilder, con);

        } catch(SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        }
        return cmdView;
    }
}
