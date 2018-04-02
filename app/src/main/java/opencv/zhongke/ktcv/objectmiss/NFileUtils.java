package opencv.zhongke.ktcv.objectmiss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LostboyJason on 2018/3/22.
 */

public class NFileUtils {
    public static void writeObjectToFile(File file,Object obj)
    {
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }
    public static Object readObjectFile(File file)
    {
        FileInputStream out;
        try {
            out = new FileInputStream(file);
            ObjectInputStream objOut=new ObjectInputStream(out);
            return objOut.readObject();
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
