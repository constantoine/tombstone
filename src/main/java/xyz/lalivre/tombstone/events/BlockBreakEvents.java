package xyz.lalivre.tombstone.events;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BlockBreakEvents implements Listener {
    private final JavaPlugin plugin;

    public BlockBreakEvents(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.CHEST) {
            return;
        }
        PersistentDataContainer container = ((Chest) event.getBlock().getState()).getPersistentDataContainer();
        Player player = event.getPlayer();
        World world = player.getWorld();
        try {
            DeathEvents.lootTombstone(container, event.getBlock().getLocation(), player, world, this.plugin);
        } catch (IllegalArgumentException exception) {
            return;
        }
        event.setDropItems(false);
    }
}
