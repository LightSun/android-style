package org.heaven7.scrap.core.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.heaven7.scrap.core.oneac.BaseScrapView;

public class TransitionAnimateExecutor extends AnimateExecutor {

    private final int transitionId;

    public TransitionAnimateExecutor(int viewId) {
        this.transitionId = viewId;
    }

    @Override
    protected Animator prepareAnimator(View target, boolean enter, BaseScrapView previous, BaseScrapView current) {
        View lastView = previous.getView().findViewById(transitionId);
        View curView = current.getView().findViewById(transitionId);
        if(lastView == null || curView == null){
            throw new IllegalStateException("for transition animation: you must assign a same view id.");
        }
        //translate, scale. then reset
        //prepare image view.
        final ImageView iv = new ImageView(target.getContext());
        Bitmap bitmap = Bitmap.createBitmap(lastView.getWidth(), lastView.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        lastView.draw(canvas);
        iv.setImageBitmap(bitmap);

        final ViewGroup vg = (ViewGroup) previous.getView().getParent();
        int[] pArr = new int[2];
        vg.getLocationOnScreen(pArr);
        int[] arr = new int[2];
        lastView.getLocationOnScreen(arr);
        int[] curArr = new int[2];
        curView.getLocationOnScreen(curArr);

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        //lp.leftMargin = arr[0] - pArr[0];
       // lp.topMargin = arr[1] - pArr[1];
        iv.setX(arr[0] - pArr[0]);
        iv.setY(arr[1] - pArr[1]);
        vg.addView(iv, lp);

        previous.getView().setVisibility(View.INVISIBLE);
        iv.setPivotX(lastView.getWidth()*1f / 2);
        iv.setPivotY(lastView.getHeight()*1f / 2);
        ObjectAnimator sx = ObjectAnimator.ofFloat(iv, View.SCALE_X, 1f, curView.getWidth() * 1f/ lastView.getWidth());
        ObjectAnimator sy = ObjectAnimator.ofFloat(iv, View.SCALE_Y, 1f, curView.getHeight() * 1f/ lastView.getHeight());
        //todo
        ObjectAnimator tx = ObjectAnimator.ofFloat(iv, View.X,
                (arr[0] - pArr[0]) + lastView.getWidth() * 1f/2,
                curArr[0] - pArr[0] + curView.getWidth() * 1f/ 2
        );
        ObjectAnimator ty = ObjectAnimator.ofFloat(iv, View.Y,
                arr[1] - pArr[1] + lastView.getHeight() * 1f/2,
                curArr[1] - pArr[1] + curView.getHeight()*1f/2
        );
        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000);
        set.playTogether(sx, sy, tx, ty);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                previous.getView().setVisibility(View.VISIBLE);
                vg.removeView(iv);
            }
        });
        return set;
    }
}
