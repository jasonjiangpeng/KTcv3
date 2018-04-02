package opencv.zhongke.ktcv;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by LostboyJason on 2018/3/30.
 */

public class AccessibilitySampleService extends AccessibilityService {
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
        // 获取包名
        String pkgName = event.getPackageName().toString();
        int eventType = event.getEventType();
        // AccessibilityOperator封装了辅助功能的界面查找与模拟点击事件等操作
     //   AccessibilityOperator.getInstance().updateEvent(this, event);

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }
}
