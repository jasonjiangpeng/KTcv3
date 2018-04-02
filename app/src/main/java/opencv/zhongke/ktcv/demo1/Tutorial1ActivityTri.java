package opencv.zhongke.ktcv.demo1;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import opencv.zhongke.ktcv.R;
import opencv.zhongke.ktcv.bean.NMeanDev;
import opencv.zhongke.ktcv.ndk.JniEnter;
import opencv.zhongke.ktcv.ndk.Mlog;
import opencv.zhongke.ktcv.objectmiss.NDetection;
import opencv.zhongke.ktcv.objectmiss.NDetectionTri;
import opencv.zhongke.ktcv.objectmiss.NTriggle;
import opencv.zhongke.ktcv.utils.NString;

import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.core.CvType.CV_8UC1;


public class Tutorial1ActivityTri extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";
    private CameraBridgeViewBase mOpenCvCameraView;
    private NCountRectView nCountRectView;
    private TextView textView,textView2;
    private ImageView imageView,imageView2;
   private List<Rect> oRect;
   private Set<Integer> allNumber=new HashSet<>();
    private boolean  isStartRun=false;
    private  Mat  old,newmat;
    private List<Rect> borderArea;
   // private List<Integer>  getBorderInt,getCenterInt;
    private List<Mat>  getBorderMat,originMat;
    private volatile boolean startRun =false;
    private  boolean workRun =true;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;
    public void onDestroy() {
        nDetection.onDestory();
        super.onDestroy();
        workRun=false;
        startRun=false;
        isStartRun=false;
        if (mOpenCvCameraView != null) mOpenCvCameraView.disableView();
        allNumber.clear();
        borderArea.clear();

        getBorderMat.clear();

    }
    private Handler  handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
               case 2:
                   Bitmap  bitmap=Bitmap.createBitmap(old.width(),old.height(), Bitmap.Config.RGB_565);
                   Utils.matToBitmap(old,bitmap);
                   imageView.setImageBitmap(bitmap);
                 //  textView2.setText("计算时间"+msg.obj);
                   break;
               case 1:
                   textView.setText("物品响应--"+msg.obj);
                   break;
               case 0:
                   textView2.setText("物品状态---"+msg.obj);
                   break;
                case 5:
                    break;
                case 11:
                    textView2.setText(msg.what);
                    break;
           }
        }
    };

    static {
        System.loadLibrary("native-lib");
    }
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestPersision();
        setContentView(R.layout.mainnewact);
        nCountRectView= (NCountRectView) findViewById(R.id.nrect);
        imageView=  findViewById(R.id.imageView);
        imageView2=  findViewById(R.id.imageView2);
        textView= (TextView) findViewById(R.id.textView);
        textView2= (TextView) findViewById(R.id.textView2);
        SetParameter   setParameter= (SetParameter) findViewById(R.id.setvalueview);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_ANY);
        mOpenCvCameraView.setCvCameraViewListener(this);
        setParameter.setChangeView(new ChangeView() {
            @Override
            public void styleChange(int value) {
                nCountRectView.setStyle(value);
            }

            @Override
            public void focusChange(int value) {
           nCountRectView.setFocusPosition(value);
            }

            @Override
            public void rcChange(int counts) {
              nCountRectView.setrc(counts);
            }
        });
    }

  public void BtnstartWork(View view){
      if (oRect!=null){
          oRect.clear();
      }
     List<NRect>    nRects=nCountRectView.getRects();
      oRect=new ArrayList<>(nRects.size());
      for (int i = 0; i <nRects.size() ; i++) {
          oRect.add(nRects.get(i).toRect());
      }
      borderArea = NTriggle.getBorderArea2(oRect.get(0));

      System.out.println("=-----------"+borderArea.size());
      Toast.makeText(this,"计算完成",Toast.LENGTH_SHORT).show();
      isStartRun=true;
     startRun=true;
  }
    @Override
    public void onPause()
    {   super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    @Override
    public void onResume()
    {if (OpenCVLoader.initDebug(false)){
        mOpenCvCameraView.enableView();
    }
        super.onResume();

    }


    public void onCameraViewStarted(int width, int height) {

    }
    public void onCameraViewStopped() {

    }

    public  NMeanDev compareVariance(Mat  mat1){
        MatOfDouble dev=new MatOfDouble();
        MatOfDouble mean=new MatOfDouble();
        Core.meanStdDev(mat1,mean,dev);
        double[]    devs=dev.toArray();
        double[]    means=mean.toArray();
        return new NMeanDev(means,devs);
    }
    private NDetection   nDetection;
    int  weili[] = { -1,-1,-1,-1,8,-1,-1,-1,-1 };
   volatile Mat  mat=null;
   public Mat   getmat(Mat mat){

       Mat  mat1=new Mat(new Size(mat.width(),mat.height()),CV_8UC1);
       MatOfInt matOfInt =new MatOfInt(weili);
       Imgproc.filter2D(mat,mat1,CV_8UC1,matOfInt);
       return mat1;
   }
NDetectionTri  nDetectionTri;
    public Mat onCameraFrame(final CvCameraViewFrame inputFrame) {
        if (isStartRun) {
            if (old == null) {
                Mat mat1=inputFrame.gray();
                old = new Mat(getmat(mat1), oRect.get(0));
                getBorderMat = NTriggle.getBorderMat(borderArea, old);
                nDetectionTri =new NDetectionTri(old,oRect);
                mat1.release();
            }
            if (nDetection == null) {
                    nDetection = new NDetection(getBorderMat,  new NDetection.CallBack() {
                        @Override
                        public List<Rect> getRect() {
                            return borderArea;
                        }
                        @Override
                        public Mat getNowMat() {
                            Mat mat1=inputFrame.gray();
                            Mat  mat11=new Mat(getmat(mat1),oRect.get(0));
                            mat1.release();
                      //      Runtime.getRuntime().gc();
                            return mat11;
                        }
                        @Override
                        public NDetectionTri getNdetecionTri() {
                            return nDetectionTri;
                        }
                    }, handler);
                    nDetection.start();
                }



        }

        /*    if (startRun){
                startRun=false;
                new Thread(){
                    @Override
                    public void run() {
                        int number = -1;
                        int startPoint = -1;
                        while (workRun) {
                            List<Mat> getNowBorderMat = NTriggle.getBorderMat(borderArea, newmat);
                            List<Integer> touchArea = NTriggle.getTouchArea(getBorderMat, getNowBorderMat);
                            if (touchArea.size() > 0) {
                                handler.obtainMessage(1, NString.listToString(touchArea)).sendToTarget();
                            }else {
                                handler.obtainMessage(0, NString.listToString(touchArea)).sendToTarget();
                            }

                        }
                    }
                }.start();*/


        return    inputFrame.gray();
    }

    private void   requestPersision(){
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this,
                  new String[]{Manifest.permission.CAMERA},
                  MY_PERMISSIONS_REQUEST_CALL_CAMERA);
        }else {

      }
  }


}
