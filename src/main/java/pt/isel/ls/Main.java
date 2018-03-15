package pt.isel.ls;

import pt.isel.ls.command.Command;
import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.command.utils.CommandUtils;
import pt.isel.ls.sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length >= 1 && args.length <= 3)
                executeBuildedCommand(new CommandBuilder(args, new CommandUtils()));
            else
                throw new CommandNotFoundException();
        } catch (CommandNotFoundException | InvalidCommandParametersException e) {
            System.out.println(e.getMessage());
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
    private static void executeBuildedCommand(CommandBuilder cmdBuilder) throws CommandNotFoundException, InvalidCommandParametersException {
        Connection con = null;
        try {
            con = Sql.CreateConnetion();
            con.setAutoCommit(false);
            Command cmd = cmdBuilder.getCmdUtils().getCmdTree().search(cmdBuilder);
            if (cmd == null)
                throw new CommandNotFoundException();

            cmd.execute(cmdBuilder, con);

        }catch(SQLException e){
            System.out.println(e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
