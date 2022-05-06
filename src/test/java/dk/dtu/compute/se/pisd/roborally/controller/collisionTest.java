package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class collisionTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }


    @AfterEach
    void tearDown() {
        gameController = null;
    }
}
    /**
     * @author Christoffer Fink s205449
     * IGANGVÆRENDE COLLISION TEST
     */
/**
    @Test
    void collision() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);
        board.getSpace(0, 0).getPlayer();
        gameController.moveForward(player1);
        gameController.moveForward(player2);
        Assertions.assertEquals(player1, board.getSpace(0, 1).getPlayer(), "Player " + player1.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(player2, board.getSpace(0, 2).getPlayer(), "Player " + player2.getName() + " should beSpace (0,2)!");

    }





