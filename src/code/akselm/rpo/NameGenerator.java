package code.akselm.rpo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 18AxMoreen on 3/25/2016.
 */
public class NameGenerator {

    List<String> names = new ArrayList<>();

    public NameGenerator(String resourceName){
        try{
            InputStream in  = getClass().getResourceAsStream(resourceName);
            BufferedReader buff = new BufferedReader(new InputStreamReader(in));
            String line;
            names.clear();
            while ((line = buff.readLine()) != null){
                names.add(line);
            }
            buff.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public NameGenerator(File file){
        try{
            FileInputStream fin = new FileInputStream(file);
            BufferedReader buff = new BufferedReader(new InputStreamReader(fin));
            String line;
            names.clear();
            while ((line = buff.readLine()) != null){
                names.add(line);
            }
            buff.close();
            fin.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String nextName(){
        Collections.shuffle(names);
        return names.get(0);
    }
}
