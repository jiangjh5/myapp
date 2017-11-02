package me.jiangjh.myapp.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public class WxPage {

    private String curPage;
    private int total;
    private List<WxArticle> articles;

    public static WxPage parse(JSONObject jsonObject) {
        WxPage page = new WxPage();
        try {
            page.setCurPage(jsonObject.getString("curPage"));
            page.setTotal(jsonObject.getInt("total"));
            page.setArticles(WxArticle.parse(jsonObject.getJSONArray("list")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return page;
    }

    public String getCurPage() {
        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<WxArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<WxArticle> articles) {
        this.articles = articles;
    }
}
