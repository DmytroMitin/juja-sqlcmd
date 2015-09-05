package command;

import main.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FindCommand implements Command{

    @Override
    public void execute (DatabaseManager manager, String... parameters) throws SQLException {
        if (parameters.length == 1) {
            execute(manager, parameters[0]);
        } else if (parameters.length == 3) {
            try {
                execute(manager, parameters[0], Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
            } catch (NumberFormatException e) {
                System.out.println("Limit and offset must be integer numbers. Enter a command name or 'help'.");
            }
        } else {
            System.out.println("Invalid number of parameters. Command 'find' must have 1 parameter " +
                    "(table) or 3 parameters (table, limit, offset) separated by symbol '|'. ");
            System.out.println("Enter a command name or 'help'.");
        }
    }

    public void execute (DatabaseManager manager, String tableName) throws SQLException {
        execute(manager, tableName, Integer.MAX_VALUE, 0);
    }

    public void execute (DatabaseManager manager, String tableName, int limit, int offset) throws SQLException {
        if (manager.getConnection() == null) {
            System.out.println("You must connect to a database first. Enter a command name or 'help'.");
            return;
        }

        if (limit < 0 || offset < 0) {
            System.out.println("Limit and offset can't be negative. Enter a command name or 'help'.");
            return;
        }

        Statement statement = manager.getStatement();

        List<List<String>> table = new ArrayList<>();
        int numberOfColumns = 0;
        List<String> columnNames;
        try {
//            statement.executeQuery("SELECT COUNT(*) FROM information_schema.columns WHERE table_name = '" + tableName + "';");
//
//            ResultSet resultSet = statement.getResultSet();
//            resultSet.next();
//            numberOfColumns = resultSet.getInt(1);
//            resultSet.close();

            statement.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_name = '" + tableName + "';");
            ResultSet resultSet = statement.getResultSet();
            columnNames = new ArrayList<>();
            while (resultSet.next()) {
                columnNames.add(resultSet.getString(1));
            }
            resultSet.close();

            numberOfColumns = columnNames.size();

            statement.executeQuery("SELECT * FROM " + tableName + " LIMIT " + limit + " OFFSET " + offset + ";");
            resultSet = statement.getResultSet();

            for (int i = 1; i <= numberOfColumns; i++) {
                List<String> column = new ArrayList<>();
                while (resultSet.next()) {
                    column.add(resultSet.getString(i));
                }
                table.add(column);
                resultSet.beforeFirst();
            }
            resultSet.close();
        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.startsWith("ERROR: relation") && errorMessage.contains("does not exist")) {
                System.out.println("Invalid table name. Enter a command name or 'help'.");
                return;
            } else {
                throw e;
            }
        }

        int maxColumnSize = 0;
        for (List<String> column : table) {
            maxColumnSize = Math.max(maxColumnSize, column.size());
        }

        System.out.println("Table: '" + tableName + "', number of rows (after limit-offset applied): " + maxColumnSize + ", number of columns: " + numberOfColumns
                + ", limit: " + limit + ", offset: " + offset);

        System.out.print("| ");
        for (String columnName : columnNames) {
            System.out.print(columnName);
            System.out.print(" | ");
        }
        System.out.println("");
        System.out.println("-----------------------------------------");

        for (int j = 0; j < maxColumnSize; j++) {
            System.out.print("| ");
            for (int i = 0; i < numberOfColumns; i++) {
                if (j < table.get(i).size()) {
                    System.out.print(table.get(i).get(j));
                } else {
                    System.out.print(" ");
                }
                System.out.print(" | ");
            }
            System.out.println("");
        }
        System.out.println("Enter a command name or 'help'.");
    }
}
