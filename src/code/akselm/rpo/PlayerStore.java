package code.akselm.rpo;

import org.bukkit.Bukkit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18AxMoreen on 3/29/2016.
 */
public class PlayerStore {
    public static HashMap<String, PlayerInfo> cachedPlayerInfo = new HashMap<>();

    public static synchronized void getInfo(final String name, final FuturePlayerInfo info){
        if (cachedPlayerInfo.containsKey(name)) info.info(cachedPlayerInfo.get(name));
        else{
            Bukkit.getScheduler().runTaskAsynchronously(RealisticPlayersOnline.getInstance(), new Runnable() {
                @Override
                public void run() {
                    final Map<String,Serializable> store = RealisticPlayersOnline.getInstance().saver.get(name);
                    Bukkit.getScheduler().runTask(RealisticPlayersOnline.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            info.info(new PlayerInfo(name, store));
                        }
                    });
                }
            });
        }
    }

    public static synchronized void saveInfo(final String name, final PlayerInfo info){
        cachedPlayerInfo.put(name, info);
        Bukkit.getScheduler().runTaskAsynchronously(RealisticPlayersOnline.getInstance(), new Runnable() {
            @Override
            public void run() {
                RealisticPlayersOnline.getInstance().saver.save(name, info.store);
            }
        });
    }
}
