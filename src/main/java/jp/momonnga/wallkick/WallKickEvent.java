package jp.momonnga.wallkick;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Holds information for wallkick events
 */
public class WallKickEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final WallKicker kicker;
    private final Vector kickVector;
    private boolean isCancelled = false;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public WallKickEvent(WallKicker kicker, Vector kickVector) {
        this.kicker = kicker;
        this.kickVector = kickVector;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    /**
     * Gets the {@code WallKicker} who kicked the wall.
     * @return {@code WallKicker} who kicked the wall.
     */
    public WallKicker getKicker() {
        return kicker;
    }

    /**
     * Gets the vector when {@code WallKicker} kick the wall.
     * @return Kick Vector.
     */
    public Vector getKickVector() {
        return kickVector;
    }

    /**
     * Returns true if the Kick Vector is vertical vector.
     * @return True if the Kick Vector is vertical vecotr.
     */
    public boolean isVerticalKick() {
        return kickVector.getX() == 0 && kickVector.getY() == 0;
    }

}
