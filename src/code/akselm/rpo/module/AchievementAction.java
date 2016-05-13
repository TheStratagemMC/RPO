package code.akselm.rpo.module;

import code.akselm.rpo.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by 18AxMoreen on 3/29/2016.
 */
public class AchievementAction implements PlayerAction {
    @Override
    public void tick(final PlayerOnline po) {
        if (ThreadLocalRandom.current().nextInt(100) > 1) return;
        PlayerStore.getInfo(po.getName(), new FuturePlayerInfo() {
            @Override
            public void info(PlayerInfo info) {
                int achievement = 0;
                if (info.containsKey("achievement"))  achievement = (int)info.get("achievement");
                    if (info.containsKey("last_achievement")){
                        if (((System.currentTimeMillis() - ((long)info.get("last_achievement"))) < 10000)) return;
                    }
                    int newInt = achievement+1;
                    if (ThreadLocalRandom.current().nextInt(newInt*newInt) != 0 && ThreadLocalRandom.current().nextBoolean()) return;
                    if (RAchievement.containsAchievement(achievement +1)){
                        RAchievement a = RAchievement.getAchievement(achievement +1);
                        if (ThreadLocalRandom.current().nextInt((int)Math.pow((double)a.getDifficulty(), (double)a.getDifficulty())) > 0) return;
                        po.getEp().getBukkitEntity().awardAchievement(a.getAchievement());
                        info.set("achievement", achievement+1);
                        info.set("last_achievement", System.currentTimeMillis());
                        PlayerStore.saveInfo(po.getName(), info);
                    }
            }
        });
    }
}
