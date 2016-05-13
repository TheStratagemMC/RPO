package code.akselm.rpo.module;

import code.akselm.rpo.PlayerOnline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by 18AxMoreen on 3/30/2016.
 */
public class RandomSpeakAction implements PlayerAction {
    @Override
    public void tick(PlayerOnline po) {
        if (ThreadLocalRandom.current().nextInt(900) == 0){
            Player[] players = (Player[])Bukkit.getOnlinePlayers().toArray();
            Player target = players[ThreadLocalRandom.current().nextInt(players.length)];
            switch(ThreadLocalRandom.current().nextInt(3)){
                case 0:
                    po.getEp().getBukkitEntity().chat(target.getName() + " tpa");
                    break;
                case 1:
                    po.getEp().getBukkitEntity().chat("Hey guys");
                    break;
                case 2:
                    po.getEp().getBukkitEntity().chat(target.getName());
                    break;
                case 3:
                    po.getEp().getBukkitEntity().chat("Hello");
            }
        }
    }
}
