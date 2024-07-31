package lissa.ratlr;

import lissa.ratlr.command.EvaluateCommand;
import lissa.ratlr.command.MergeCommand;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(subcommands = { MergeCommand.class, EvaluateCommand.class })
public final class MainCLI {

    private MainCLI() {
    }

    public static void main(String[] args) {
        new CommandLine(new MainCLI()).registerConverter(Path.class, Path::of).execute(args);
    }
}
