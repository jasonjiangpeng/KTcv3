package opencv.zhongke.ktcv.objectmiss;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import java.util.ArrayList;
import java.util.List;

import opencv.zhongke.ktcv.bean.NMeanDev;
import opencv.zhongke.ktcv.demo1.NRect;
import opencv.zhongke.ktcv.ndk.JniEnter;
import opencv.zhongke.ktcv.ndk.Mlog;

import static org.opencv.imgproc.Imgproc.threshold;

public class NTriggle {
    /*长与宽*/
   private static final int size=10;
   private static boolean  isFist=true;
   /*定义方差值*/
   private static final int VARIANCE=6;
    private List<Integer>  objectMiss=new ArrayList<>();
    public NTriggle() {
    }
    /*获取边界区域*/
    public static List<Rect> getBorderArea2(Rect rect){
        Mlog.logShow(rect.toString());
        int  rows=rect.width/size;
        int cols=rect.height/size;
        List<Rect>  rects=new ArrayList<>();
        for (int j = 0; j <cols ; j++) {
            Rect rect1 =new Rect(0,0+size*j,size,size);
            rects.add(rect1);
        }
        for (int i = 1; i <rows-1; i++) {
            Rect rect2 =new Rect(size*i,rect.height-size,size,size);
            rects.add(rect2);
        }
        int  ncols=cols-1;
        for (int m = ncols; m >0 ; m--) {
            Rect rect2 =new Rect(rect.width-size,size*m,size,size);
            rects.add(rect2);
        }
        int  nrows=rows-1;
        for (int n = nrows; n >0 ;n--) {
            Rect rect1 =new Rect(size*n,0,size,size);
            rects.add(rect1);
        }
        return rects;
    }
    public static List<Rect> getBorderArea2(List<Rect> rect){
        Mlog.logShow(rect.toString());
         List<Rect>  rects=new ArrayList<>();



        return rects;
    }
    /*获取图片均值*/
    public static int compareVariance(Mat  mat1){
        MatOfDouble matOfDouble2=new MatOfDouble();
        Core.meanStdDev(mat1,matOfDouble2,new MatOfDouble());
    /*    double[] doubles = matOfDouble2.toArray();
      int sum= (int) ((doubles[0]+doubles[1]+doubles[2])/3);*/
     int  sum= (int) matOfDouble2.toArray()[0];
        return sum;
    }
    /*获取边界方差*/
    public static List<Integer>  getBorderDev(List<Rect> rects, Mat mat){
        List<Integer>  integers=new ArrayList<>();
        int size=rects.size();
        for (int i = 0; i <size; i++) {
            Mat mat1=new Mat(mat,rects.get(i));
            integers.add(compareVariance(mat1));
        }
        return integers;
    }
    public static List<NMeanDev>  getBorderMeanDev(List<Rect> rects, Mat mat){
        List<NMeanDev>  integers=new ArrayList<>();
        int size=rects.size();
        for (int i = 0; i <size; i++) {
            Mat mat1=new Mat(mat,rects.get(i));
            integers.add(getNMeanDev(mat1));
        }
        return integers;
    }
    public static List<Mat>  getBorderMat(List<Rect> rects, Mat mat){
        List<Mat>  integers=new ArrayList<>();
        int size=rects.size();
        for (int i = 0; i <size; i++) {
            Mat mat1=new Mat(mat,rects.get(i));
            integers.add(mat1);
        }
        return integers;
    }
    public static List<Mat>  getBorderMat(List<Rect> rects, Mat mat,int staPoint){
        List<Mat>  integers=new ArrayList<>();

        int size=rects.size();
        int a=rects.get(0).x;
        int b=rects.get(0).y;
        for (int i = staPoint; i <size; i++) {
           Rect  rect =rects.get(i).clone();
            int x=rect.x-a;
            int y=rect.y-b;
            rect.x=x;
            rect.y=y;
            Mat mat1=new Mat(mat,rect);
            integers.add(mat1);
        }
        return integers;
    }
    /*获取新旧值*/
    public static List<Integer>  getTouchArea(List<Mat> old, List<Mat> now){
        List<Integer>  integers=new ArrayList<>();
        int size=old.size();
        int  min=6;
        int  position=-1;
        for (int i = 0; i <size ; i++) {
            Mat  old1=old.get(i);
            Mat  now1=now.get(i);
            int difPoint = JniEnter.getDifPoint(old1.getNativeObjAddr(), now1.getNativeObjAddr());
            if (difPoint>min){
                position=i;
              //  Mlog.logShow(difPoint);

            }
        }
        if (position>0){
            integers.add(position);
        }
       // Mlog.logShow("获取区域大小"+integers.size());
        return integers;
    }
    /*返回图像均值方差*/
    private static NMeanDev getNMeanDev(Mat  mat1){
        MatOfDouble dev=new MatOfDouble();
        MatOfDouble mean=new MatOfDouble();
        Core.meanStdDev(mat1,mean,dev);
        double[]    devs=dev.toArray();
        double[]    means=mean.toArray();
        return new NMeanDev(means,devs);
    }
    /*对比2个方差*/
    public static List<Integer>  compareDev(List<Integer> old, List<Integer> now){
        List<Integer>  integers=new ArrayList<>();
        int size=old.size();
        for (int i = 0; i <size; i++) {
            int result=Math.abs(old.get(i)-now.get(i));
            if (result>VARIANCE){
                integers.add(i);
            }
        }
        return integers;
    }

    public static List<Integer>  compareDev3(List<NMeanDev> old, List<NMeanDev> now){
        List<Integer>  integers=new ArrayList<>();
        int size=old.size();
        for (int i = 0; i <size; i++) {
            if (old.get(i).isSurpass(now.get(i))){
                integers.add(i);
            }
        }
        return integers;
    }
    public static List<Integer>  compareDev(List<Integer> old, List<Integer> now,int limit){
        List<Integer>  integers=new ArrayList<>();
        int size=old.size();
        for (int i = 0; i <size; i++) {
            int result=Math.abs(old.get(i)-now.get(i));
            if (result>limit){
                integers.add(i);
            }
        }
        return integers;
    }

      /*获取中心矩形*/
      public static List<Rect> getCenterArea(Rect rect){
          int  width=(rect.width/size)-1;
          int height=(rect.height/size)-1;
          List<Rect>  rects=new ArrayList<>();
          for (int j = 1; j <height ; j++) {
              for (int i = 1; i <width ; i++) {
                  Rect rect1 =new Rect(size*i,size*j,size,size);
                  rects.add(rect1);
              }
              }

          return rects;
      }
      /*计算干扰点起点*/
      public static int  getStartPoint(List<Integer> integers){
          int  size=integers.size();
          if (size==1){
              return integers.get(0);
          }
          int  a=-1;
          for (int i = 0; i <size ; i++) {
              if (i>0){
             int b=integers.get(i)-integers.get(i-1);
             if (b==1){
                a=integers.get(i);
             }
              }
          }
          return a;
      }
      /*计算矩阵中心店*/
      public static List<Point>  getRectCenterPoint(List<Rect> nRects){
          int size=nRects.size();
          List<Point>  list=new ArrayList<>();
          for (int i = 0; i <size; i++) {
              Point  points =nRects.get(i).getCenterPoint();
              list.add(points);

          }
          return list;
      }
    /*计算平均值*/
    private double claculateSquare(Point  n1,Point n2){
        double  a1=n1.x-n2.x;
        double  b1=n1.y-n2.y;

        return a1*a1+b1*b1;
    }



}
