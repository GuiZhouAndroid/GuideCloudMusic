package com.zs.itking.guidecloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zs.itking.guidecloudmusic.MainActivity;
import com.zs.itking.guidecloudmusic.MyApplication;
import com.zs.itking.guidecloudmusic.R;
import com.zs.itking.guidecloudmusic.adapter.MyFragmentPagerAdapter;
import com.zs.itking.guidecloudmusic.adapter.TextPagerAdapter;
import com.zs.itking.guidecloudmusic.welcome.FixedSpeedScroller;
import com.zs.itking.guidecloudmusic.welcome.MyInterceptViewPager;

import java.lang.reflect.Field;

public class WelcomeActivity extends AppCompatActivity {


    private static final String TAG = "WelcomeActivity";
    public static boolean SHOW_TWO_ANIM = true;//第二个界面是否展示动画 网易云音乐 3->2时 2没展示动画效果
    MyInterceptViewPager mTextPager;//文字
    MyInterceptViewPager mImageViewPager;//图片
    RelativeLayout mTouchLayout;//点击分发
    ImageView mIndicatorOne, mIndicatorTwo, mIndicatorThree;
    TextView tv_login, tv_go;
    int pageIndex = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        MyApplication.AppContext = WelcomeActivity.this;

        mTextPager = findViewById(R.id.main_text_pager);
        mImageViewPager = findViewById(R.id.main_image_pager);
        mTouchLayout = findViewById(R.id.main_touch_layout);
        mIndicatorOne = findViewById(R.id.main_indicator_one);
        mIndicatorTwo = findViewById(R.id.main_indicator_two);
        mIndicatorThree = findViewById(R.id.main_indicator_three);
        tv_login = findViewById(R.id.tv_login);
        tv_go = findViewById(R.id.tv_go);

        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");//反射
            field.setAccessible(true);
            FixedSpeedScroller scrollerText = new FixedSpeedScroller(this, new AccelerateInterpolator());
            field.set(mTextPager, scrollerText);
            scrollerText.setmDuration(350);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        TextPagerAdapter textPagerAdapter = new TextPagerAdapter();
        mTextPager.setAdapter(textPagerAdapter);
        mImageViewPager.setAdapter(fragmentPagerAdapter);
        mImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onPageSelected(int i) {
                pageIndex = i;
                mIndicatorOne.setImageDrawable(getResources().getDrawable(R.drawable.welcome_circle_gray));
                mIndicatorTwo.setImageDrawable(getResources().getDrawable(R.drawable.welcome_circle_gray));
                mIndicatorThree.setImageDrawable(getResources().getDrawable(R.drawable.welcome_circle_gray));
                switch (i) {
                    case 0:
                        SHOW_TWO_ANIM = true;
                        mIndicatorOne.setImageDrawable(getResources().getDrawable(R.drawable.welcome_circle_main));//第一页滑动圆点
                        break;
                    case 1:
                        if (!tv_login.getText().toString().isEmpty() || !tv_go.getText().toString().isEmpty()) {
                            tv_login.setVisibility(View.INVISIBLE);
                            tv_login.setText("");
                            tv_go.setVisibility(View.INVISIBLE);
                            tv_go.setText("");
                        }
                        mIndicatorTwo.setImageDrawable(getResources().getDrawable(R.drawable.welcome_circle_main));//第二页滑动圆点
                        break;
                    case 2:
                        if (tv_login.getText().toString().isEmpty() && tv_go.getText().toString().isEmpty()) {
                            tv_login.setText("登录/注册");
                            tv_login.setVisibility(View.VISIBLE);
                            tv_go.setText("进入主页");
                            tv_go.setVisibility(View.VISIBLE);
                        }
                        SHOW_TWO_ANIM = false;
                        mIndicatorThree.setImageDrawable(getResources().getDrawable(R.drawable.welcome_circle_main));//第三页滑动圆点

                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //点击分发 实现两个viewpager的联动，根据横向滑动距离和方向判断是否应该翻页
        mTouchLayout.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;//没有用到
            float endX;
            float endY;//没有用到

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        if (startX - endX >= (width / 8)) {// startX - endX 大于0 且大于宽的1/8 可以往后翻页
                            if (pageIndex == 0) {
                                mImageViewPager.setCurrentItem(1);
                                mTextPager.setCurrentItem(1, true);
                            } else if (pageIndex == 1) {
                                mImageViewPager.setCurrentItem(2);
                                mTextPager.setCurrentItem(2, true);
                            } else if (pageIndex == 2) { //ViewPager在最后一页滑动之后，跳转到主页面
                                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            }

                        } else if (endX - startX >= (width / 8)) { // endX - startX   大于0 且大于宽的1/8 可以往前翻页
                            if (pageIndex == 2) {
                                mImageViewPager.setCurrentItem(1);
                                mTextPager.setCurrentItem(1, true);

                            } else if (pageIndex == 1) {
                                mImageViewPager.setCurrentItem(0);
                                mTextPager.setCurrentItem(0, true);
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void onLogin(View view) {
        Toast.makeText(this, "登录/注册", Toast.LENGTH_SHORT).show();
    }

    public void onMain(View view) {
        Toast.makeText(this, "立即体验", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyApplication.AppContext != null) {
            MyApplication.AppContext = null;
        }
    }
}
