package com.xiaofu.mysearch;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by XiaoFu on 2017-06-28 10:35.
 * 注释：
 */

public class MySearchView extends View {
    private Paint bgPaint, textPaint;
    private Paint.FontMetricsInt fontMetrics;
    private float left, top, right, bottom;
    private RectF mRectF;
    private ValueAnimator mAnimator;
    private boolean isOpen = true;//是否打开


    private int bgColor, textSize, textColor;
    private String text;

    private final static int MIN_ALPHA = 0;
    private final static int MAX_ALPHA = 200;
    private final static long ANIMATORE_TIME = 300L;
    private final static String TEXT = "搜索更多";
    private final static int TEXT_SIZE = 22;
    private final static int TEXT_COLOR = Color.WHITE;
    private final static int BG_COLOR = Color.RED;

    public MySearchView(Context context) {
        super(context);
        initView(context, null);
    }

    public MySearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MySearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(BG_COLOR);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE);
        fontMetrics = textPaint.getFontMetricsInt();

        mRectF = new RectF();

        initAnimator();
        getConfigValue(context, attrs);
    }

    private void getConfigValue(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MySearchView, 0, 0);
        try {
            bgColor = typedArray.getColor(R.styleable.MySearchView_backgroundColor, BG_COLOR);
            textColor = typedArray.getColor(R.styleable.MySearchView_textColor, TEXT_COLOR);
            textSize = typedArray.getDimensionPixelSize(R.styleable.MySearchView_textSize, TEXT_SIZE);
            text = typedArray.getString(R.styleable.MySearchView_text);

            setBackgroundColor(bgColor);
            setTextColor(textColor);
            setTextSize(textSize);
            setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    private void initAnimator() {
        if (null == mAnimator) {
            mAnimator = ObjectAnimator.ofInt(MAX_ALPHA, MIN_ALPHA);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    if (!isOpen) {
                        if (left < (right - bottom)) {
                            left = (right - bottom) * animation.getAnimatedFraction();//根据比例获取绘制view左边的值
                            if (null != bgPaint) {
                                bgPaint.setAlpha(value);
                            }
                        }
                    } else {
                        if (left > 0) {
                            left = (right - bottom) * (1 - animation.getAnimatedFraction());
                            if (null != bgPaint) {
                                bgPaint.setAlpha(MAX_ALPHA - value + MIN_ALPHA);
                            }
                        }
                    }

                    if (1f == animation.getAnimatedFraction()) {
                        if (left > 0) {
                            isOpen = false;
                        } else {
                            isOpen = true;
                        }
                        if (null != mOnAnimatorStateListener) {
                            mOnAnimatorStateListener.onAnimatorFinish(isOpen);
                        }
                    } else if (0f == animation.getAnimatedFraction()) {
                        if (null != mOnAnimatorStateListener) {
                            mOnAnimatorStateListener.onAnimatorStart(isOpen);
                        }
                    } else {
                        if (null != mOnAnimatorStateListener) {
                            mOnAnimatorStateListener.onAnimatorIng();
                        }
                    }
                    invalidate();
                }
            });
            mAnimator.setDuration(ANIMATORE_TIME);
            mAnimator.setInterpolator(new AccelerateInterpolator());//先慢后快加速
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        top = left = 0;
        right = w;
        bottom = h;
        bgPaint.setAlpha(MAX_ALPHA);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(left, top, right, bottom);
        canvas.drawRoundRect(mRectF, bottom, bottom, bgPaint);//画出圆角矩形

        int textWidth = getTextWidth(textPaint, text);
        float baseline = (mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2;//计算baseline用于文字居中
        if (mRectF.width() > (textWidth + 2 * bottom)) {
            canvas.drawText(text, left + (mRectF.width() - textWidth) / 2, baseline, textPaint);//写上文字
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (x + getLeft() < getRight() && y + getTop() < getBottom() && isOpen) {//添加点击监听
                    if (null != mOnClickListener) {
                        mOnClickListener.onClick();
                    }
                }
                break;
        }
        return true;
    }

    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public void setBackgroundColor(int color) {
        this.bgColor = color;
        if (null != bgPaint) {
            bgPaint.setColor(bgColor);
        }
    }

    public void setTextColor(int color) {
        this.textColor = color;
        if (null != textPaint) {
            textPaint.setColor(textColor);
        }
    }

    public void setTextSize(int size) {
        this.textSize = size;
        if (null != textPaint) {
            textPaint.setTextSize(textSize);
        }
    }

    public void setText(String s) {
        if (!TextUtils.isEmpty(s)) {
            this.text = s;
        } else {
            this.text = TEXT;
        }
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
        if (null != mAnimator) {
            mAnimator.start();
        }
    }

    private OnAnimatorStateListener mOnAnimatorStateListener;

    public void setOnAnimatorStateListener(OnAnimatorStateListener onAnimatorStateListener) {
        this.mOnAnimatorStateListener = onAnimatorStateListener;
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface OnAnimatorStateListener {
        void onAnimatorStart(boolean isOpen);

        void onAnimatorFinish(boolean isOpen);

        void onAnimatorIng();
    }

    public interface OnClickListener {
        void onClick();
    }

}
