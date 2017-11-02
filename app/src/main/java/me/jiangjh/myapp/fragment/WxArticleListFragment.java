package me.jiangjh.myapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.jiangjh.myapp.R;
import me.jiangjh.myapp.activity.WxArticleDetailActivity;
import me.jiangjh.myapp.api.APIService;
import me.jiangjh.myapp.entity.WxArticle;
import me.jiangjh.myapp.entity.WxPage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/24.
 */

public class WxArticleListFragment extends Fragment {

    private Context mContext;
    View mContentView;
    RecyclerView mRecyclerView;
    DataAdapter mAdapter;
    WxPage mPage;
    List<WxArticle> mArticleList;
    private String mCid;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        if (mContentView == null) {
            mContentView = inflater.inflate(R.layout.fragment_wx_article_list, container, false);
            initView(mContentView);
            loadData();
        }

        if (container != null) {
            container.removeView(mContentView);
        }

        return mContentView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter = new DataAdapter(mArticleList = new ArrayList<>()));
    }
    private void loadData() {
        APIService.MokApi mokApi = APIService.factory.getMokApi();
        Call<ResponseBody> call = mokApi.wxSearch(APIService.MokApi.KEY, mCid);
        call.enqueue(new LoadDataCallback(getContext(), this));
    }

    static class LoadDataCallback implements Callback<ResponseBody> {

        WeakReference<Context> contextWR;
        WeakReference<WxArticleListFragment> fragmentWR;

        LoadDataCallback(Context context, WxArticleListFragment fragment) {
            contextWR = new WeakReference<>(context);
            fragmentWR = new WeakReference<>(fragment);
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                        WxPage page = WxPage.parse(retJson.getJSONObject("result"));
                        fragmentWR.get().mPage = page;
                        fragmentWR.get().mArticleList.clear();
                        fragmentWR.get().mArticleList.addAll(page.getArticles());
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

    public void setData(String cid) {
        this.mCid = cid;
    }

    class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<WxArticle> articles;

        DataAdapter(List<WxArticle> articles) {
            this.articles = articles;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(WxArticleListFragment.this.getContext())
                    .inflate(R.layout.rv_article_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            WxArticle article = articles.get(position);
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.title.setText(article.getTitle());
            viewHolder.time.setText(article.getPubTime());
//            viewHolder.thumbnails.setImageBitmap();
            Glide.with(mContext).load(article.getThumbnails()).into(viewHolder.thumbnails);
            viewHolder.getItemView().setTag(article.getSourceUrl());
            viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = (String) v.getTag();
                    Intent intent = new Intent(mContext,
                            WxArticleDetailActivity.class);
                    intent.putExtra(WxArticleDetailActivity.URL, url);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return articles.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView title;
            TextView time;
            ImageView thumbnails;

            public MyViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.wx_article_title);
                time = (TextView) itemView.findViewById(R.id.wx_article_time);
                thumbnails = (ImageView) itemView.findViewById(R.id.wx_article_thumbnails);
            }

            public View getItemView() {
                return this.itemView;
            }

        }
    }
}
