import com.google.common.io.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18AxMoreen on 3/25/2016.
 */
public class ConvertFile {
    public static void main(String[] args) throws Exception{
        File file = new File("mc alts.txt");
        List<String> lines = Files.readLines(file, Charset.defaultCharset());
        List<String> out = new ArrayList<>();
        for (String line : lines){
            String[] p = line.split(":");
            out.add(p[0].replace(" ", ""));
        }

        File f = new File("names.txt");
        if (!f.exists()) f.createNewFile();
        FileOutputStream fout = new FileOutputStream(f);
        PrintWriter pw = new PrintWriter(fout);
        for (String o : out){
            pw.println(o);
        }
        pw.close();
        fout.close();
    }
}
