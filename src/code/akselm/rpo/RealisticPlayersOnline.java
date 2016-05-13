package code.akselm.rpo;

import code.akselm.rpo.data.FileSaver;
import code.akselm.rpo.data.Saver;
import code.akselm.rpo.module.AchievementAction;
import code.akselm.rpo.module.QuitAction;
import code.akselm.rpo.module.RandomSpeakAction;
import com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by 18AxMoreen on 3/25/2016.
 */
public class RealisticPlayersOnline extends JavaPlugin implements Listener {
    final Map<UUID,PlayerOnline> playersOnline = new HashMap<>();
    PlayerGenerator generator;
    private static RealisticPlayersOnline instance;
    PlayerActionIterator iterator;
    SecureRandom r = new SecureRandom();
    NameGenerator names;
    String pluginName = "RPO";
    int difficulty = 1;
    String commandName = "rpo";
    Saver saver;
    String namesFile = null;
    int threshold = 15;
    int minAmount = 2;
    int maxAmount = 5;
    int streak = 0;
    int speed = 3; //from 1 to 20

    public static RealisticPlayersOnline getInstance(){
        return instance;
    }
    public void onEnable(){
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);

        if (!getDataFolder().exists()) getDataFolder().mkdir();

        saver = new FileSaver(new File(getDataFolder(), "saves"));
        //try to load from config
        loadConfig();
        difficulty = r.nextInt(40)+10;
        iterator = new PlayerActionIterator();
        iterator.register(new AchievementAction());
        iterator.register(new QuitAction());
        iterator.register(new RandomSpeakAction());
        generator = new PlayerGenerator(threshold, minAmount, maxAmount);
        if (namesFile == null) names = new NameGenerator("names.txt");
        else names = new NameGenerator(new File(getDataFolder(), namesFile));
        //change plugin name with reflection
        try{
            Field f = getDescription().getClass().getField("name");
            f.setAccessible(true);
            f.set(getDescription(), pluginName);
        }catch(Exception e){
            e.printStackTrace();
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, iterator, 20l, 20l);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                //randomize the joining a bit
                if (r.nextInt(8) < 1 && streak < 1) return;
                if (r.nextInt(difficulty) != 0) return;
                //streak++;
                if (streak > 3) {
                    streak = 0;
                    return;
                }
                if (r.nextBoolean()) return;

                List<UUID> realPlayers = new ArrayList<>();
                Set<UUID> foundPlayersOnline = new HashSet<>();
                for (Player player : Bukkit.getOnlinePlayers()){
                    if (!playersOnline.containsKey(player.getUniqueId())) realPlayers.add(player.getUniqueId());
                    if (playersOnline.containsKey(player.getUniqueId())) foundPlayersOnline.add(player.getUniqueId());
                }
                for (UUID id : playersOnline.keySet()){
                    if (!foundPlayersOnline.contains(id)) playersOnline.remove(id);
                }


                //Bukkit.broadcastMessage("Real players: "+realPlayers.size());
                int diff = generator.getAmountToHave(realPlayers.size()) - playersOnline.size();

