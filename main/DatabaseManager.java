package main;

import command.*;
import exception.ExitException;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    private final static Map<String, Command> COMMANDS = new HashMap<>();

    private Connection connection;

    private Statement statement;

    private String user;

    private String database;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public String getUser() {
        return user;
    }

    public String getDatabase() {
        return database;
    }

    public static void main(String[] args) throws SQLException{
        try {
            DatabaseManager manager = new DatabaseManager();
            manager.run();
        } catch (ExitException e) {}
    }

    public void run() throws SQLException, ExitException {

        COMMANDS.put("connect", new ConnectCommand());
        COMMANDS.put("list", new ListCommand());
        COMMANDS.put("help", new HelpCommand());
        COMMANDS.put("exit", new ExitCommand());
        COMMANDS.put("find", new FindCommand());
        COMMANDS.put("modify", new ModifyCommand());

        System.out.println("Hello. This is simple console database manager. Enter a command name or 'help'.");

        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String input = in.nextLine();
            String[] inputParsed = input.split("\\|");
            Command command = COMMANDS.get(inputParsed[0]);
            int numberOfParameters = inputParsed.length - 1;
            String[] parameters = new String[numberOfParameters];
            System.arraycopy(inputParsed, 1, parameters, 0, numberOfParameters);
            try {
                if (command != null) {
                    command.execute(this, parameters);
                } else {
                    System.out.println("Invalid command name. Enter a command name or 'help'.");
                }
            } catch (Exception e) {

                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (Exception e1) {
                    e.addSuppressed(e1);
                }

                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e1) {
                    e.addSuppressed(e1);
                }

                throw e;
            }
        }
        in.close();

//        String lineSeparator = System.getProperty("line.separator");

//        statement.executeQuery("SELECT usename FROM pg_shadow;");
//        ResultSet resultSet = statement.getResultSet();
//        while (resultSet.next()) {
//            String users = resultSet.getString(1);
//            System.out.println(users);
//        }

//        statement.executeQuery("SELECT datname FROM pg_database;");
//        ResultSet resultSet = statement.getResultSet();
//        while (resultSet.next()) {
//            String databases = resultSet.getString(1);
//            System.out.println(databases);
//        }


    }
}
