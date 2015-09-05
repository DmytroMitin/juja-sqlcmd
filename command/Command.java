package command;

import exception.ExitException;
import main.DatabaseManager;

import java.sql.SQLException;

public interface Command {

    void execute (DatabaseManager manager, String... parameters) throws SQLException, ExitException;
}
