package code.akselm.rpo.data;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18AxMoreen on 3/29/2016.
 */
public class FileSaver implements Saver {

    public File dir;

    public FileSaver(File dir) {
        this.dir = dir;
        if (!dir.exists()) dir.mkdirs();
    }

    @Override
    public void save(String name, Map<String, Serializable> store) {
        try{
            File n = new File(dir, name+".yml");
            if (!n.exists()) n.createNewFile();
            YamlConfiguration config = new YamlConfiguration();
            config.load(n);




            for (Map.Entry<String,Serializable> entry : store.entrySet()){
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bout);
                out.writeObject(entry.getValue());
                config.set(entry.getKey(), bout.toString());
                out.close();
                bout.close();
            }

            config.save(n);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Serializable> get(String name) {
        Map<String,Serializable> map = new HashMap<>();
        try{
            File n = new File(dir, name);
            if (!n.exists()) return map;
            YamlConfiguration config = new YamlConfiguration();
            config.load(n);

            for (String key : config.getKeys(false)){
                ByteArrayInputStream bin = new ByteArrayInputStream(config.getString(key).getBytes());
                ObjectInputStream oin = new ObjectInputStream(bin);
                Serializable o = (Serializable)oin.readObject();
                map.put(key, o);
                oin.close();
                bin.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
