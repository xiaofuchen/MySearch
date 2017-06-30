package com.xiaofu.mysearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyScrollView scrollview;
    private MySearchView search;
    private MySearchView2 search2;
    private ImageButton iv_search;
    private boolean isAnmotioning = false;//是否在动画中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void initView() {
        scrollview = (MyScrollView) findViewById(R.id.scrollview);
        search = (MySearchView) findViewById(R.id.search);
        search2 = (MySearchView2) findViewById(R.id.search2);
        iv_search = (ImageButton) findViewById(R.id.iv_search);

        search2.setMarginTopFixed(Utils.dp2px(this,450));
    }

    private void setListener() {
        scrollview.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY, int oritention) {
                search2.setSrcollValue(scrollY);
                if (scrollY >= Utils.dp2px(MainActivity.this.getApplicationContext(), 200) && View.GONE == iv_search.getVisibility() && MyScrollView.SCROLL_UP == oritention && !isAnmotioning) {
                    search.setIsOpen(false);
                }
                if (scrollY <= Utils.dp2px(MainActivity.this.getApplicationContext(), 200) && View.VISIBLE == iv_search.getVisibility() && MyScrollView.SCROLL_DOWN == oritention && !isAnmotioning) {
                    search.setIsOpen(true);
                }
            }
        });
        search.setOnAnimatorStateListener(new MySearchView.OnAnimatorStateListener() {
            @Override
            public void onAnimatorStart(boolean isOpen) {
                isAnmotioning = false;
                if(isOpen){
                    iv_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimatorFinish(boolean isOpen) {
                isAnmotioning = false;
                if (!isOpen) {
                    iv_search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimatorIng() {
                isAnmotioning = true;
            }
        });
        search.setOnClickListener(new MySearchView.OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(), "点击", Toast.LENGTH_SHORT).show();
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击", Toast.LENGTH_SHORT).show();
            }
        });
        search2.setOnSearchClickListener(new MySearchView2.OnSearchClickListener() {
            @Override
            public void onSearchClick() {
                Toast.makeText(getApplicationContext(), "点击", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
