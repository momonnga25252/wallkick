package jp.momonnga.wallkick;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public final class WallKickUtil {

    public static boolean isOnGround(Player player) {
        Location loc = player.getLocation().clone().subtract(0, 0.1, 0);
        Material type = loc.getBlock().getType();
        return isSteppableBlock(type);
    }

    private static boolean isSteppableBlock(Material type) {
        return type.isSolid() ||
                type.name().contains("SLAB") ||
                type.name().contains("STAIRS") ||
                type.name().contains("CARPET") ||
                type.name().contains("SNOW");
    }

    public static boolean isFalling(Player player) {
        return player.getVelocity().getY() < -0.08; // 落下中とみなす
    }

    /**
     * Returns true if player is facing wall.
     * @param   player
     *          {@code Player} to be converted.
     * @param   wallHeight
     *          Height of wall to be checked.
     * @return True if player is facing wall.
     */
    public static boolean isFacingWall(Player player, int wallHeight) {
        Location pLoc = player.getLocation();
        BlockFace pFace = player.getFacing();
        World world = player.getWorld();
        Block baseBlock = world.getBlockAt(pLoc).getRelative(pFace);

        for (int i = 0;i < wallHeight;i++) {
            // 正面にあるブロックが衝突できないか空である場合は壁とみなさない
            if (baseBlock.isPassable() || baseBlock.isEmpty()) return false;
            // 最初に判定したブロックから指定数のブロックが積み重なって壁を形成しているか確認する
            baseBlock = baseBlock.getRelative(BlockFace.UP);
        }
        return true;
    }
}
