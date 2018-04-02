package opencv.zhongke.ktcv.ndk;

import org.opencv.core.Mat;

/**
 * Created by LostboyJason on 2018/3/20.
 */

public class JniEnter {
    public static void init(){
        System.loadLibrary("native-lib");
    }
    /*计算2副图像相似度*/
    public static native double compareSimilar(long v1,long v2);
     /*对图像进行卷积核转换*/
    public static native long   convolution(long v1,int[] v);
    /*获取图像变化值*/
 //  public static native long
    /*对比2张图差异点个数*/
   public static native int getDifPoint(long v1,long v2);
    /*对比2张图差异点个数*/
    public static native long getDifPoint2(long v1,long v2 );
   /*直方图对比*/
   public static native int  nCompareHist(long v1,long v2);
   /*均衡化图片*/
   public static native void equalizeHist(long v1);
   /*特征对比*/
   public static native float  charCompare(long v1,long v2);
   /*拉普拉斯增强*/
   public static native long  lplsChange(long v1);
   /*卷积相似度对比*/
   public static native double convoSimilar(long v1,long v2);
}
