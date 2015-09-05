package command;

import main.DatabaseManager;

import java.sql.*;

public class ConnectCommand implements Command {

    @Override
    public void execute (DatabaseManager manager, String... parameters) throws SQLException{
        if (parameters.length == 3) {
            execute(manager, parameters[0], parameters[1], parameters[2]);
        } else {
            System.out.println("Invalid number of parameters. Command 'connect' must have 3 parameters " +
                    "(database, user, password) separated by symbol '|'. Enter a command name or 'help'.");
        }
    }

    public void execute (DatabaseManager manager, String database, String user, String password) throws SQLException{
        if (manager.getConnection() != null) {
            System.out.println("You've already connected to database '" + manager.getDatabase() + "' as user '" +
                    manager.getUser() + "'. Switching databases or users is not supported. Enter a command name or 'help'.");
            return;
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver is not installed. Enter a command name or 'help'.");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, user, password);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            manager.setConnection(connection);
            manager.setStatement(statement);
            manager.setDatabase(database);
            manager.setUser(user);
            System.out.println("Connected to database '" + database + "' as user '" + user + "' successfully. Enter a command name or 'help'.");
        } catch (SQLException e) {;
            String errorMessage = e.getMessage();
            if (errorMessage.startsWith("FATAL: database") && errorMessage.contains("does not exist")){
                System.out.println("Invalid database name. Enter a command name or 'help'.");
                return;
            } else if (errorMessage.startsWith("FATAL: password authentication failed for user")){
                System.out.println("Invalid user name or password. Enter a command name or 'help'.");
                return;
            } else {
                throw e;
            }
        }

    }

}
