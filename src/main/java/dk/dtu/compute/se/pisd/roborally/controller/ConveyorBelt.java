package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**Ahmed Nassaralha s215702
 *
 */
public class ConveyorBelt extends FieldAction{
    Heading heading;

    public ConveyorBelt(Heading heading) {
        this.heading = heading;
    }

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
