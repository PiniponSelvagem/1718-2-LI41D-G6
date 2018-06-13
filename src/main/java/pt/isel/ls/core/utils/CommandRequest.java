package pt.isel.ls.core.utils;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.common.commands.db_queries.SQLData;
import pt.isel.ls.core.exceptions.*;
import pt.isel.ls.view.CommandView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;
import static pt.isel.ls.core.strings.ExceptionEnum.SQL_ERROR;
import static pt.isel.ls.core.strings.ExceptionEnum.VIEW__CREATION_ERROR;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SQL;

public class CommandRequest {
    private final static Logger log = LoggerFactory.getLogger(CommandRequest.class);

    private CommandView cmdView;
    private CommandBuilder cmdBuilder;
    private DataContainer data;
    private CommandUtils cmdUtils;
    private PGSimpleDataSource ds;
    private Connection con;

    public CommandRequest(String[] args, CommandUtils cmdUtils, PGSimpleDataSource ds) throws CommonException {
        this.cmdUtils = cmdUtils;
        this.ds = ds;
        this.cmdBuilder = new CommandBuilder(args, cmdUtils);
    }

    /**
     * Checks if the command requires a SQL connection and executes the command accordingly.
     */
    public void checkAndExecuteCommand() throws CommonException {
        if (cmdBuilder.getCommand() == null)
            throw new CommandException(COMMAND__NOT_FOUND);

        try {
            if (cmdBuilder.getCommand().isSQLRequired()) {
                con = ds.getConnection();
                con.setAutoCommit(false);
                data = executeCommand(con);
                con.commit();
            }
            else {
                data = executeCommand(null);
            }
        } catch (SQLException e) {
            log.error(String.format(SQL_ERROR.toString(), e.getErrorCode(), e.getMessage()), this.hashCode());

            data = new DataContainer(cmdBuilder.getCommand().getClass().getSimpleName());
            data.add(D_SQL, new SQLData<>(e.getErrorCode(), e.getMessage()));

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                log.error(String.format(SQL_ERROR.toString(), e1.getErrorCode(), e1.getMessage()), this.hashCode());
            }
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.error(String.format(SQL_ERROR.toString(), e.getErrorCode(), e.getMessage()), this.hashCode());
                }
            }
        }
        data.headerType = cmdBuilder.getHeaderType();
    }

    /**
     * Execute the command
     * NOTE: THIS METHOD IS PUBLIC FOR TESTS, FOR SAFER COMMAND EXECUTION USE {@link #checkAndExecuteCommand},
     *       that calls this method after the checks.
     * @return Returns the DataContainer with the info that the command got
     * @throws CommandException CommandException
     */
    public DataContainer executeCommand(Connection con) throws CommandException, ParameterException, TheMoviesDBException, SQLException {
        return cmdBuilder.getCommand().execute(cmdBuilder, con);
    }


    /**
     * Create view for requested header
     * @return Returns CommandView ready for output.
     * @throws ViewNotImplementedException ViewNotImplementedException
     */
    public CommandView executeView() throws ViewNotImplementedException {
        HashMap<String, String> viewMap = cmdUtils.getCmdViewMap(data.headerType);

        if (viewMap == null) { //if view type does not exist
            log.warn("Requested view '{}' does not exist", data.headerType, this.hashCode());
            throw new ViewNotImplementedException(data.headerType);
        }

        String viewLink = viewMap.get(data.getCreatedBy());
        if (viewLink!=null) {
            Object obj;
            try {
                Class<?> klass = Class.forName(viewLink);
                Constructor<?> constructor = klass.getConstructor(DataContainer.class);
                obj = constructor.newInstance(data);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                //log.error("View creation error with type '{}' for command '{}'", data.headerType, data.getCreatedBy(), this.hashCode());
                throw new ViewNotImplementedException(VIEW__CREATION_ERROR);
            }
            cmdView = (CommandView) obj;
            cmdView.createView();
        }

        if (cmdView == null) { //if command does not have implementation for this type of header
            log.info("Requested view '{}' for command '{}' is not implemented", data.headerType, data.getCreatedBy(), this.hashCode());
            throw new ViewNotImplementedException(data.headerType);
        }

        return cmdView;
    }

    /**
     * @return DataContainer from executed command
     */
    public DataContainer getData() {
        return data;
    }

    /**
     * @return Returns fileName, can be null.
     */
    public String getFileName() {
        return cmdBuilder.getFileName();
    }
}
