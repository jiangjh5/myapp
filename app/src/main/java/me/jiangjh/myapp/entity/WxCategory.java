package me.jiangjh.myapp.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public class WxCategory implements Serializable {

    private String id;
    private String name;

    public static List<WxCategory> parse(JSONArray jsonArray) {
        List<WxCategory> categoryList = new ArrayList<>();
        int l = jsonArray.length();
        for(int i = 0; i < l; i++) {
            try {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                WxCategory category = parse(jsonObj);
                if (category != null) {
                    categoryList.add(category);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categoryList;
    }

    public static WxCategory parse(JSONObject jsonObj) {
        WxCategory c = new WxCategory();
        try {
            c.setId(jsonObj.getString("cid"));
            c.setName(jsonObj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return c;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
