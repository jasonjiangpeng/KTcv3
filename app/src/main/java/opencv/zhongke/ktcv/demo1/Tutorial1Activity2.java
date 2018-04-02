package opencv.zhongke.ktcv.demo1;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

import opencv.zhongke.ktcv.R;
import opencv.zhongke.ktcv.ndk.JniEnter;

public class Tutorial1Activity2 extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";
    private CameraBridgeViewBase mOpenCvCameraView;
    private NCountRectView nCountRectView;
    private TextView textView;
    private  int status=0;
    private Handler  handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Mat  compare2=new Mat(newmat,oRect.get(0));

            double v = JniEnter.compareSimilar(compare1.getNativeObjAddr(), compare2.getNativeObjAddr());

            textView.setText(status+":::"+v);
           // old=newmat.clone();
            startRun=true;
            status++;
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
        textView= (TextView) findViewById(R.id.textView);
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
private boolean  isStartRun=false;
  public void BtnstartWork(View view){
      if (oRect!=null){
          oRect.clear();
      }
      List<NRect>    nRects=nCountRectView.getRects();
      oRect=new ArrayList<>(nRects.size());
      for (int i = 0; i <nRects.size() ; i++) {
          oRect.add(nRects.get(i).toRect());
      }
     startRun=true;
      isStartRun=true;
      compare1=new Mat(old,oRect.get(0));
      System.out.println(compare1.rows());
      System.out.println(compare1.cols());


  }
    @Override
    public void onPause()
    {
        super.onPause();
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
         isStartRun=false;
         startRun=false;
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }




    public void onCameraViewStarted(int width, int height) {


    }

    public void onCameraViewStopped() {
    }



   private int   changeValue=0;
  private Mat  mata;

    int sd[] = { -1,-1,-1,-1,8,-1,-1,-1,-1 };
    int   fudiao[] = { -1,-1,0,-1,0,1,0,1,1 };
    int  test[] = { 1,1,-1,0,-1,1,-1,0,0 };
    int sharpen[] = { 0,-1,0,-1,5,-1,0,-1,0 };
    int  bianyuan[] = { -1,0,1,-1,0,1,-1,0,1 };
    int  sobel[] = { -1,0,1,-2,0,2,-1,0,1 };
    int  sobel2[] = { -1,-2,-1,0,0,0,1,2,1 };
    int  prewit[] = { 1,1,1,0,0,0,-1,-1,-1 };
    int  prewit2[] = { -1,0,1,-1,0,1,-1,0,1 };
    int kirsch[] = { 5,5,5,-3,0 - 3,-3,-3,-3 };
    int kirsch1[] = { -3,5,5,-3,0,5,-3,-3,-3 };
    int kirsch2[] = { -3,-3,5,-3,0, 5,-3,-3,5 };
   Mat  old,newmat;
  Mat  compare1;
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
   //     System.out.println(JniEnter.getco(22));
     // long getco = JniEnter.getco(mata.getNativeObjAddr());
        long start=System.currentTimeMillis();
        Mat  mat=inputFrame.gray();
        if (isStartRun){
            long convolution = JniEnter.convolution(mat.getNativeObjAddr(), test);
            if (old==null){
                old=new Mat(convolution).clone();
            }
            newmat=new Mat(convolution);
            if (startRun){
                startRun=false;
                handler.sendEmptyMessage(0);
            }
            long end=System.currentTimeMillis()-start;
        }

       // System.out.println("计算时间"+end);
        return     mat;
    }


    private volatile boolean startRun =false;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;
  private void   requestPersision(){
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this,
                  new String[]{Manifest.permission.CAMERA},
                  MY_PERMISSIONS_REQUEST_CALL_CAMERA);
        }else {

      }

  }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
