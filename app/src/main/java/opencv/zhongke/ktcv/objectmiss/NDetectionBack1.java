package opencv.zhongke.ktcv.objectmiss;

import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

import opencv.zhongke.ktcv.bean.NTrigleEvent;
import opencv.zhongke.ktcv.ndk.Mlog;
import opencv.zhongke.ktcv.utils.NString;

import static org.opencv.core.CvType.CV_8UC1;


/**
 * Created by LostboyJason on 2018/3/28.
 */

public class NDetectionBack1 extends Thread {
    private List<Mat>  originMat;
    private  boolean  isRun=false;
    private CallBack callBack;
    private Handler handler;
    private NStatus  nStatus;
    private NDetectionTri nDetectionTri;
    private List<Integer>  objectMiss=new ArrayList<>();
    public void onDestory(){
        isRun=false;
        objectMiss.clear();
        nDetectionTri.destroy();
        originMat.clear();
    }
    public NDetectionBack1(List<Mat> originMat, CallBack callBack , Handler handler) {
        this.originMat = originMat;
        this.callBack = callBack;
        this.handler = handler;
        nStatus=NStatus.IDEL;
        nDetectionTri=callBack.getNdetecionTri();
    }
    private List<Point>  getCenterPoint(List<Rect> rects){
        List<Point>   list=new ArrayList<>();
        int size=rects.size();
        for (int i = 0; i <size ; i++) {
            Rect  rect =rects.get(i);
            int x=rect.x+rect.width/2;
            int y=rect.y+rect.height/2;
            list.add(new Point(x,y));
        }
        return list;
    }
    private NTrigleEvent response(List<Rect> rects, List<Integer> border, List<Integer> trig){
        List<Point>  list=getCenterPoint(rects);
        List<Point>  tiregarea=nDetectionTri.getCenterPoint();

        return response(list,tiregarea,border,trig);
    }
    private NTrigleEvent response(List<Point> pbord, List<Point> tirgarea, List<Integer> border, List<Integer> trigint){
        if (tirgarea.size()<0){
            return null;
        }
        int   max=0;
        int  bopo=-1;
        int  trigl=-1;
        int  size= border.size();
        int  tri= trigint.size();
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <tri ; j++) {
                int  boPosition=border.get(i);
                int position = trigint.get(j);
                if (objectMiss.contains(position)){
                    continue;
                }
                int  cc=calFather(pbord.get(boPosition),tirgarea.get(position));
                if (cc>max){
                    max=cc;
                    bopo=i;
                    trigl=position;
                }
            }
        }
        return  new NTrigleEvent(max,trigl,bopo);
    }
    private int calFather(Point p1,Point p2){
        int  a=p1.x-p2.x;
        int  b=p2.y-p2.y;
        return a*a+b*b;
    }

    private synchronized NStatus getStatus(){
        return nStatus;
    }
    @Override
    public void run() {
        List<Rect> rects=null;
        Mat  nowMat=null;
        int  number=0;
        NTrigleEvent nTrigleValue=null;
        List<Integer>  touchAreas=new ArrayList<>();
        while (isRun){
            long  start=System.currentTimeMillis();
            if (rects==null){
                rects=callBack.getRect();
            }
            nowMat=callBack.getNowMat();
            if (nowMat.type()!=CV_8UC1)continue;
            List<Mat> getNowBorderMat = NTriggle.getBorderMat(rects, nowMat);
            List<Integer> touchArea = NTriggle.getTouchArea(originMat, getNowBorderMat);
            if (touchArea.size() > 0) {
                number=2;
                  if (nStatus==NStatus.IDEL){
                    if (nTrigleValue==null){
                        nTrigleValue=new NTrigleEvent();
                    }
                    touchAreas.addAll(touchArea);
                    nStatus=NStatus.ACTIVE;
                }
                List<Integer> integers = nDetectionTri.judgeOjectStutas(nowMat, nStatus);
                NTrigleEvent nTriglenow  = response(rects, touchAreas, integers);
                nTrigleValue.setNTrigleValue(nTriglenow);
                if (nTrigleValue.getNumber()>4){
                    nTrigleValue.setRx(true);
                  handler.obtainMessage(1, nTrigleValue.getTriglePosition()+1 ).sendToTarget();
                }
            }else {
                if (nStatus==NStatus.ACTIVE){
                    number--;
                    if (number<0){
                        touchAreas.clear();
                        if (!nTrigleValue.isRx()&&nTrigleValue.getMaxValue()>1){
                            handler.obtainMessage(1, nTrigleValue.getTriglePosition()+1 ).sendToTarget();
                        }
                        nTrigleValue=null;
                        nStatus=NStatus.IDEL;
                       List<Integer> integers = nDetectionTri.judgeOjectStutas(nowMat, nStatus);
                        List<Integer> list = NObjectMiss.detectionObejctMiss(integers);
                        objectMiss.clear();
                        objectMiss.addAll(list);
                        nDetectionTri.setOldMat(nowMat);
                        //   handler.obtainMessage(1, "没有物体进入").sendToTarget();
                        handler.obtainMessage(0, NString.listToStringObjectMiss(integers)).sendToTarget();
                    }
                }
            }


            long  end=System.currentTimeMillis()-start;
            if (end>80){
                Mlog.logShow(end+"TIME");
            }
            if (end<120){
                SystemClock.sleep(120-end);
            }


        }
    }

    @Override
    public synchronized void start() {
        isRun =true;
        super.start();
    }
    public    interface CallBack{

        List<Rect> getRect();
        Mat getNowMat();

        NDetectionTri getNdetecionTri();
    }
}
