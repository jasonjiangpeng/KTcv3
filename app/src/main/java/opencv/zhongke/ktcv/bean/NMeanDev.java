package opencv.zhongke.ktcv.bean;

import java.math.BigDecimal;

/**
 * Created by LostboyJason on 2018/3/26.
 */

public class NMeanDev {
    private final  double SURPASSVALUE=9;
    private double[]  mean;
    private double[]  dev;

    public NMeanDev(double[] mean, double[] dev) {
        this.mean = mean;
        this.dev = dev;
    }

    public double[] getMean() {
        return mean;
    }

    public void setMean(double[] mean) {
        this.mean = mean;
    }

    public double[] getDev() {
        return dev;
    }

    public void setDev(double[] dev) {
        this.dev = dev;
    }
    private double get2double(double sd) {
        BigDecimal bg = new BigDecimal(sd);
        double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
    private   boolean isSurpass(){
        double  d=mean[0]*mean[0]+dev[0]*dev[0];
        if (d>SURPASSVALUE){
            return true;
        }
        return false;
    }
    public  boolean isSurpass(NMeanDev nMeanDev){
        double   c= Math.abs( this.mean[0]-nMeanDev.mean[0]);
        double   d= Math.abs( this.dev[0]-nMeanDev.dev[0]);
        double  e=c*c+d*d;
        if (e>SURPASSVALUE){
            return true;
        }
        return false;
    }
    public  boolean equelsNMeanDev(NMeanDev nMeanDev){
        double   c= Math.abs( this.mean[0]-nMeanDev.mean[0]);
        double   d= Math.abs( this.dev[0]-nMeanDev.dev[0]);
        if (c<2&&d<2){
            return true;
        }

        return false;
    }
    public  boolean nearZeao(){
          if (mean[0]<10&&dev[0]<20){
              return true;
          }
        return false;
    }
    public  boolean nearQuiet(){
        if (mean[0]>15&&dev[0]>25){
            return true;
        }
        return false;
    }
    public  boolean surpassValue(){
        if (dev[0]>24){
            return true;
        }
     //   if (mean[0]>25&&dev[0]>25){
        if (mean[0]>18&&dev[0]>20){
            return true;
        }
        return false;
    }
    public String  toString(){
        StringBuilder  stringBuilder=new StringBuilder();
        stringBuilder.append("均值=");
        for (int i = 0; i <mean.length ; i++) {
            stringBuilder.append(get2double(mean[i])+":");
        }
        stringBuilder.append("方差=");
        for (int i = 0; i <dev.length ; i++) {
            stringBuilder.append(get2double(dev[i])+":");
        }
        return stringBuilder.toString();
    }
}
