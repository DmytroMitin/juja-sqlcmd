package command;

import main.DatabaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ModifyCommand implements Command {
    @Override
    public void execute (DatabaseManager manager, String... parameters) throws SQLException {
        if (parameters.length == 5) {
            execute(manager, parameters[0], parameters[1], parameters[2], parameters[3], parameters[4]);
        } else {
            System.out.println("Invalid number of parameters. Command 'modify' must have 5 parameters "
                    + "(table, name of key column, value in key column, name of column, new value) separated by symbol '|'.");
            System.out.println("Enter a command name or 'help'.");
        }
    }

    public void execute (DatabaseManager manager, String table, String keyColumnName, String keyColumnValue, String columnName, String newValue) throws SQLException {

        Statement statement = manager.getStatement();
        List<String> oldValues = new ArrayList<>();

//        try {
            statement.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_name = '" + table + "';");
            ResultSet resultSet = statement.getResultSet();
            List<String> columnNames = new ArrayList<>();
            while (resultSet.next()) {
                columnNames.add(resultSet.getString(1));
            }
            resultSet.close();
            if (columnNames.isEmpty()) {
                System.out.println("Invalid database name or database doesn't contain any columns. Enter a command name or 'help'.");
                return;
            }
            if (!columnNames.contains(keyColumnName)) {
                System.out.println("Invalid name of key column. Enter a command name or 'help'.");
                return;
            }
            if (!columnNames.contains(columnName)) {
                System.out.println("Invalid name of destination column. Enter a command name or 'help'.");
                return;
            }

            statement.executeQuery("SELECT " + columnName + " FROM " + table + " WHERE " + keyColumnName + " = '" + keyColumnValue + "';");
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                oldValues.add(resultSet.getString(columnName));
            }
            resultSet.close();

            statement.executeUpdate("UPDATE " + table + " SET " + columnName + " = '" + newValue
                    + "' WHERE " + keyColumnName + " = '" + keyColumnValue + "';");
//        } catch (SQLException e) {
//            String errorMessage = e.getMessage();
//            if (errorMessage.startsWith("ERROR: relation") && errorMessage.contains("does not exist")) {
//                System.out.println("Invalid table name. Enter a command name or 'help'.");
//                return;
//            } else if (errorMessage.startsWith("ERROR: column") && errorMessage.contains("does not exist")) {
//                System.out.println("Invalid name of key column. Enter a command name or 'help'.");
//                return;
//            } else {
//                throw e;
//            }
//        }

        System.out.println("In table '" + table + "' in row(s) with {" + keyColumnName + " = " + keyColumnValue + "} value(s) "
                + oldValues.toString() + " were changed to '" + newValue + "' successfully.");
        System.out.println("Enter a command name or 'help'.");
    }
}
