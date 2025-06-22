package jp.momonnga.wallkick;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Represents a WallKicker.
 */
public class WallKicker {
    public static final String WALLKICKER_PERMISSION = "wallkick.wallkicker";
    public static final String VERTICALKICK_PERMISSION = "wallkicker.verticalkick";
    private static final Map<UUID,WallKicker> kickers = new HashMap<>();
    private final Player player;
    private int maxKickCombo = 2;
    private int kickCombo = 0;
    private double footReach = 0.5;
    private int wallHeight = 1;

    /**
     * Returns a {@code WallKicker} converted from a {@code Player} or already converted.
     * @param   player
     *          {@code Player} to be converted.
     * @return the resulting {@code WallKicker}.
     */
    public static WallKicker of(Player player) {
        UUID uuid = player.getUniqueId();
        if (!kickers.containsKey(uuid)) {
            kickers.put(uuid,new WallKicker(player));
        }
        return kickers.get(uuid);
    }

    private WallKicker(Player player) {
        this.player = player;
    }

    /**
     * Kick the wall.
     */
    public void kickWall() {
        if (!canKickWall()) return;

        Vector jumpVector = player.getVelocity().clone();
        boolean isVerticalKick = jumpVector.getX() == 0 && jumpVector.getZ() == 0;

        jumpVector.multiply(3);
        jumpVector.setY(0.7);

        if (isVerticalKick) {
            if (!player.hasPermission(VERTICALKICK_PERMISSION)) return;
        } else {
            Vector opposite = player.getFacing().getOppositeFace().getDirection();
            if (opposite.getX() == 0) {
                jumpVector.setZ(opposite.getZ() * 0.3);
            }
            if (opposite.getZ() == 0) {
                jumpVector.setX(opposite.getX() * 0.3);
            }
        }

        WallKickEvent event = new WallKickEvent(this,jumpVector);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        player.setVelocity(jumpVector);
        kickCombo++;
    }

    /**
     * Returns true if the player meets the requirements to wall kick.
     *
     * <p> <h3>Requirements for kicking wall</h3>
     * <ul>
     * <li>Player must have permission to wall kick.</li>
     * <li>Player must be Jumping.</li>
     * <li>The Wall must exist in the direction the player is facing.</li>
     * <li>The distance between the player and the wall must be within {@code footReach}.</li>
     * <li>Player's wall kick combo must not reach {@code maxKickCombo}.</li>
     * </ul>
     *
     * @return {@code true} if {@code player} can kick wall.
     * */
    public boolean canKickWall() {
        if (!isWallKicker()) return false;
        if (WallKickUtil.isOnGround(player)) return false;
        if (!WallKickUtil.isFacingWall(player,wallHeight)) return false;
        if (!reachWall()) return false;
        if (!checkWallKickCombo()) return false;

        if (player.getVelocity().getX() == 0 && player.getVelocity().getZ() == 0) {
            return isVerticalKicker();
        }

        return true;
    }

    /**
     * Returns true if the player has the permission to kick the wall.
     *
     * @return True if player is wallkicker.
     */
    public boolean isWallKicker() {
        return player.hasPermission(WALLKICKER_PERMISSION);
    }

    /**
     * Returns true if the player has the permission to vertical kick.
     * @return True if player is verticalkicker.
     */
    public boolean isVerticalKicker() {
        return player.hasPermission(VERTICALKICK_PERMISSION);
    }

    /**
     * Returns are true if the distance to the wall is less than or equal to footReach.
     * @return True if the player's feet can reach the wall.
     */
    public boolean reachWall() {
        RayTraceResult result = player.getWorld().rayTraceBlocks(player.getLocation(),player.getFacing().getDirection(),1);
        if (result == null) return false;
        return result.getHitPosition().distance(player.getLocation().toVector()) <= footReach;
    }

    /**
     * returns true if {@code kickCombo} has not reached {@code maxKickCombo}.
     * @return True if {@code kickCombo} has not reached {@code maxKickCombo}.
     */
    public boolean checkWallKickCombo() {
        return kickCombo < maxKickCombo;
    }

    /**
     * Returns the number of kick combos of the player.
     * @return the number of kick combos.
     */
    public int getKickCombo() {
        return kickCombo;
    }

    /**
     * Sets the number of kick combos of the player.
     * @param kickCombo the number of kick combos.
     */
    public void setKickCombo(int kickCombo) {
        this.kickCombo = kickCombo;
    }

    /**
     * Returns the reach of the player's legs.
     * <p> Used to determine the distance between the player and the wall.
     * @return the reach of the player's legs.
     */
    public double getFootReach() {
        return footReach;
    }

    /**
     * Sets the reach of the player's legs.
     * <p> Used to determine the distance between the player and the wall.
     * @param footReach the reach of the player's legs.
     */
    public void setFootReach(double footReach) {
        this.footReach = footReach;
    }

    /**
     * Returns the upper limit of the player's kick combo.
     * @return the upper limit of the player's kick combo.
     */
    public int getMaxKickCombo() {
        return maxKickCombo;
    }

    /**
     * Sets the upper limit of the player's kick combo.
     * @param  maxKickCombo the upper limit of the player's kick combo.
     */
    public void setMaxKickCombo(int maxKickCombo) {
        this.maxKickCombo = maxKickCombo;
    }

    /**
     * Returns the height of the block to be accepted as a wall.
     * @return the height of the block to be accepted as a wall.
     */
    public int getWallHeight() {
        return wallHeight;
    }

    /**
     * Sets the height of the block to be accepted as a wall.
     * @param wallHeight the height of the block to be accepted as a wall.
     */
    public void setWallHeight(int wallHeight) {
        this.wallHeight = wallHeight;
    }

    @Override
    public String toString() {
        return "WallKicker{" +
                "player=" + player +
                ", maxKickCombo=" + maxKickCombo +
                ", kickCombo=" + kickCombo +
                ", footReach=" + footReach +
                ", wallHeight=" + wallHeight +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer());
    }


    public Player getPlayer() {
        return player;
    }
}
