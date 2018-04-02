package opencv.zhongke.ktcv.demo1;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import opencv.zhongke.ktcv.R;


/**
 * Created by Administrator on 2018/1/4.
 */

public class SetParameter extends LinearLayout {


    private ChangeView changeView;
    private AmountView  amountView,amount_view2,amount_view3;
    public ChangeView getChangeView() {
        return changeView;
    }

    public void setChangeView(ChangeView changeView) {
        this.changeView = changeView;
    }

    public SetParameter(Context context) {
        super(context);
    }
    public SetParameter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View inflate = LayoutInflater.from(context).inflate(R.layout.sub_setparmeter2, this);
        initUi(inflate);
    }
    public void initUi(View inflate) {
        amountView= (AmountView) inflate.findViewById(R.id.amount_view);
        amount_view2= (AmountView) inflate.findViewById(R.id.amount_view2);
        amount_view3= (AmountView) inflate.findViewById(R.id.amount_view3);
        amountView.setGoods_storage(2);
        amount_view2.setGoods_storage(30);
        amount_view3.setGoods_storage(amount_view2.getAmount());
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
          @Override
          public void onAmountChange(View view, int amount) {
              if (changeView!=null){
                  changeView.styleChange(amount);
              }

          }
       });
        amount_view2.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {

                amount_view3.setGoods_storage(amount-1);
                amount_view3.setAmount(0);
                if (changeView!=null){
                    changeView.rcChange(amount);
                }

            }
        });
        amount_view3.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (changeView!=null){
                    changeView.focusChange(amount);
                }

            }
        });

    }
   public void init(){
       amountView.init(0);
       amount_view2.init(1);
       amount_view3.init(0);
   }

}
