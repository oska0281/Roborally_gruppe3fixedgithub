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

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     *This is just some dummy controller operation to make a simple move to see something
     *happening on the board. This method should eventually be deleted!
     *@param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        Player currentPlayer = board.getCurrentPlayer();
        if(space.getPlayer() == null)
            currentPlayer.setSpace(space);
        else return;

        int currentPlayerNumber = board.getPlayerNumber(currentPlayer);
        Player nextPlayer = board.getPlayer((currentPlayerNumber + 1) % board.getPlayersNumber());
        board.setCurrentPlayer(nextPlayer);

        board.setCounter(board.getCounter() + 1);
    }


    // XXX: V2
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }


    /**
     * This method ends the programming phase, which makes the execute button active to press.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * This method executes the moves which the player has requested
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX: V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command.isInteractive()){
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    for(Player player : board.getPlayers()){
                        List<FieldAction> actions = player.getSpace().getActions();
                        if(actions != null) {
                            for (FieldAction action : actions){
                                action.landedOn(this, player.getSpace());
                            }
                            if(player.getCheckpoints() == board.getTotalCheckpoints()){
                                board.setWon(true);
                            }
                        }
                    }
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));

                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }


    /**
     *
     * @David Otzen s201386
     */
    public void executeCommandOptionAndContinue(@NotNull Command option){
        Player currentPlayer = board.getCurrentPlayer();
        if(currentPlayer != null &&
                board.getPhase()== Phase.PLAYER_INTERACTION &&
                option !=null);
        board.setPhase(Phase.ACTIVATION);
        executeCommand(currentPlayer, option);

        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        if(nextPlayerNumber < board.getPlayersNumber()){
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        } else {
            int step = board.getStep() + 1;
            for(Player player : board.getPlayers()){
                List<FieldAction> actions = player.getSpace().getActions();
                if(actions != null) {
                    for (FieldAction action : actions){
                        action.landedOn(this, player.getSpace());
                    }
                }
                if(player.getCheckpoints() == board.getTotalCheckpoints()){
                    board.setWon(true);
                }
            }
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
            } else {
                startProgrammingPhase();
            }
        }
    }





    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case FASTER_FORWARD:
                    this.fasterForward(player);
                    break;
                case U_TURN:
                    this.uTurn(player);
                    break;
                case BACK_UP:
                    this.backUp(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }




    /**
     * ...
     *
     * @author Oskar Lolk Larsen,  s215717
     *
     */

    class moveNotPossibleException extends Exception {

        private Space space;

        private Heading heading;

        private Player player;

        /**
         * Here we create the Exception moveIsNotPossible, but for now, nothing happens when thrown
         *
         * @param player
         * @param space
         * @param heading
         */

        public moveNotPossibleException(Player player, Space space, Heading heading) {
            super("Move is not possible");

            this.heading = heading;

            this.space = space;

            this.player = player;
        }
    }

    /**
     * ...
     *
     * @author Oskar Lolk Larsen,  s215717
     *
     */

    /**
     * The moveForward has been slightly modified with a catch statement at the bottom, however it has been set to be ignored since it doesn't do anything
     *
     * @param player
     */

    public void moveForward(Player player) {
        if (board != null && player != null && player.board == board) {Heading heading = player.getHeading();
            Space space = player.getSpace();
            Space target = board.getNeighbour(space, heading);
            if(target != null) {
                try {
                    movePlayerToSpace(player,target,heading);
                } catch (moveNotPossibleException ignored){
                }
            }
        }
    }

    /**
     * @author Oskar Lolk Larsen,  s215717
     */

    /**
     *
     * The movePlayerToSpace which relocates the pushed player to the next space which the pushing player is heading.
     * If none of the criteria met the moveNotPossibleException will be thrown.
     *
     * @param player
     * @param space
     * @param heading
     * @throws moveNotPossibleException
     */
    public void movePlayerToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading)
            throws moveNotPossibleException {
        Player other = space.getPlayer();
        if (other !=null) {
            Space target = board.getNeighbour(space,
                    heading);
            if (target != null) {
                movePlayerToSpace(other,
                        target,
                        heading);
            } else  {
                throw new moveNotPossibleException(player,
                        space,
                        heading);
            }
        }
        /**
         * @author Christoffer Fink 205449'
         * allowed to walk where there isnt wall
         */
        if(player.getSpace() != null){
           if(player.getSpace().getWalls() != null){
               for(Heading wall : player.getSpace().getWalls()){
                   if(wall == heading) {
                       throw new moveNotPossibleException(player, space, heading);
                   }
               }
           }
        }
        if(space.getWalls() != null){
            for(Heading wall : space.getWalls()){
                if(wall.prev().prev() == heading) {
                    throw new moveNotPossibleException(player, space, heading);
                }
            }
        }

        player.setSpace(space);
    }


    /**
     * @author Oskar Lolk Larsen,  s215717
     * Same function as moveForward, however the method is set two times to get the fastForward function
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * Here the player's direction is set to turn right
     */
    public void turnRight(@NotNull Player player) {
        if(player !=null && player.board == board){
            player.setHeading(player.getHeading().next());
        }
    }

    /**
     * Here the player's direction is set to turn left
     */
    public void turnLeft(@NotNull Player player) {
        if(player !=null && player.board == board){
            player.setHeading(player.getHeading().prev());
        }
    }
    /**@author Peter Møller s215707
     * */
    public void uTurn(@NotNull Player player) {
        if(player !=null && player.board == board){
            player.setHeading(player.getHeading().next().next());
        }
    }

    /**@author Peter Møller s215707
     * */
    public void backUp(@NotNull Player player) {
        if(player !=null && player.board == board){
            uTurn(player);
            moveForward(player);
            uTurn(player);
        }
    }


    /**@author Peter Møller s215707
     * Just as in fastForward, but here the moveForward is used one more time to get fasterForward
     */
    public void fasterForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
        moveForward(player);
    }


    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}
