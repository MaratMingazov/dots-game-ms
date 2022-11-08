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

    @ShellMethod("Player movement")
    public String move(@ShellOption(help = "Player id") int id,
                       @ShellOption(help = "X coordinates") int x,
                       @ShellOption(help = "Y coordinates") int y) {
        validate();
        game.setDot(Players.getById(id), x, y);
        return game.printComputerBoard();
    }

    private void validate() {
        if (game == null) {
            throw new IllegalArgumentException("Firstly you should start the game.");
        }
    }


}
