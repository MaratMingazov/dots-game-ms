package com.dotsgamems.game;

import lombok.val;
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

        if (game.isGameFinished()) {
            return game.printComputerBoard();
        }
        val player = Players.getById(id);
        val oppositePlayer = Players.getOppositeById(id);
        game.makeMove(player, x, y);

        if (game.isGameFinished()) {
            return game.printComputerBoard();
        }
        val oppositePlayerMove = game.calculateNextMove(oppositePlayer);
        game.makeMove(oppositePlayer, oppositePlayerMove.x, oppositePlayerMove.y);

        return game.printComputerBoard();
    }

    @ShellMethod("Player movement")
    public String train(@ShellOption(help = "Board size") int boardSize,
                        @ShellOption(help = "Number of epoch to train") int epoch) {

        for (int i = 0; i < epoch; i++) {
            game = new Game(boardSize);
            while (!game.isGameFinished()) {
                val firstPlayerMove = game.calculateNextMove(Players.FIRST);
                game.makeMove(Players.FIRST, firstPlayerMove.x, firstPlayerMove.y);
                if (!game.isGameFinished()) {
                    val secondPlayer = game.calculateNextMove(Players.SECOND);
                    game.makeMove(Players.SECOND, secondPlayer.x, secondPlayer.y);
                }
            }
        }
        return game.printComputerBoard();
    }

    private void validate() {
        if (game == null) {
            throw new IllegalArgumentException("Firstly you should start the game.");
        }
    }


}
