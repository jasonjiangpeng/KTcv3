package opencv.zhongke.ktcv.bean;

import java.util.List;

/**
 * Created by LostboyJason on 2018/3/29.
 */

public class NTrigleEvent {
    private int maxValue;
    private int  triglePosition;
    private int  borderPosition;
    private int  number=0;
    private boolean  isChang=false;
    private boolean  isRx=false;
    public NTrigleEvent(int maxValue, int triglePosition, int borderPosition) {
        this.maxValue = maxValue;
        this.triglePosition = triglePosition;
        this.borderPosition = borderPosition;
    }

    public boolean isChang() {
        return isChang;
    }

    public void setChang(boolean chang) {
        isChang = chang;
    }

    public boolean isRx() {
        return isRx;
    }

    public void setRx(boolean rx) {
        isRx = rx;
    }

    public void setNTrigleValue(NTrigleEvent nTrigleValue){
        if (nTrigleValue.maxValue>maxValue){
           this.maxValue=nTrigleValue.maxValue;
           this.triglePosition=nTrigleValue.triglePosition;
           this.borderPosition=nTrigleValue.borderPosition;
        //    isChang=true;
            number=0;
        }else {
          //  isChang=false;
            number++;
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public NTrigleEvent() {
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getTriglePosition() {
        return triglePosition;
    }

    public void setTriglePosition(int triglePosition) {
        this.triglePosition = triglePosition;
    }

    public int getBorderPosition() {
        return borderPosition;
    }

    public void setBorderPosition(int borderPosition) {
        this.borderPosition = borderPosition;
    }
}
