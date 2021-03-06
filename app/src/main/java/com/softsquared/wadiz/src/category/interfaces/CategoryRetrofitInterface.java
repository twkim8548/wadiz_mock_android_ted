package com.softsquared.wadiz.src.category.interfaces;

import com.softsquared.wadiz.src.category.models.CategoryNameResponse;
import com.softsquared.wadiz.src.category.models.ItemlistResponse;
import com.softsquared.wadiz.src.main.reward.reward_home.models.ItemResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryRetrofitInterface {

    @GET("/category")
    Call<CategoryNameResponse> getCategoryBanner();

    @GET("/project/{categoryIdx}")
    Call<ItemlistResponse> getCategoryItem(@Path("categoryIdx") String categoryIdx);

}
