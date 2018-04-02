package opencv.zhongke.ktcv.objectmiss;

import android.graphics.Point;

import org.opencv.core.Mat;

import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

import opencv.zhongke.ktcv.bean.NMeanDev;
import opencv.zhongke.ktcv.ndk.JniEnter;
import opencv.zhongke.ktcv.ndk.Mlog;

/**
 * Created by LostboyJason on 2018/3/28.
 */

public class NDetectionTri  {
    private List<Mat> oldMat;
    private List<Rect> rects=new ArrayList<>();
    public List<Point>  getCenterPoint(){
        List<Point>   list=new ArrayList<>();
        int size=rects.size();
        int a=rects.get(0).x;
        int b=rects.get(0).y;
        for (int i = 1; i <size ; i++) {
            Rect  rect =rects.get(i);
            int x=rect.x+rect.width/2-a;
            int y=rect.y+rect.height/2-b;
            list.add(new Point(x,y));
        }
        return list;
    }
    public NDetectionTri(Mat  old, List<Rect> list ) {
        this.rects=list;
        Mlog.logShow(old.width()+"H"+old.height());
        List<Rect> k=new ArrayList<>();
        k.addAll(rects);
        oldMat =NTriggle.getBorderMat(rects,old,1);

    }

    public void setOldMat(Mat nowmat) {

        this.oldMat  =NTriggle.getBorderMat(rects,nowmat,1);;
    }

    public List<Rect> getRects() {
        return rects;
    }

    public void setRects(List<Rect> rects) {
        this.rects = rects;
    }

    public List<Mat> getOldMat() {
        return oldMat;
    }
    public List<NMeanDev>  getOldListNMeanDev(){
        List<NMeanDev>  nMeanDevs=new ArrayList<>();
        int  size= oldMat.size();
        for (int i = 0; i <size ; i++) {
            NMeanDev nMeanDev = NCalulate.getNMeanDev(oldMat.get(i));
            nMeanDevs.add(nMeanDev);
        }
        return nMeanDevs;
    }
    public List<NMeanDev>  getNowNmeanDev(Mat mat){
        List<Mat>  nowMat=NTriggle.getBorderMat(rects,mat,1);
        int  size=nowMat.size();
        List<NMeanDev>   nMeanDevs =new ArrayList<>();
        for (int i = 0; i <size ; i++) {
            NMeanDev nMeanDev = NCalulate.getNMeanDev(nowMat.get(i));
            nMeanDevs.add(nMeanDev);
        }
        return nMeanDevs;
    }
    public List<NMeanDev>  getdifNmeanDev(Mat mat){
         List<Mat>  nowMat=NTriggle.getBorderMat(rects,mat,1);
        int  size=nowMat.size();
        List<NMeanDev>   integers =new ArrayList<>();
        for (int i = 0; i <size ; i++) {
            NMeanDev matMeanDev = getMatMeanDev(oldMat.get(i), nowMat.get(i));
            integers.add(matMeanDev);
        }
        return integers;
    }
    public List<Integer>  compareListNmeanDev(List<NMeanDev>  oldnMeanDevs,List<NMeanDev> nownMeanDevs){
        int  size=oldnMeanDevs.size();
        List<Integer>   integers =new ArrayList<>();
        for (int i = 0; i <oldnMeanDevs.size() ; i++) {
            integers.add(size);
        }
        return integers;
    }
    public  List<Integer> judgeOjectStutas(Mat mat,NStatus nStatus){
        List<NMeanDev> oldMean=getOldListNMeanDev();
        List<NMeanDev> nowMean=getNowNmeanDev(mat);
        List<NMeanDev> difMean=getdifNmeanDev(mat);
        return judgeOjectStutas(oldMean,nowMean,difMean,nStatus);
    }

    /*物品状态*/
   public  List<Integer> judgeOjectStutas(List<NMeanDev> oldMean,List<NMeanDev> nowMean,List<NMeanDev> difMean,NStatus  nStatus){
       int  size=oldMean.size();
       List<Integer>  integers =new ArrayList<>();
        if (nStatus==NStatus.IDEL){
            for (int i = 0; i <size ; i++) {
                int  result=-1;
                if (oldMean.get(i).equelsNMeanDev(difMean.get(i))&&nowMean.get(i).nearZeao()){
                    result=NConstants.MOBJECTMISS;
                }else {
                    result=NConstants.MOBJECERROR;
                }
                integers.add(result);
            }
        }else {
            for (int i = 0; i <size ; i++) {
             //  int  result =-1;
               if (difMean.get(i).surpassValue()&&!(oldMean.get(i).equelsNMeanDev(nowMean.get(i)))){
            //       result=NConstants.MOBJECTACTION;
                 //  Mlog.logShow("---------------------触发区域------"+i);
                   integers.add(i);
               }

            }
        }
       return integers;
   }
   public List<Integer> trigleArea(List<Integer> list){
       List<Integer>  list1=new ArrayList<>();
       for (int i = 0; i <list.size() ; i++) {
           if (list.get(i)==2){
               list1.add(i);
           }
       }
       return list1;
   }
    public NMeanDev  getMatMeanDev(Mat mat1, Mat mat2){
        long difPoint = JniEnter.getDifPoint2(mat1.getNativeObjAddr(), mat2.getNativeObjAddr());
        Mat mat=new Mat(difPoint);
        NMeanDev  nows=NCalulate.getNMeanDev(mat);
        return nows;
    }
   public void destroy(){
        if (oldMat!=null){
            oldMat.clear();
        }
        if (rects!=null){
            rects.clear();
        }
   }

}
