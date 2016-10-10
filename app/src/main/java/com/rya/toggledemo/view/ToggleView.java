package com.rya.toggledemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义开关
 *
 * @author poplar
 *         <p>
 *         Android 的界面绘制流程
 *         测量			 摆放		绘制
 *         measure	->	layout	->	draw
 *         | 		  |			 |
 *         onMeasure -> onLayout -> onDraw 重写这些方法, 实现自定义控件
 *         <p>
 *         onResume()之后执行
 *         <p>
 *         View
 *         onMeasure() (在这个方法里指定自己的宽高) -> onDraw() (绘制自己的内容)
 *         <p>
 *         ViewGroup
 *         onMeasure() (指定自己的宽高, 所有子View的宽高)-> onLayout() (摆放所有子View) -> onDraw() (绘制内容)
 */

public class ToggleView extends View {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private Paint mPaint;
    private Bitmap mSwitchBackground;
    private boolean mState;
    private Bitmap mSlideButton;
    private boolean isTouching;
    private float mCurrentX;
    private OnStateChangeListener onStateChangeListener;

    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (context != null && attrs != null) {
            int switch_background = attrs.getAttributeResourceValue(NAMESPACE, "switch_background", -1);
            int slide_button = attrs.getAttributeResourceValue(NAMESPACE, "slide_button", -1);
            boolean state = attrs.getAttributeBooleanValue(NAMESPACE, "state", false);

            setSwitchBackgroundResource(switch_background);
            setSlideButtonResource(slide_button);
            setSwitchState(state);
        }

        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    // 方法指定View的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSwitchBackground.getWidth(), mSwitchBackground.getHeight());
    }

    // 绘制内容
    @Override
    protected void onDraw(Canvas canvas) {
        // 填充背景图
        canvas.drawBitmap(mSwitchBackground, 0, 0, mPaint);

        //填充前景图(按钮)
        int maxLeft = mSwitchBackground.getWidth() - mSlideButton.getWidth();
        if (isTouching) {
            if (mCurrentX < 0 || mCurrentX > maxLeft) {
                if (mCurrentX < 0) {
                    canvas.drawBitmap(mSlideButton, 0, 0, mPaint);
                } else {
                    canvas.drawBitmap(mSlideButton, maxLeft, 0, mPaint);
                }
            } else {
                canvas.drawBitmap(mSlideButton, mCurrentX, 0, mPaint);
            }
        } else {
            if (mState) {
                canvas.drawBitmap(mSlideButton, maxLeft, 0, mPaint);
            } else {
                canvas.drawBitmap(mSlideButton, 0, 0, mPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                mCurrentX = event.getX() - (mSlideButton.getWidth() / 2);
                System.out.println("ACTION_DOWN...");
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX() - (mSlideButton.getWidth() / 2);
                System.out.println("ACTION_MOVE..." + mCurrentX);
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                boolean state;

                mCurrentX = event.getX() - (mSlideButton.getWidth() / 2);
                System.out.println("ACTION_UP...");
                if (event.getX() > (mSwitchBackground.getWidth() / 2)) {
                    state = true;
                } else {
                    state = false;
                }
                if ((onStateChangeListener != null) && (state != mState)) {
                    onStateChangeListener.onStateChange(state);
                }
                mState = state;

                break;
            default:
                break;
        }
        // 系统调用onDraw()
        invalidate();

        return true;
    }

    /**
     * 设置背景图片
     *
     * @param switch_background
     */
    public void setSwitchBackgroundResource(int switch_background) {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), switch_background);
    }

    /**
     * 设置前景(开关按钮背景)
     *
     * @param slide_button
     */
    public void setSlideButtonResource(int slide_button) {
        mSlideButton = BitmapFactory.decodeResource(getResources(), slide_button);
    }

    /**
     * 设置开关状态
     *
     * @param switchState
     */
    public void setSwitchState(boolean switchState) {
        this.mState = switchState;
    }

    public interface OnStateChangeListener {
        public void onStateChange(boolean state);
    }

    //内部监听方法
    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }
}
