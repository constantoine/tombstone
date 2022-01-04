package xyz.lalivre.tombstone.events;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public class InventoryOpenEvents implements Listener {
    private final JavaPlugin plugin;

    public InventoryOpenEvents(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof Chest chest)) {
            return;
        }
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        PersistentDataContainer container = chest.getPersistentDataContainer();
        World world = player.getWorld();
        try {
            DeathEvents.lootTombstone(container, chest.getBlock().getLocation(), player, world, this.plugin);
        } catch (IllegalArgumentException exception) {
            return;
        }
        chest.getBlock().setType(Material.AIR);
    }
}
