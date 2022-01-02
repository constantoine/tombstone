package xyz.lalivre.tombstone.events;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockBreakEvents implements Listener {
    private final NamespacedKey expKey;

    public BlockBreakEvents(@NotNull JavaPlugin plugin) {
        this.expKey = new NamespacedKey(plugin, "experience");
    }

    @EventHandler
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.CHEST) {
            return;
        }
        PersistentDataContainer container = ((Chest) event.getBlock().getState()).getPersistentDataContainer();
        if (!container.has(this.expKey, PersistentDataType.INTEGER)) {
            return;
        }
        Player player = event.getPlayer();
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
        for (ItemStack stack : toDrop.values()) {
            world.dropItemNaturally(event.getBlock().getLocation(), stack);
        }
        event.setDropItems(false);
    }
}
