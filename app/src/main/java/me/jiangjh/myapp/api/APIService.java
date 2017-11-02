package me.jiangjh.myapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/10/24.
 */

public class APIService {

    public static class factory {
        private static MokApi mMokApiInstance;

        public static MokApi getMokApi() {
            if (mMokApiInstance == null) {
                synchronized (MokApi.class) {
                    if (mMokApiInstance == null) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(MokApi.BASE_URL)
                                .build();
                        mMokApiInstance = retrofit.create(MokApi.class);
                    }
                }
            }
            return mMokApiInstance;
        }

    }


    public interface MokApi {

        String BASE_URL = "http://apicloud.mob.com";
        String KEY = "21dc4b33fc8e8";

        @GET("/wx/article/category/query")
        Call<ResponseBody> wxCategory(@Query("key") String key);

        @GET("/wx/article/search")
        Call<ResponseBody> wxSearch(@Query("key") String key, @Query("cid") String cid);

        @GET("/wx/article/search")
        Call<ResponseBody> wxSearch(@Query("key") String key, @Query("cid") String cid,
                            @Query("page") int page, @Query("size") int size);

    }

}
