package code.akselm.rpo.module;

import code.akselm.rpo.PlayerOnline;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by 18AxMoreen on 3/29/2016.
 */
public class QuitAction implements PlayerAction {
    @Override
    public void tick(PlayerOnline po) {
        if (ThreadLocalRandom.current().nextInt(120) == 0){
            po.disconnect();
        }
    }
}
