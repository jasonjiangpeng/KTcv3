package opencv.zhongke.ktcv.demo1;

import android.os.Parcel;
import android.os.Parcelable;

import org.opencv.core.Rect;

/**
 * Created by Administrator on 2018/1/12.
 */

public class NRect implements Parcelable {
    private   int left;
    private   int right;
    private   int top;
    private   int bottom;

    public String tostring(){
        return left+"="+top+"="+right+"="+bottom;
    }
    public static NRect   stringCreate(String v){
        String[]  sd=v.split("=");

     int   left= Integer.valueOf(sd[0]);
       int  top= Integer.valueOf(sd[1]);
        int right= Integer.valueOf(sd[2]);
       int  bottom= Integer.valueOf(sd[3]);
        return new NRect(left,top,right,bottom);
    }
    public NRect(int left, int top, int right, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }
   public int  getWeight(){
        return right-left;
   }
    public int  getheight(){
        return bottom-top;
    }
    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    protected NRect() {
    }
   public Rect toRect(){
        return new Rect(left,top,getWeight(),getheight());


   }
    public static final Creator<NRect> CREATOR = new Creator<NRect>() {
        @Override
        public NRect createFromParcel(Parcel in) {
            NRect  nRect =new NRect();
            nRect.readFromParcel(in);
            return new NRect();
        }

        @Override
        public NRect[] newArray(int size) {
            return new NRect[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    public void readFromParcel(Parcel in) {
       this.left = in.readInt();
        this.top = in.readInt();
        this.right = in.readInt();
        this.bottom = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(left);
        dest.writeInt(top);
        dest.writeInt(right);
        dest.writeInt(bottom);

    }

}
