package code.akselm.rpo;

/**
 * Created by 18AxMoreen on 3/25/2016.
 */
public class PlayerGenerator {
    public int threshold; //amount of players where the maxAmount is added
    public int minAmount;
    public int maxAmount; //max amount of players to add to the server

    public Rolling rolling;

    public PlayerGenerator(int threshold, int maxAmount, int minAmount) {
        this.threshold = threshold;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;

        rolling = new Rolling(5);
    }

    public int getAmountToHave(int currentSize){
        rolling.add(currentSize);
        if (currentSize == 0) return 0;
        int barely = (int)((maxAmount*(rolling.getAverage() / threshold)));
        return Math.min(maxAmount, Math.max(minAmount, barely));
    }


    public class Rolling {

        private int size;
        private double total = 0d;
        private int index = 0;
        private double samples[];

        public Rolling(int size) {
            this.size = size;
            samples = new double[size];
            for (int i = 0; i < size; i++) samples[i] = 0d;
        }

        public void add(double x) {
            total -= samples[index];
            samples[index] = x;
            total += x;
            if (++index == size) index = 0; // cheaper than modulus
        }

        public double getAverage() {
            return total / size;
        }
    }
}
