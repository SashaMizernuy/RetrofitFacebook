package ua.genesis.sasha.retrofitfacebook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sasha on 28.03.18.
 */

public class Controller {
    static final String BASE_URL="https://graph.facebook.com/v2.11/";


    public static GraphApi getApi(){

        Gson gson=new GsonBuilder().setLenient().create();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GraphApi graphApi=retrofit.create(GraphApi.class);

        return graphApi;
    }
}
