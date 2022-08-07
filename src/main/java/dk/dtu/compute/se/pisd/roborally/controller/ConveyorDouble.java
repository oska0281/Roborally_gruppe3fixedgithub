package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class ConveyorDouble extends FieldAction{
    Heading heading;

    public ConveyorDouble(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean landedOn(@NotNull GameController gameController, @NotNull Space space) {
        if(space != null){
            Player player  = space.getPlayer();
            Space neighbour = gameController.board.getNeighbour(space, heading);
            if(player != null && neighbour != null){
                try {
                    gameController.movePlayerToSpace(player, neighbour, heading);
                    neighbour=gameController.board.getNeighbour(player.getSpace(), heading);
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
