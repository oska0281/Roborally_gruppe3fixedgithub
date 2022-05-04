package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * @author Oskar Lolk Larsen s215717
 */

public class Checkpoint extends FieldAction{
    private int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    @Override
    public boolean landedOn(@NotNull GameController gameController, @NotNull Space space) {
        if(space != null){
            Player player = space.getPlayer();
            if(player != null){
                if(player.getCheckpoints()+1 == checkpointNumber){
                    player.setCheckpoints(player.getCheckpoints()+1);
                }

            }
            return true;
        }
        return false;
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }

    public void setCheckpointNumber(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }
}
