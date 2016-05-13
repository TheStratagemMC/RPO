package code.akselm.rpo;

import org.bukkit.Achievement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18AxMoreen on 3/29/2016.
 */
public class RAchievement {

    private static Map<Integer,RAchievement> achievementMap = new HashMap<>();

    private Achievement achievement;
    private int level;
    private int difficulty = 1;

    private RAchievement(int level, Achievement achievement){
        this.level = level;
        this.achievement = achievement;
    }

    private RAchievement(int level, Achievement achievement, int difficulty){
       this.level = level;
        this.achievement = achievement;
        this.difficulty = difficulty;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public int getLevel() {
        return level;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public static RAchievement getAchievement(int level){
        return achievementMap.get(level);
    }

    public static boolean containsAchievement(int level){
        return achievementMap.containsKey(level);
    }
    private static void a(int level, Achievement achievement){
        achievementMap.put(level, new RAchievement(level, achievement));
    }
    private static void a(int level, Achievement achievement, int difficulty){
        achievementMap.put(level, new RAchievement(level, achievement, difficulty));
    }
    static{
        a(1, Achievement.OPEN_INVENTORY);
        a(2, Achievement.MINE_WOOD);
        a(3, Achievement.BUILD_WORKBENCH, 2);
        a(4, Achievement.BUILD_PICKAXE, 2);
        a(5, Achievement.BUILD_FURNACE, 4);
        a(6, Achievement.ACQUIRE_IRON, 2);
        a(7, Achievement.BUILD_HOE, 5);
        a(8, Achievement.MAKE_BREAD, 3);
        a(9, Achievement.BAKE_CAKE, 5);
        a(10, Achievement.BUILD_BETTER_PICKAXE);
        a(11, Achievement.COOK_FISH);
        a(12, Achievement.ON_A_RAIL, 5);
        a(13, Achievement.BUILD_SWORD);
        a(14, Achievement.KILL_ENEMY);
        a(15, Achievement.KILL_COW);
        a(16, Achievement.FLY_PIG, 10);
    }
}
