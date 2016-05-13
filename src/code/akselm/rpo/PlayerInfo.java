package code.akselm.rpo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18AxMoreen on 3/29/2016.
 */
public class PlayerInfo {
    public Map<String,Serializable> store = new HashMap<>();
    public String name;
    public PlayerInfo(String name, Map<String, Serializable> store) {
        this.store = store;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Serializable get(String key){
        return store.get(key);
    }

    public void set(String key, Serializable object){
        store.put(key, object);
    }
    
    public boolean containsKey(String key){
        return store.containsKey(key);
    }

}
