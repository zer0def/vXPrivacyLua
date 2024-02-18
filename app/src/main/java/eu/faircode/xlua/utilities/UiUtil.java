package eu.faircode.xlua.utilities;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.reflect.Field;

import eu.faircode.xlua.R;

public class UiUtil {
    private static final String TAG = "XLua.UiUtil";
    public static final int CIRCLE_DIAMETER = 64;

    public static void initFloatingActionButtonAnimations(Context context, Animation fabOpen, Animation fabClose, Animation fromBottom, Animation toBottom) {
        try {
            fabOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim_one);
            fabClose = AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim_one);
            fromBottom = AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim_one);
            toBottom = AnimationUtils.loadAnimation(context,R.anim.to_bottom_anim_one);
        }catch (Exception e) {
            Log.e(TAG, "Failed to init Floating Action Buttons... e=" + e);
        }
    }

    public static void setSwipeRefreshLayoutEndOffset(Context context, SwipeRefreshLayout srl, int startOffset) {
        if(srl == null || context == null) {
            Log.e(TAG, "[setSwipeRefreshLayoutEndOffset] Context or SwipeRefreshLayout is null...");
            return;
        }

        int offsetRaw = getSwipeRefreshEndOffset(context);
        if(offsetRaw == -1) {
            try {
                Field mSpinnerOffsetEndField = SwipeRefreshLayout.class.getDeclaredField("mSpinnerOffsetEnd");
                mSpinnerOffsetEndField.setAccessible(true);
                int mSpinnerOffsetEnd = (int) mSpinnerOffsetEndField.get(srl);
                if(mSpinnerOffsetEnd > 0)
                    offsetRaw = mSpinnerOffsetEnd;
                else throw new Exception("Field Error");
            }catch (Exception e) {
                Log.e(TAG, "Failed to use Reflection to get Current SwipeRefreshLayout [mSpinnerOffsetEnd] Field... hardcoding it too (130) " + e);
                offsetRaw = 130;
            }
        }

        srl.setProgressViewOffset(false, startOffset, startOffset + offsetRaw);
    }

    public static int getSwipeRefreshEndOffset(Context context) {
        try {
            final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return (int) (CIRCLE_DIAMETER * metrics.density);
        }catch (Exception e) {
            Log.e(TAG, "Failed to calculate offset for Circle Refresh Swipe thingy: " + e);
            return -1;
        }
    }
}
