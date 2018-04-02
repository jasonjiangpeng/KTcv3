package opencv.zhongke.ktcv.objectmiss;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

import opencv.zhongke.ktcv.demo1.NRect;

/**
 * Created by LostboyJason on 2018/3/22.
 */

public class NObjectMiss extends Thread{
    public NObjectMiss() {

    }
    /*获取图片均值*/
    public int compareVariance(Mat  mat1){
        MatOfDouble  matOfDouble2=new MatOfDouble();
        Core.meanStdDev(mat1,new MatOfDouble(),matOfDouble2);
        return (int) matOfDouble2.toArray()[0];
    }
    public static List<Integer>  detectionObejctMiss(List<Integer> integers){
        List<Integer>  integers1=new ArrayList<>();
        for (int i = 0; i <integers.size() ; i++) {
            int  c=integers.get(i);
            if (c== NConstants.MOBJECTMISS)
                integers1.add(c);
        }
        return integers1;
    }
}
