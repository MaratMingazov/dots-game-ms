package com.dotsgamems;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class GameCommands {

    private Game game;

    @ShellMethod("Start game")
    public String start(@ShellOption(help = "Board size") int boardSize) {
        game = new Game(boardSize);
        return game.printComputerBoard();
    }

    @ShellMethod("First player movement")
    public String move1(@ShellOption(help = "X coordinates") int x,
                        @ShellOption(help = "Y coordinates") int y) {
        if (game == null) {
            throw new IllegalArgumentException("Firstly you should start the game.");
        }
        game.setDot(Players.FIRST, x, y);
        return game.printComputerBoard();
    }

    @ShellMethod("Second player movement")
    public String move2(@ShellOption(help = "X coordinates") int x,
                        @ShellOption(help = "Y coordinates") int y) {
        if (game == null) {
            throw new IllegalArgumentException("Firstly you should start the game.");
        }
        game.setDot(Players.SECOND, x, y);
        return game.printComputerBoard();
    }


}
