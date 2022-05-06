/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.

            gameController = new GameController(initializeBoard());
            int no = result.get();
            Board board = gameController.board;
            board.attach(this);
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }
            board.setCurrentPlayer(board.getPlayer(0));

            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);
            RepositoryAccess.getRepository().createGameInDB(board);
        }
    }

    /**
     * @author Oskar Lolk Larsen s215717
     *
     * this method loads the games from the database and asks the user which of the gameID's they wish to load.
     * The system then finds the game which has the same gameID as the one requested.
     */
    public void saveGame() {
        RepositoryAccess.getRepository().updateGameInDB(gameController.board);
    }


    public void loadGame() {
        List<Integer> gameIds = new ArrayList<>();
        RepositoryAccess.getRepository().getGames().forEach(gameInDB -> gameIds.add(gameInDB.id));
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(gameIds.get(0), gameIds);
        dialog.setTitle("Select game");
        dialog.setHeaderText("Select game");
        Optional<Integer> result = dialog.showAndWait();
        if(result.isPresent()) {
            gameController = new GameController( RepositoryAccess.getRepository().loadGameFromDB(result.get()));
        }
        roboRally.createBoardView(gameController);

    }


    /**
     * @author Oskar Lolk Larsen s215717
     *
     * This method checks which boards are available
     */
    private Board initializeBoard(){
        List<String> boards = LoadBoard.getBoards();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(boards.get(0), boards);
        dialog.setTitle("Select board");
        dialog.setHeaderText("Select board");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            return LoadBoard.loadBoard(result.get());
        }
        return new Board(8,8);
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();
            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }


    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     *
     * @return
     */
    public boolean isGameRunning() {
        return gameController != null;
    }

    /**
     * Does so player can win
     * @author Christoffer Fink s205449
     * @param subject the subject which changed
     */
    @Override
    public void update(Subject subject) {
        if(subject.getClass() == Board.class){
            if(((Board) subject).isWon()){
                for (Player player: ((Board) subject).getPlayers()) {
                    if(player.getCheckpoints() == ((Board) subject).getTotalCheckpoints()) {
                        Alert alert = new Alert(AlertType.CONFIRMATION, "Game won by, " + player.getName(), ButtonType.OK);
                        alert.showAndWait();
                        stopGame();
                        //Så viser den ikke vores dialogboks mere end en gang
                        ((Board) subject).setWon(false);
                        return;
                    }
                }
            }
        }
    }

}
