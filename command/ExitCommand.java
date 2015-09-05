package command;

import exception.ExitException;
import main.DatabaseManager;

public class ExitCommand implements Command {
    @Override
    public void execute(DatabaseManager manager, String... parameters) throws ExitException {
        System.out.println("I'm quitting. Bye.");
        throw new ExitException(null);
    }
}
