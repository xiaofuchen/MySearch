package com.xiaofu.mysearch;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by XiaoFu on 2017-06-30 10:02.
 * 注释：
 */

public class MySearchView2 extends RelativeLayout {
    private RelativeLayout rl_content;
    private MySearchView search;
    private ImageView iv_search;
    private ImageButton ib_search;
    private boolean isAnmotioning = false;
    private Context mContext;
    private int marginsTop = 0;
    private int marginsTopMark = 0;
    private ObjectAnimator animator;

    public MySearchView2(Context context) {
        super(context);
        init(context);
    }

    public MySearchView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MySearchView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_search2, this);

        rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
        search = (MySearchView) view.findViewById(R.id.search);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        ib_search = (ImageButton) view.findViewById(R.id.ib_search);

        setListener();
    }

    private void setListener() {
        search.setOnClickListener(new MySearchView.OnClickListener() {
            @Override
            public void onClick() {
                if (null != mOnSearchClickListener) {
                    mOnSearchClickListener.onSearchClick();
                }
            }
        });
        search.setOnAnimatorStateListener(new MySearchView.OnAnimatorStateListener() {
            @Override
            public void onAnimatorStart(boolean isOpen) {
                isAnmotioning = false;
                if (isOpen) {
                    iv_search.setVisibility(VISIBLE);
                    ib_search.setVisibility(GONE);
                }
            }

            @Override
            public void onAnimatorFinish(boolean isOpen) {
                isAnmotioning = false;
                if (isOpen) {
                    ib_search.setVisibility(GONE);
                    iv_search.setVisibility(VISIBLE);
                } else {
                    if(null == animator) {
                        animator = ObjectAnimator.ofFloat(ib_search, "alpha", 0.5f, 1f);
                        animator.setDuration(MySearchView.ANIMATORE_TIME);
                    }
                    animator.start();
                    ib_search.setVisibility(VISIBLE);
                    iv_search.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void onAnimatorIng() {
                isAnmotioning = true;
            }
        });


        iv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSearchClickListener) {
                    mOnSearchClickListener.onSearchClick();
                }
            }
        });
        ib_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSearchClickListener) {
                    mOnSearchClickListener.onSearchClick();
                }
            }
        });
    }


    /**
     * 设置控件初始化时候相距父布局的高度
     *
     * @param top
     */
    public void setMarginTopFixed(int top) {
        if (0 < top) {
            marginsTop = top;
        }
        setMarginTop(top);
    }

    /**设置控件相距父布局的高度
     * @param top
     */
    private void setMarginTop(int top) {
        if (top < 0 || marginsTopMark== top) {
            return;
        }
//        if (top < Utils.dp2px(mContext, 10)) {//默认相距父布局10dp
//            top = Utils.dp2px(mContext, 10);
//        }
        if (rl_content.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) rl_content.getLayoutParams();
            p.setMargins(0, top, 0, 0);
            rl_content.requestLayout();
        }

        marginsTopMark = top;
    }

    /**
     * 设置滑动时候的状态显示
     *
     * @param srcollY
     */
    public void setSrcollValue(int srcollY) {
//        Log.e("xf", "srcollY" + srcollY + "marginsTop" + marginsTop);

        if (srcollY > marginsTop) {
            setMarginTop(0);
            if (search.isOpen() && !isAnmotioning) {
                search.setIsOpen(false);
            }
        } else {
            setMarginTop(marginsTop - srcollY);
            if (!search.isOpen() && !isAnmotioning) {
                search.setIsOpen(true);
            }
        }
    }

    private OnSearchClickListener mOnSearchClickListener;

    public void setOnSearchClickListener(OnSearchClickListener onSearchClickListener) {
        this.mOnSearchClickListener = onSearchClickListener;
    }

    public interface OnSearchClickListener {
        void onSearchClick();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(null != animator){
            animator = null;
        }
    }
}
