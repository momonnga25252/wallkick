package jp.momonnga.wallkick;

import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.bukkit.ChatColor.*;


public final class WallKick extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("wallkick")).setExecutor(this);
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length == 0) return false;
            if (sender instanceof Player player) {
                return switch (args[0]) {
                    case "enable" -> {
                        if (args.length == 1) {
                            player.addAttachment(this).setPermission(WallKicker.WALLKICKER_PERMISSION, true);
                            sendPluginMessage(player, "Permissions have been added to the player.");
                        } else {
                            String playerId = args[1];
                            Player target = getServer().getPlayer(playerId);
                            if (target != null) {
                                target.addAttachment(this).setPermission(WallKicker.WALLKICKER_PERMISSION, true);
                                sendPluginMessage(player, "Permissions have been added to the player.");
                                sendPluginMessage(target, "Permission was granted to kick the wall.");
                            } else {
                                sendPluginMessage(player, RED + "Player ID does not exist.");
                            }
                        }
                        yield true;
                    }
                    case "disable" -> {
                        if (args.length == 1) {
                            player.addAttachment(this).setPermission(WallKicker.WALLKICKER_PERMISSION, false);
                            sendPluginMessage(player, "Permission to kick the wall has been revoked.");
                        } else {
                            String playerId = args[1];
                            Player target = getServer().getPlayer(playerId);
                            if (target != null) {
                                target.addAttachment(this).setPermission(WallKicker.WALLKICKER_PERMISSION, false);
                                sendPluginMessage(player, "The player's permission revoked.");
                                sendPluginMessage(target, "Permission to kick the wall has been revoked.");
                            } else {
                                sendPluginMessage(player, RED + "Player ID does not exist.");
                            }
                        }
                        yield true;
                    }
                    case "vertical" -> {
                        if (args.length == 1) {
                            sendPluginMessage(player, RED + "The command argument is invalid.");
                        } else {
                            Player target = player;
                            if (args.length == 3) {
                                String playerId = args[2];
                                target = getServer().getPlayer(playerId);
                                if (target == null) {
                                    sendPluginMessage(player, RED + "Player ID does not exist.");
                                    yield true;
                                }
                            }

                            if (args[1].equalsIgnoreCase("enable")) {
                                target.addAttachment(this).setPermission(WallKicker.VERTICALKICK_PERMISSION, false);
                                sendPluginMessage(player, "Permissions have been added to the player.");
                                if (!target.equals(player)) {
                                    sendPluginMessage(target, "Permission was granted to kick the wall.");
                                }
                                target.addAttachment(this).setPermission(WallKicker.VERTICALKICK_PERMISSION, true);
                            } else if (args[1].equalsIgnoreCase("disable")) {
                                target.addAttachment(this).setPermission(WallKicker.VERTICALKICK_PERMISSION, false);
                                sendPluginMessage(player, "The player's permission revoked.");
                                if (!target.equals(player)) {
                                    sendPluginMessage(target, "Permission to kick the wall has been revoked.");
                                }
                            } else {
                                sendPluginMessage(player, RED + "The command argument is invalid.");
                            }
                        }
                        yield true;
                    }
                    case "maxcombo" -> {
                        if (args.length == 1) {
                            sendPluginMessage(player, RED + "The command argument is invalid.");
                        } else {
                            WallKicker target = WallKicker.of(player);
                            if (args.length == 3) {
                                String playerId = args[2];
                                Player targetPlayer = getServer().getPlayer(playerId);
                                if (targetPlayer == null) {
                                    sendPluginMessage(player, RED + "Player ID does not exist.");
                                    yield true;
                                }
                                target = WallKicker.of(targetPlayer);
                            }
                            try {
                                int maxCombo = Integer.parseInt(args[1]);
                                int oldMax = target.getMaxKickCombo();
                                target.setMaxKickCombo(maxCombo);
                                sendPluginMessage(player, "Max kick combo has been changed. " + oldMax + "→" + maxCombo);
                                if (!target.equals(player)) {
                                    sendPluginMessage(target, "Max kick combo has been changed. " + oldMax + "→" + maxCombo);
                                }
                            }catch (NumberFormatException e) {
                                sendPluginMessage(player,RED + "The Number is invalid.");
                            }
                        }
                        yield true;
                    }
                    default -> false;
                };
            } else if (sender instanceof BlockCommandSender || sender instanceof ConsoleCommandSender) {
                return switch (args[0]) {
                    case "enable" -> {
                        if (args.length == 1) {
                            sendPluginMessage(sender, RED + "Specify the player.");
                            yield true;
                        } else {
                            String playerId = args[1];
                            Player target = getServer().getPlayer(playerId);
                            if (target != null) {
                                target.addAttachment(this).setPermission(WallKicker.WALLKICKER_PERMISSION, true);
                                sendPluginMessage(sender, "Permissions have been added to the player.");
                                sendPluginMessage(target, "Permission was granted to kick the wall.");
                            } else {
                                sendPluginMessage(sender, RED + "Player ID does not exist.");
                            }
                        }
                        yield true;
                    }
                    case "disable" -> {
                        if (args.length == 1) {
                            sendPluginMessage(sender, RED + "Specify the player.");
                        } else {
                            String playerId = args[1];
                            Player target = getServer().getPlayer(playerId);
                            if (target != null) {
                                target.addAttachment(this).setPermission(WallKicker.WALLKICKER_PERMISSION, false);
                                sendPluginMessage(sender, "The player's permission revoked.");
                                sendPluginMessage(target, "Permission to kick the wall has been revoked.");
                            } else {
                                sendPluginMessage(sender, RED + "Player ID does not exist.");
                            }
                        }
                        yield true;
                    }
                    case "vertical" -> {
                        if (args.length == 1) {
                            sendPluginMessage(sender, RED + "The command argument is invalid.");
                        } else {
                            Player target;
                            if (args.length == 3) {
                                String playerId = args[2];
                                target = getServer().getPlayer(playerId);
                                if (target == null) {
                                    sendPluginMessage(sender, RED + "Player ID does not exist.");
                                    yield true;
                                }
                            } else {
                                sendPluginMessage(sender, RED + "Specify the player.");
                                yield true;
                            }

                            if (args[1].equalsIgnoreCase("enable")) {
                                target.addAttachment(this).setPermission(WallKicker.VERTICALKICK_PERMISSION, false);
                                sendPluginMessage(sender, "Permissions have been added to the player.");
                                if (target.equals(sender)) {
                                    sendPluginMessage(target, "Permission was granted to kick the wall.");
                                }
                                target.addAttachment(this).setPermission(WallKicker.VERTICALKICK_PERMISSION, true);
                            } else if (args[1].equalsIgnoreCase("disable")) {
                                target.addAttachment(this).setPermission(WallKicker.VERTICALKICK_PERMISSION, false);
                                sendPluginMessage(sender, "The player's permission revoked.");
                                if (target.equals(sender)) {
                                    sendPluginMessage(target, "Permission to kick the wall has been revoked.");
                                }
                            } else {
                                sendPluginMessage(sender, RED + "The command argument is invalid.");
                            }
                        }
                        yield true;
                    }
                    default -> false;
                };
            } else {
                return false;
            }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            if (args[0].length() == 0) {
                return List.of("enable","disable","vertical","maxcombo");
            } else {
                return Stream.of("enable","disable","vertical","maxcombo").filter(arg -> arg.startsWith(args[0])).toList();
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("vertical")) {
                if (args[1].length() == 0) {
                    return List.of("enable","disable");
                } else {
                    return Stream.of("enable","disable").filter(arg -> arg.startsWith(args[1])).toList();
                }
            } else if (args[0].equalsIgnoreCase("maxcombo")) {
                return Collections.emptyList();
            }
        }
        return super.onTabComplete(sender, command, alias, args);
    }

    private void sendPluginMessage(CommandSender target, String msg) {
        String prefix = "["+ AQUA +"WallKick" + RESET + "]" + GRAY + ": "+ RESET;
        target.sendMessage(prefix + msg);
    }

    /**
     * Switching the player's allowFlight during wall-kick conditions
     * @param event {@code PlayerMoveEvent}
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event) {
        WallKicker kicker = WallKicker.of(event.getPlayer());
        if (kicker.getGameMode().equals(GameMode.CREATIVE) || kicker.getGameMode().equals(GameMode.SPECTATOR)) return;
        if (kicker.isWallKicker()) kicker.setAllowFlight(kicker.canKickWall());
        if (kicker.isOnGround() && kicker.getKickCombo() > 0) kicker.setKickCombo(0);
    }


    /**
     * Activate the wall kick to the player when you press jump twice.
     * @param event {@code PlayerToggleFlightEvent}
     */
    @EventHandler
    public void onFlightEvent(PlayerToggleFlightEvent event) {
        WallKicker kicker = WallKicker.of(event.getPlayer());
        if (kicker.getGameMode().equals(GameMode.CREATIVE) || kicker.getGameMode().equals(GameMode.SPECTATOR)) return;
        if (kicker.canKickWall()) kicker.kickWall();
        event.setCancelled(true);
    }

}
