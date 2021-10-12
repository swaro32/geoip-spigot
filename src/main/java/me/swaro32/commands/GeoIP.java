package me.swaro32.commands;

import me.swaro32.IP;
import me.swaro32.Time;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.*;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class GeoIP implements CommandExecutor {
    @Override
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
        if ( args.length == 1 || (args.length == 2 && args[1].equals("-ip")) ) {
            String ipResponseJson = "";
            String timeResponseJson = "";
            boolean noCurrentTime = false;
            InetAddress ip_inet;

            if ( args.length == 1 ) {
                Player player = sender.getServer().getPlayer(args[0]);

                if ( player == null ) {
                    sender.sendMessage("§cPlayer not found");
                    return false;
                }

                try {
                    ip_inet = InetAddress.getByName(player.getAddress().getHostString());
                } catch (UnknownHostException e) {
                    sender.sendMessage("§4Fatal error. Please check console");
                    e.printStackTrace();
                    return false;
                }
            } else if (args[1].equals("-ip")) {
                try {
                    ip_inet = InetAddress.getByName(args[0]);
                } catch (UnknownHostException e) {
                    sender.sendMessage("§cWrong IP address");
                    e.printStackTrace();
                    return false;
                }
            } else {
                sender.sendMessage("§aUsage: §f/geoip §b(nickname) §eor §f/geoip §b(ip) -ip");
                return false;
            }

            if ( ip_inet.getHostAddress().equals("127.0.0.1") ) {
                sender.sendMessage("§cCan't use GeoIP on localhost");
                return false;
            }

            sender.sendMessage("§aPlease wait...");

            try {
                URLConnection connectionToApi = new URL("http://ip-api.com/json/"+ip_inet.getHostAddress()+
                        "?fields=continent,country,regionName,city,timezone,offset,proxy").openConnection();

                Scanner scanner = new Scanner(connectionToApi.getInputStream()).useDelimiter("\\A");
                ipResponseJson = scanner.next();
            } catch (MalformedURLException e) {
                sender.sendMessage("§4Whoops... Something went wrong. Check your console.");
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                sender.sendMessage("§4Whoops... Can't connect to API. Check your console.");
                e.printStackTrace();
                return false;
            }

            Gson gson = new Gson();
            IP ip_info = gson.fromJson(ipResponseJson, IP.class);

            String currentTime = "§cUnknown";

            if ( !noCurrentTime ) {
                Calendar calendar = Calendar.getInstance();
                TimeZone timezone = calendar.getTimeZone();

                Date unixtimeToDate = new Date( new Date().getTime() - timezone.getRawOffset() + (ip_info.offset * 1000L) );
                currentTime = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss").format(unixtimeToDate);
            }

            String usingProxyString = "No";

            if ( ip_info.proxy ) {
                usingProxyString = "§4§lYes";
            }

            String offsetPlusOrMinus = "";

            if ( ip_info.offset > 0 ) {
                offsetPlusOrMinus = "+";
            } else if ( ip_info.offset < 0 ) {
                offsetPlusOrMinus = "-";
            }

            sender.sendMessage("§aIP: §f"+ip_inet.getHostAddress()+
                    "\n§aContinent: §f"+ip_info.continent+
                    "\n§aCountry: §f"+ip_info.country+
                    "\n§aRegion: §f"+ip_info.regionName+
                    "\n§aCity: §f"+ip_info.city+
                    "\n§aTime zone: §f"+ip_info.timezone+
                    "\n§aTime offset: §f"+offsetPlusOrMinus+ip_info.offset / 60 / 60+" §ahours"+
                    "\n§aCurrent time: §f"+currentTime+
                    "\n§aUsing proxy: §f"+usingProxyString);

        } else {
            sender.sendMessage("§aUsage: §f/geoip §b(nickname) §aor §f/geoip §b(ip) §f-ip");
            return false;
        }

        return true;
    }
}
