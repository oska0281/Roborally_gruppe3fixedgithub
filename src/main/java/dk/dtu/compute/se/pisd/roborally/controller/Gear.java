package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * @author Peter Møller s215707
 */
public class Gear extends FieldAction{
    public final static int LEFT_TURN = 1;
    public final static int RIGHT_TURN = -1;

    private int directionOfTurn;

    public Gear(int directionOfTurn) {
        this.directionOfTurn = directionOfTurn;
    }


    @Override
    public boolean landedOn(@NotNull GameController gameController, @NotNull Space space) {
        Heading newHeading;
        if(directionOfTurn == LEFT_TURN){
            newHeading = space.getPlayer().getHeading().prev();
        }else {
            newHeading = space.getPlayer().getHeading().next();
        }
        space.getPlayer().setHeading(newHeading);
        return true;
    }

    public int getDirectionOfTurn() {
        return directionOfTurn;
    }
}
