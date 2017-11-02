package me.jiangjh.myapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import me.jiangjh.myapp.fragment.WxArticleListFragment;
import me.jiangjh.myapp.fragment.WxFeaturedFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_frame, new WxFeaturedFragment());
//        WxArticleListFragment fragment = new WxArticleListFragment();
//        fragment.setData("1");
//        ft.replace(R.id.main_frame, fragment);
        ft.commit();
    }
}
