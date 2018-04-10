package pt.isel.ls;

import pt.isel.ls.core.commands.Command;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;

public class Main {

    public static void main(String[] args) {
        Connection con = null;
        try {
            if (args.length <= 1) {
                executeBuildedCommand(new CommandBuilder(args, new CommandUtils())).printAllInfo();
            }
            else if (args.length <= 4) {
                con = Sql.getConnection();
                con.setAutoCommit(false);
                executeBuildedCommand(con, new CommandBuilder(args, new CommandUtils())).printAllInfo();
                con.commit();
            }
            else
                throw new CommandException(COMMAND__NOT_FOUND);
        } catch (CommandException  e) {
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
     * Executes and validates the user core, ONLY INTERNAL COMMANDS.
     * @param cmdBuilder
     * @return Returns the view for that core.
     */
    private static CommandView executeBuildedCommand(CommandBuilder cmdBuilder) throws CommandException {
        Command cmd = cmdBuilder.execute();
        if (cmd == null)
            throw new CommandException(COMMAND__NOT_FOUND);
        return cmd.execute(cmdBuilder);
    }

    /**
     * Executes and validates the user core.
     *
     * @param cmdBuilder Builded core ready to be searched.
     * @return Returns the view for that core.
     * @throws SQLException
     * @throws CommandException
     */
    public static CommandView executeBuildedCommand(Connection con, CommandBuilder cmdBuilder) throws CommandException {

        CommandView cmdView = null;

        try {
            Command cmd = cmdBuilder.execute();
            if (cmd == null)
                throw new CommandException(COMMAND__NOT_FOUND);
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
