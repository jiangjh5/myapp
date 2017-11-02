package me.jiangjh.myapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.jiangjh.myapp.R;
import me.jiangjh.myapp.api.APIService;
import me.jiangjh.myapp.entity.WxCategory;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2017/10/24.
 */

public class WxFeaturedFragment extends Fragment {

    CoordinatorLayout mContentView;
    TabLayout mTabs;
    ViewPager mPagers;
    DataAdapter mAdapter;
    List<WxCategory> mCategoryList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        if (mContentView == null) {
            mContentView = (CoordinatorLayout) inflater.inflate(R.layout.fragment_wx_featured, container, false);
            initView(mContentView);
            loadData();
        }

        if (container != null) {
            container.removeView(mContentView);
        }

        return mContentView;
    }

    private void initView(ViewGroup view) {
        mTabs = (TabLayout) view.findViewById(R.id.tabLayout);
        mPagers = (ViewPager) view.findViewById(R.id.viewPager);
        mCategoryList = new ArrayList<>();
        mPagers.setAdapter(mAdapter = new DataAdapter(getChildFragmentManager(), mCategoryList));
        mTabs.setupWithViewPager(mPagers);
    }

    private void loadData() {
        APIService.MokApi mokApi = APIService.factory.getMokApi();
        Call<ResponseBody> call = mokApi.wxCategory(APIService.MokApi.KEY);
        call.enqueue(new LoadDataCallback(getContext(), this));
    }

    static class LoadDataCallback implements Callback<ResponseBody> {

        WeakReference<Context> contextWR;
        WeakReference<WxFeaturedFragment> fragmentWR;

        LoadDataCallback(Context context, WxFeaturedFragment fragment) {
            contextWR = new WeakReference<>(context);
            fragmentWR = new WeakReference<>(fragment);
        }

        @Override
        public void onResponse(Call<ResponseBody> call,  retrofit2.Response<ResponseBody> response) {
            if (contextWR.get() == null)
                return;

            ResponseBody body = response.body();
            if (body != null) {
                try {
                    String retStr = body.string();
                    try {
                        JSONObject retJson = new JSONObject(retStr);
                        String retCode = retJson.getString("retCode");
                        if (!"200".equals(retCode))
                            return;
                        JSONArray result = retJson.getJSONArray("result");
                        List<WxCategory> categories = WxCategory.parse(result);
                        fragmentWR.get().mCategoryList.clear();
                        fragmentWR.get().mCategoryList.addAll(categories);
                        fragmentWR.get().mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }

    class DataAdapter extends FragmentPagerAdapter {

        private List<WxCategory> categories;

        public DataAdapter(FragmentManager fm, List<WxCategory> categories) {
            super(fm);
            this.categories = categories;
        }

        @Override
        public Fragment getItem(int position) {
            WxArticleListFragment fragment = new WxArticleListFragment();
            WxCategory category = categories.get(position);
            fragment.setData(category.getId());
            return fragment;
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < categories.size()) {
                return categories.get(position).getName();
            } else {
                return null;
            }
        }

//        @Override
//        public void notifyDataSetChanged() {
//            super.notifyDataSetChanged();
//            mPagers.setOffscreenPageLimit(getCount());
//        }
    }


}
