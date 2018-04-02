package opencv.zhongke.ktcv.demo1;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import opencv.zhongke.ktcv.R;
import opencv.zhongke.ktcv.bean.NMeanDev;
import opencv.zhongke.ktcv.ndk.JniEnter;
import opencv.zhongke.ktcv.ndk.Mlog;
import opencv.zhongke.ktcv.objectmiss.NCalulate;
import opencv.zhongke.ktcv.objectmiss.NDetection;

import static org.opencv.core.Core.NORM_MINMAX;
import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.core.CvType.CV_32FC3;
import static org.opencv.core.CvType.CV_64FC3;
import static org.opencv.core.CvType.CV_8SC3;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.core.CvType.CV_8UC3;

public class Tutorial1Activity3 extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";
    private CameraBridgeViewBase mOpenCvCameraView;
    private NCountRectView nCountRectView;
    private TextView textView,textView2;
    private  int status=0;
    private ImageView imageView,imageView2;
    private Handler  handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
       /*     long start=System.currentTimeMillis();
            int v = JniEnter.getDifPoint(old.getNativeObjAddr(),newmat.getNativeObjAddr());
            long end=System.currentTimeMillis()-start;
             System.out.println("计算时间"+end);*/
           //  old=newmat.clone();
            if (msg.what==2){
                Bitmap  bitmap=Bitmap.createBitmap(old.width(),old.height(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(old,bitmap);
                imageView.setImageBitmap(bitmap);
                String s = NCalulate.getNMeanDev(old).toString();
//                int difPoint = JniEnter.getDifPoint(old.getNativeObjAddr(), newmat.getNativeObjAddr());
             //   int i = Core.countNonZero(old);
             //   textView2.setText(     NCalulate.getNMeanDev(old).toString());
                textView2.setText(   "数量"+s);
                return;
            }
       //     int  v=JniEnter.nCompareHist(old.getNativeObjAddr(),newmat.getNativeObjAddr());
       /*    String  va=getmeanStdDev(old)+"新图"+getmeanStdDev(newmat);

         //   float v = JniEnter.charCompare(old.getNativeObjAddr(), newmat.getNativeObjAddr());
         //   textView.setText(  v+"值"  );
              textView.setText(status+":::"+va);*/
           // old=newmat.clone();
            Bitmap  bitmap=Bitmap.createBitmap(newmat.width(),newmat.height(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(newmat,bitmap);
          //  int i = Core.countNonZero(newmat);
            //double v = JniEnter.convoSimilar(old.getNativeObjAddr(), newmat.getNativeObjAddr());
            //   textView2.setText(     NCalulate.getNMeanDev(old).toString());
         //   NMeanDev  olds=NCalulate.getNMeanDev(old);

         //   String difPoint = NCalulate.getNMeanDev(newmat).toString();
            String s = NCalulate.getNMeanDev(newmat).toString();
            long difPoint = JniEnter.getDifPoint2(old.getNativeObjAddr(), newmat.getNativeObjAddr());
            Mat  mat=new Mat(difPoint);
            NMeanDev  nows=NCalulate.getNMeanDev(mat);
            mat.release();

            textView.setText(   "相似"+nows.toString()+"现在"+s);

            imageView2.setImageBitmap(bitmap);
            startRun=true;
            status++;
        }
    };

    static {
        System.loadLibrary("native-lib");
    }
    /** Called when the activity is first created. */
NMeanDev  oldMean;
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
    List<Rect> oRect;

  public void BtnstartWork(View view){
      if (oRect!=null){
          oRect.clear();
      }
     List<NRect>    nRects=nCountRectView.getRects();
      oRect=new ArrayList<>(nRects.size());
      for (int i = 0; i <nRects.size() ; i++) {
          oRect.add(nRects.get(i).toRect());
      }
      Mlog.logShow(oRect.get(0).toString());
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

    public void onDestroy() {
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    public void onCameraViewStarted(int width, int height) {

    }
    public void onCameraViewStopped() {

    }



   private int   changeValue=0;
  private Mat  mata;

private boolean  isStartRun=false;

   Mat  old,newmat;
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
   //     System.out.println(JniEnter.getco(22));
        long start=System.currentTimeMillis();
        Mat  mat2 =inputFrame.gray();
       // Imgproc.equalizeHist(mat2,mat2);
         Mat  mat=new Mat();
        int  lpls[] = { -1,-1,-1,-1,8,-1,-1,-1,-1 };
        int  suanzi2[]={-1,0,1,-1,0,1,-1,0,1};
        int  suanzi3[]={1,1,1,0,0,0,-1,-1,-1};
         int  sobel1[]={1,2,1,0,0,0,-1,-2,-1};
        MatOfInt  matOfInt =new MatOfInt(lpls);
         Imgproc.filter2D(mat2,mat,CV_32FC1,matOfInt);
         mat.convertTo(mat,CV_8UC1);
    //     Imgproc.threshold(mat,mat,100,255,NORM_MINMAX);
       //  Core.normalize(mat2,mat2,0,255,NORM_MINMAX,CV_8UC1);

    //   Imgproc.equalizeHist(mat2,mat);

       if (isStartRun){
            if (old==null){
                old=new Mat(mat.clone(),oRect.get(0));

                handler.sendEmptyMessage(2);
            }
           newmat=new Mat(mat,oRect.get(0));
            if (startRun){
                startRun=false;
                handler.sendEmptyMessage(0);
            }
        }

        long end=System.currentTimeMillis()-start;
        if (end<50){
            SystemClock.sleep(50-end);
        }
        return     mat2;
    }


    private  boolean startRun =false;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;
    private void   requestPersision(){
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this,
                  new String[]{Manifest.permission.CAMERA},
                  MY_PERMISSIONS_REQUEST_CALL_CAMERA);
        }else {


      }
  }
  private String  getmeanStdDev(Mat mat){
        MatOfDouble  matOfDouble=new MatOfDouble();
        MatOfDouble  matOfDouble2=new MatOfDouble();
        Core.meanStdDev(mat,matOfDouble,matOfDouble2);
         int  a= (int) matOfDouble.toArray()[0];
         int  b= (int) matOfDouble2.toArray()[0];
        String   str=String.valueOf(a)+"方差"+String.valueOf(b);
        return str;
  }

}
