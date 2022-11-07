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

    @ShellMethod("First player movement")
    public String move1(@ShellOption(help = "X coordinates") int x,
                        @ShellOption(help = "Y coordinates") int y) {
        if (game == null) {
            throw new IllegalArgumentException("Firstly you should start the game.");
        }
        game.setDot(Players.FIRST, new Point(x,y));
        return game.printBoard();
    }

    @ShellMethod("Second player movement")
    public String move2(@ShellOption(help = "X coordinates") int x,
                        @ShellOption(help = "Y coordinates") int y) {
        if (game == null) {
            throw new IllegalArgumentException("Firstly you should start the game.");
        }
        game.setDot(Players.SECOND, new Point(x,y));
        return game.printBoard();
    }


}
