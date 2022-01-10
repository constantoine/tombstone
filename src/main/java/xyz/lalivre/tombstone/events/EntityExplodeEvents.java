package xyz.lalivre.tombstone.events;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class EntityExplodeEvents implements Listener {
    private final JavaPlugin plugin;


    public EntityExplodeEvents(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityExplode(@NotNull EntityExplodeEvent event) {
        Iterator<Block> blocks = event.blockList().iterator();
        while (blocks.hasNext()) {
            Block block = blocks.next();
            if (block.getType() != Material.CHEST) {
                continue;
            }
            PersistentDataContainer container = ((Chest) block.getState()).getPersistentDataContainer();
            NamespacedKey expKey = DeathEvents.expKey(plugin);
            if (!container.has(expKey, PersistentDataType.INTEGER)) {
                continue;
            }
            blocks.remove();
        }
    }
}
