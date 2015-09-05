package command;

import main.DatabaseManager;


public class HelpCommand implements Command {
    @Override
    public void execute(DatabaseManager manager, String... parameters) {
        System.out.println("Available commands:");
        System.out.println("  connect|database|user|password -- connects to specified database using these credentials ");
        System.out.println("  list -- shows all existing tables");
        System.out.println("  find|table (or find|table|limit|offset) -- shows specified table");
        System.out.println("  modify|table|name_of_column_#1|value_in_column_#1|name_of_column_#2|new_value " +
                "-- changes value(s) in intersection of row(s) with {name_of_column_#1 = value_in_column_#1}");
        System.out.println("                                                                                  "
                + " and column 'name_of_column_#2' to 'new_value'");
        System.out.println("  exit -- exits from console database manager");
        System.out.println("  help -- prints this help");
        System.out.println("(use symbol '|' to separate parameters)");
        System.out.println("Enter a command name or 'help'.");

    }
}
