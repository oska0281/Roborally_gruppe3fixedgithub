package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Christoffer Fink s205449
 */
public abstract class FieldAction {
   public abstract boolean landedOn(@NotNull GameController gameController, @NotNull Space space);
}
