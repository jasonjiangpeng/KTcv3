package opencv.zhongke.ktcv.demo1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/1/11.
 */

public class NCountRectView extends View {
    private  int weight;
    private  int height;
    private Paint paintRed;
    private Paint paintBlue;
    private int dlt;
    private   int cols=0;
    private   int rows=0;
    private   int defaultSize=50;
    private  int  style=0;
    private final  int STYLENORMAL=0;
    private final  int STYLEROW=1;
    private final  int STYLECOL=2;
    private Paint fontPaint;

    public int getFocusPosition() {
        return focusPosition;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public void setFocusPosition(int focusPosition) {
        this.focusPosition = focusPosition;
        invalidate();
    }

    private int focusPosition =-1;

    public List<NRect> getRects() {
        return rects;
    }

    public void setRects(List<NRect> rects) {
        this.rects = rects;
    }

    private List<NRect> rects=null;
    public NCountRectView(Context context) {
        this(context,null);

    }

    public NCountRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        paintRed=getPaint2();
        fontPaint=getFontPaint();
        paintBlue=getPaint();
    }
      public List<NRect> genRect(int c){
        List<NRect> ls=new ArrayList<>(c);
          for (int j = 0; j <c ; j++) {
                ls.add(new NRect(100,100,200,200));
          }
        return ls;
    }
    public  void setrc(int c){
        focusPosition =-1;
        if (rects!=null){
            rects.clear();
        }
        rects=genRect(c);
        invalidate();
    }

    private Paint getPaint(){
        Paint paint =new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
        paint.setAntiAlias(true);
        return  paint;
    }
    private Paint getFontPaint(){
        Paint paint =new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30f);
        paint.setAntiAlias(true);
        return  paint;
    }
    private Paint getPaint2(){
        Paint paint =new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
        paint.setAntiAlias(true);
        return  paint;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if (rects!=null){
            for (int i = 0; i <rects.size() ; i++) {
                if (focusPosition ==i){
                    canvas.drawRect(rects.get(i).getLeft(),rects.get(i).getTop(),rects.get(i).getRight(),rects.get(i).getBottom(),paintRed);
                }else {
                    canvas.drawRect(rects.get(i).getLeft(),rects.get(i).getTop(),rects.get(i).getRight(),rects.get(i).getBottom(),paintBlue);
                }
               int  x=rects.get(i).getLeft()+ rects.get(i).getWeight()/2-20;
               int  y=rects.get(i).getTop()+rects.get(i).getheight()/2-20;
               canvas.drawText(i+"",x,y,fontPaint);
            }
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
           if (event.getAction()== MotionEvent.ACTION_DOWN&&focusPosition>=0){
               if (focusPosition>=rects.size()){
                   return false;
               }
               NRect  nRect =rects.get(focusPosition);
               int w=nRect.getWeight();
               int h=nRect.getheight();
               nRect.setLeft((int) event.getX());
               nRect.setTop((int) event.getY());
               nRect.setRight((int) event.getX()+w);
               nRect.setBottom((int) event.getY()+h);
               rects.set(focusPosition,nRect);
           }
           invalidate();
        return super.onTouchEvent(event);
    }





    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_SCROLL:
                    if (focusPosition<0){
                        return false;
                    }
                    //获得垂直坐标上的滚动方向,也就是滚轮向下滚
                    if( event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f){
                      actionMoveasub();
                    } else{
                      actionMoveadd();
                    }
                    stylechange(style);

                    return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    private void actionMoveasub() {
        dlt=-5;
    }

    private void actionMoveadd() {
        dlt=5;
    }
    private  void stylechange(int  value){
        NRect  nRect =rects.get(focusPosition);
        int  l=nRect.getLeft();
        int  t=nRect.getTop();
        int  r=nRect.getRight();
        int  b=nRect.getBottom();
       switch (value){
           case 0:
               nRect.setLeft(l+dlt);
               nRect.setRight(r-dlt);
               nRect.setTop(t+dlt);
               nRect.setBottom(b-dlt);
               break;
           case 1:
               nRect.setLeft(l+dlt);
               nRect.setRight(r-dlt);
               break;
           case 2:
               nRect.setTop(t+dlt);
               nRect.setBottom(b-dlt);
               break;

       }
        rects.set(focusPosition,nRect);
       invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        weight=getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        height=getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        setMeasuredDimension(weight,height);

    }


}
