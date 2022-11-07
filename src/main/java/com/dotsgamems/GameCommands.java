package com.dotsgamems;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.awt.*;

@ShellComponent
public class GameCommands {

    private Game game;

    @ShellMethod("Start game")
    public String startGame() {
        game = new Game(15);
        return game.printBoard();
    }

    @ShellMethod("Move player1")
    public String move1(@ShellOption(help = "Dot X coordinates") int x,
                        @ShellOption(help = "Dot Y coordinates") int y) {
        if (game == null) {
            throw new IllegalArgumentException("Firstly you should start the game");
        }
        game.setDot(Players.FIRST, new Point(x,y));
        return game.printBoard();
    }


}
