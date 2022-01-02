package xyz.lalivre.tombstone;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lalivre.tombstone.events.BlockBreakEvents;
import xyz.lalivre.tombstone.events.DeathEvents;
import xyz.lalivre.tombstone.events.InventoryOpenEvents;

public final class Tombstone extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathEvents(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakEvents(this), this);
        getServer().getPluginManager().registerEvents(new InventoryOpenEvents(this), this);
        getServer().getConsoleSender().sendMessage(
                Component.text("[Tombstone]: ").color(NamedTextColor.GOLD).append(
                        Component.text("Starting plugin.").color(NamedTextColor.GREEN)
                ));

    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(
                Component.text("[Tombstone]: ").color(NamedTextColor.GOLD).append(
                        Component.text("Shutting down plugin.").color(NamedTextColor.RED)
                ));
    }
}
