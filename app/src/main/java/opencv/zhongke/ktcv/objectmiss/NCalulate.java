package opencv.zhongke.ktcv.objectmiss;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

import opencv.zhongke.ktcv.bean.NMeanDev;

/**
 * Created by LostboyJason on 2018/3/23.
 */

public class NCalulate {

    /*返回图像均值方差*/
    public static NMeanDev getNMeanDev(Mat  mat1){
        MatOfDouble dev=new MatOfDouble();
        MatOfDouble mean=new MatOfDouble();
        Core.meanStdDev(mat1,mean,dev);

        double[]    devs=dev.toArray();
        double[]    means=mean.toArray();
        return new NMeanDev(means,devs);
    }

}
