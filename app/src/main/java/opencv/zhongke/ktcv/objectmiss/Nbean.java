package opencv.zhongke.ktcv.objectmiss;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LostboyJason on 2018/3/22.
 */

public class Nbean implements Parcelable{
    private int  x;
    private  int y;

    public Nbean(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected Nbean(Parcel in) {
        x = in.readInt();
        y = in.readInt();
    }

    public static final Creator<Nbean> CREATOR = new Creator<Nbean>() {
        @Override
        public Nbean createFromParcel(Parcel in) {
            return new Nbean(in);
        }

        @Override
        public Nbean[] newArray(int size) {
            return new Nbean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
    }
}
