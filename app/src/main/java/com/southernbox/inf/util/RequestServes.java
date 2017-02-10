package com.southernbox.inf.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nanquan.lin on 2017/1/19 0019.
 * 网络请求服务器
 */

public interface RequestServes {

    @GET("/IceAndFireServer/option.json")
    Call<String> getOption();

    @GET("{jsonUrl}")
    Call<String> getPerson(@Path("jsonUrl") String jsonUrl);

}
