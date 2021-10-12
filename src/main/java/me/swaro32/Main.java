package me.swaro32;

import me.swaro32.commands.About;
import org.bukkit.plugin.java.JavaPlugin;
import me.swaro32.commands.GeoIP;

public class Main extends JavaPlugin {
    @Override
    public void onEnable () {
        this.getCommand("geoip").setExecutor(new GeoIP());
        this.getCommand("geoipinfo").setExecutor(new About());

        getLogger().info("GeoIP plugin is ready!");
    }
}
