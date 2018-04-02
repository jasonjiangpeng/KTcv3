package opencv.zhongke.ktcv.ndk;

/**
 * Created by Administrator on 2017/8/10.
 */

public class Mlog {
    private static String MYTAG="Mlog=============:";
    public static void logShow(String tag,Object o) {
        if (o == null) {
            System.out.println(tag + ":" + null + "对象");
        } else {
            System.out.println(tag + ":" +o.toString());
        }
    }
    public static void logShow(Object o){
        if (o==null){
            return;
        }
            System.out.println(MYTAG+o.toString());
    }
}
