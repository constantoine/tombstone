package xyz.lalivre.tombstone.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DeathEvents implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getDrops().size() == 0) {
            return;
        }
        Player player = event.getPlayer();
        Location location = player.getLocation();

        Block left = location.getBlock();
        left.setType(Material.CHEST);
        Chest leftSide = (Chest) left.getState();

        if (event.getDrops().size() > 27) {
            Block right = location.clone().add(1, 0, 0).getBlock();
            right.setType(Material.CHEST);
            Chest rightSide = (Chest) right.getState();

            org.bukkit.block.data.type.Chest chestData1 = (org.bukkit.block.data.type.Chest) leftSide.getBlockData();
            org.bukkit.block.data.type.Chest chestData2 = (org.bukkit.block.data.type.Chest) rightSide.getBlockData();

            chestData1.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
            leftSide.setBlockData(chestData1);
            chestData2.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
            rightSide.setBlockData(chestData2);

            leftSide.setType(Material.CHEST);
            rightSide.setType(Material.CHEST);
        }

        leftSide.setCustomName("Tombe de " + player.getName());
        leftSide.update();

        List<ItemStack> drops = event.getDrops();
        Inventory chestInventory = leftSide.getInventory();
        for (ItemStack stack : drops) {
            chestInventory.addItem(stack);
        }

        event.getDrops().clear();
    }
}
