package code.akselm.rpo;

import code.akselm.rpo.module.PlayerAction;
import org.bukkit.Bukkit;

import java.util.*;


/**
 * Created by 18AxMoreen on 3/29/2016.
 */
public class PlayerActionIterator implements Runnable {
    public List<PlayerAction> enabledActions = new ArrayList<>();
    public int index = 0;
    Random r = new Random();
    public int tick = 0;
    public List<PlayerOnline> cache = new ArrayList<>();

    public PlayerActionIterator(){
        Collection<PlayerOnline> temp = getPlayers();
        for (PlayerOnline po : temp){
            if (!cache.contains(po)) cache.add(po);
        }
        for (PlayerOnline po : cache){
            if (!temp.contains(po)) cache.remove(po);
        }
    }
    public Collection<PlayerOnline> getPlayers(){
        return RealisticPlayersOnline.getInstance().playersOnline.values();
    }

    public void register(PlayerAction action){
        enabledActions.add(action);
    }
    public void run(){
        tick++;
        if (tick % 5 == 0){
            Collection<PlayerOnline> temp = getPlayers();
            for (PlayerOnline po : temp){
                if (!cache.contains(po)) cache.add(po);
            }
            for (PlayerOnline po : cache){
                if (!temp.contains(po)) cache.remove(po);
            }
        }

        if (index >= cache.size()) {
            index = 0;
            Collections.shuffle(cache);
        }

        if (cache.size() == 0) return;
        PlayerOnline current = cache.get(index);

        if (Bukkit.getPlayer(current.getId()) == null) return;
        if (!Bukkit.getPlayer(current.getId()).isOnline()) return;
        PlayerAction action = enabledActions.get(r.nextInt(enabledActions.size()));
        action.tick(current);
        index++;
    }
}
