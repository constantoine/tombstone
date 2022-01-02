package xyz.lalivre.tombstone.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeathEvents implements Listener {
    private final JavaPlugin plugin;

    public DeathEvents(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    private static Location getNearbyAirBlock(@NotNull Location loc) {
        int radius = 5;
        final World world = loc.getWorld();
        for (int y = 0; y < radius; y++) {
            for (int x = 0; x < radius; x++) {
                for (int z = 0; z < radius; z++) {
                    Block block = world.getBlockAt((int) loc.getX() + x, (int) loc.getY() + y, (int) loc.getZ() + z);
                    if (block.getType() == Material.AIR) {
                        return new Location(world, loc.getX() + x, loc.getY() + y, loc.getZ() + z);
                    }
                }
            }
        }
        return loc;
    }

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        if (event.getDrops().size() == 0) {
            return;
        }
        Player player = event.getPlayer();
        Location location = getNearbyAirBlock(player.getLocation());

        Block left = location.getBlock();
        left.setType(Material.CHEST);
        Chest leftSide = (Chest) left.getState();
        PersistentDataContainer container = leftSide.getPersistentDataContainer();
        List<ItemStack> drops = event.getDrops();
        container.set(new NamespacedKey(this.plugin, "experience"), PersistentDataType.INTEGER, event.getDroppedExp());
        for (int i = 0; i < drops.size(); i++) {
            NamespacedKey key = new NamespacedKey(this.plugin, Integer.toString(i));
            container.set(key, PersistentDataType.BYTE_ARRAY, drops.get(i).serializeAsBytes());
        }

        leftSide.setCustomName("Tombe de " + player.getName());
        leftSide.update();

        event.getDrops().clear();
    }
}