                System.out.println("PO: "+playersOnline.size());
                System.out.println("Diff: "+diff);
                if (diff < 0){
                    for (int i = 0; i > diff; i--){
                        List<UUID> keys = new ArrayList<>(playersOnline.keySet());
                        Collections.shuffle(keys);

                        PlayerOnline po = playersOnline.get((UUID)keys.get(0));
                        playersOnline.remove(po.getId());
                        po.disconnect();

                    }
                }
                else if (diff == 0) return;
                else {
                    if (r.nextBoolean()) return;
                    streak++;
                    // for (int i = 0; i < Math.min(1, diff)//; i++){
                    if (r.nextInt(4) > 1) return;
                    String name = names.nextName();
                    if (name.length() >= 16) name = name.substring(0, 15);
                    final String nextName = name;
                    PlayerStore.getInfo(nextName, new FuturePlayerInfo() {
                        @Override
                        public void info(PlayerInfo info) {
                            PlayerOnline po;
                            if (info.containsKey("uuid")){
                                po = new PlayerOnline(nextName, (UUID)info.get("uuid"));
                            }
                            else po = new PlayerOnline(nextName);
                            po.join((CraftServer) Bukkit.getServer());
                            playersOnline.put(po.getId(), po);
                            info.set("uuid", po.getId());
                            PlayerStore.saveInfo(nextName, info);
                            po.getEp().getBukkitEntity().setGameMode(GameMode.CREATIVE);

                        /*for (Player player : Bukkit.getOnlinePlayers()){
                            player.hidePlayer(po.getEp().getBukkitEntity());
                            //System.out.println("Test");
                            if (player.getUniqueId().equals(po.getId())) System.out.println("In list.");
                        }*/
                            po.getEp().getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
                            po.getEp().getBukkitEntity().performCommand("rtp");
                        }
                    });

                }
                   // }
                }

        }, 20*(50/(speed)), 20*(50/speed));



    }


 /*   @EventHandler
    public void onJoin(PlayerJoinEvent event){
        d("" + event.getPlayer().getUniqueId());
        CraftPlayer cp = (CraftPlayer)event.getPlayer();
        d(""+(cp.getHandle().playerConnection.networkManager.channel == null));
    }*/
    public void d(String m){
        Bukkit.broadcast(m, "test");
    }
    public void loadConfig(){
        File config = new File(getDataFolder(), "config.yml");
        if (config.exists()){


            YamlConfiguration c = new YamlConfiguration();
            try{
                c.load(config);
            }catch(Exception e){
                e.printStackTrace();
            }
            threshold = c.getInt("threshold");
            minAmount = c.getInt("minAmount");
            maxAmount = c.getInt("maxAmount");
            speed = c.getInt("speed");
            pluginName = c.getString("pluginName");
            commandName = c.getString("commandName");
            if (c.getString("namesFile") == null) return;
            if (!c.getString("namesFile").equals("changeThisToAFileInThePluginDirectory.txt")){
                namesFile = c.getString("namesFile");
            }

        }
        else{
            getLogger().info("First time running RPO, running with default parameters...");
            try{
                InputStream in = getClass().getResourceAsStream("config.yml");
                File file = new File(getDataFolder(), "config.yml");
                if (!file.exists()) file.createNewFile();
                FileOutputStream fout = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(fout);
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = r.readLine()) != null){
                    pw.println(line);
                }
                r.close();
                pw.close();
                fout.close();
                in.close();
            }catch(Exception e){
                e.printStackTrace();
            }


        }
    }


    @EventHandler
    public void command(PlayerCommandPreprocessEvent event){
        if (event.getMessage().toLowerCase().startsWith("/"+commandName + " ")){
           /* for (PlayerOnline po : playersOnline.values()){
              /*  Bukkit.broadcastMessage((Bukkit.getPlayer(po.getName()) == null) + "");
                Bukkit.broadcastMessage((Bukkit.getPlayer(po.getId()) == null) + "");
                Bukkit.broadcastMessage((Bukkit.getPlayerExact(po.getName()) == null) + "");
*/
           // }

            String[] s = event.getMessage().split(" ");
            String[] args = Arrays.copyOfRange(s, 1, s.length);
            if (!event.getPlayer().hasPermission("rpo.admin")) {
                return;
            }
            if (args.length == 0){
                event.getPlayer().sendMessage(ChatColor.GREEN + "Please specify an argument.");
                event.setCancelled(true);
                return;
            }
            if (args[0].equalsIgnoreCase("reload")){
                loadConfig();
                event.getPlayer().sendMessage(ChatColor.AQUA + "Configuration reloaded.");
                event.setCancelled(true);
            }

        }
    }
}
