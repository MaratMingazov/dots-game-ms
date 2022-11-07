package com.dotsgamems;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class GameCommands {

    private Game game;

    @ShellMethod("Start game")
    public String startGame() {
        game = new Game(15);
        return game.printBoard();
    }


}
