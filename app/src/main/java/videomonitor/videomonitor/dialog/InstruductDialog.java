package videomonitor.videomonitor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

import videomonitor.videomonitor.R;

/**
 * Created by Administrator on 2017-06-28.
 */

public class InstruductDialog extends Dialog implements View.OnClickListener {
    protected Context mContext;

    public InstruductDialog(Context context) {
        super(context, R.style.THeme_Dialog_NoFrame);
        mContext = context;
        setContentView(R.layout.activity_picture);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    // 屏蔽长按menu键弹出输入法~！11.1
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getRepeatCount() > 0
                && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


}
