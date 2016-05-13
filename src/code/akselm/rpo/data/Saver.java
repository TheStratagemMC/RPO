package code.akselm.rpo.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 18AxMoreen on 3/29/2016.
 */
public interface Saver {
    public void save(String name, Map<String,Serializable> store);
    public Map<String,Serializable> get(String name);
}
