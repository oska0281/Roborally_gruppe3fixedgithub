package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/** @author Ahmed Nassaralha s215702
 *
 */
public class ConveyorBelt extends FieldAction{
    Heading heading;

    public ConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    /**
     *
     *This method checks if any players is located on space and neighbour space.
     *If both spaces free it will move player to neighbour
     * @param gameController
     * @param space
     * @return
     */
    @Override
    public boolean landedOn(@NotNull GameController gameController, @NotNull Space space) {
        if(space != null){
            Player player  = space.getPlayer();
            Space neighbour = gameController.board.getNeighbour(space, heading);
            if(player != null && neighbour != null){
                player.setHeading(heading);
                try {
                    gameController.movePlayerToSpace(player, neighbour, heading);
                }catch (GameController.moveNotPossibleException e){
                    //Gør ikke noget
                }
                return true;
            }
        }
        return false;
    }

    public Heading getHeading() {
        return heading;
    }
}
