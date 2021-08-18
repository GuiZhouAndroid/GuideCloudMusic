package com.zs.itking.guidecloudmusic.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.zs.itking.guidecloudmusic.R;
import com.zs.itking.guidecloudmusic.welcome.LazyLoadFragment;


/**
 * Created by wobiancao on 19/1/11.
 */
public class FragmentOnePage extends LazyLoadFragment {
    ImageView mBgView;
    ImageView mShowView;

    public static FragmentOnePage newInstance() {
        FragmentOnePage page = new FragmentOnePage();
        Bundle args = new Bundle();
        page.setArguments(args);
        return page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBgView = findViewById(R.id.image_one_bg);
        mShowView = findViewById(R.id.image_one_show);
    }

    @Override
    protected int setContentView() {
        return R.layout.welcome_frgament_one_page;
    }

    @Override
    protected void lazyLoad() {

    }
}
