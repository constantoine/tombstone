package xyz.lalivre.tombstone.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryOpenEvents implements Listener {
    private final NamespacedKey expKey;

    public InventoryOpenEvents(@NotNull JavaPlugin plugin) {
        this.expKey = new NamespacedKey(plugin, "experience");
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
        if (!container.has(this.expKey, PersistentDataType.INTEGER)) {
            return;
        }
        World world = player.getWorld();
        PlayerInventory playerInventory = player.getInventory();
        Integer xp = container.get(this.expKey, PersistentDataType.INTEGER);
        assert xp != null;
        player.giveExp(xp, true);
        container.remove(this.expKey);
        ArrayList<ItemStack> content = new ArrayList<>();
        for (NamespacedKey key : container.getKeys()) {
            if (!key.namespace().equals(this.expKey.namespace())) {
                continue;
            }
            content.add(ItemStack.deserializeBytes(container.get(key, PersistentDataType.BYTE_ARRAY)));
        }
        HashMap<Integer, ItemStack> toDrop = playerInventory.addItem(content.toArray(new ItemStack[0]));
        Location chestLocation = chest.getLocation();
        for (ItemStack stack : toDrop.values()) {
            world.dropItemNaturally(chestLocation, stack);
        }
        chest.getBlock().setType(Material.AIR);
    }
}
