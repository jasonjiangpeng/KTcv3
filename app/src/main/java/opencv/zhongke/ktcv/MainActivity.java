package opencv.zhongke.ktcv;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.opencv.core.Mat;

import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        img=findViewById(R.id.img);
      //  Animation animation = AnimationUtils.loadAnimation(this, R.anim.in);
      //   img.startAnimation(animation);

      //  tv.setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
   new Thread(){
       @Override
       public void run() {
           int  a=100;
           int b=100;
           while (true){
               img.setX(a);
               img.setY(b);
               a+=10;
               b+=20;
               if (a>1000){
                   a=0;
               }
               if (b>1000){
                   b=0;
               }
               SystemClock.sleep(200);
           }
       }
   }.start();
    }

    public String[] getMethed(){
        String   packagename="com.qyqp.hn.wxapi.WXEntryActivity";
       try {
           Class<?> aClass = Class.forName(packagename);
           Method[] methods = aClass.getMethods();
           for (int i = 0; i <methods.length ; i++) {
               System.out.println(methods[i].toString());
           }
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }
       return null;
   }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

}
