package ua.genesis.sasha.retrofitfacebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sasha on 28.03.18.
 */

public interface GraphApi {
    @GET("/820882001277849")

    Call<List<PostModel>> getUserById(@Query("id")Integer id , @Query("name")String name);
}
