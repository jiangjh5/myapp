package me.jiangjh.myapp.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public class WxArticle {

    private String id;
    private String cid;
    private String title;
    private String subTitle;
    private String sourceUrl;
    private String pubTime;
    private String thumbnails;

    public static List<WxArticle> parse(JSONArray jsonArray) {
        List<WxArticle> articles = new ArrayList<>();
        int l = jsonArray.length();
        for(int i = 0; i < l; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxArticle article = parse(jsonObject);
                if (article != null) {
                    articles.add(article);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }

    public static WxArticle parse(JSONObject jsonObject) {
        WxArticle article = new WxArticle();
        try {
            article.setId(jsonObject.getString("id"));
            article.setCid(jsonObject.getString("cid"));
            article.setTitle(jsonObject.getString("title"));
            article.setSubTitle(jsonObject.getString("subTitle"));
            article.setPubTime(jsonObject.getString("pubTime"));
            article.setThumbnails(jsonObject.getString("thumbnails"));
            article.setSourceUrl(jsonObject.getString("sourceUrl"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return  article;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }
}
