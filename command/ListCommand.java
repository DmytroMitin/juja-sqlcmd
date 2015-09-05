package command;

import main.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command {
    @Override
    public void execute(DatabaseManager manager, String... parameters) throws SQLException {
        if (manager.getConnection() == null) {
            System.out.println("You must connect to a database first. Enter a command name or 'help'.");
            return;
        }

        Statement statement = manager.getStatement();
        statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';");
        ResultSet resultSet = statement.getResultSet();
        List<String> tables = new ArrayList<>();
        while (resultSet.next()) {
            tables.add(resultSet.getString("table_name"));
        }
        resultSet.close();
        System.out.println("Existing tables:");
        System.out.println(tables);
        System.out.println("Enter a command name or 'help'.");
    }
}
