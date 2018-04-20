package ua.genesis.sasha.retrofitfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static GraphApi graphApi;
    List<PostModel> posts;
    RecyclerView recyclerView;

    LoginButton loginButton;
    CallbackManager callbackManager;
    //В ваше приложение одновременно может входить только один человек, и LoginManager
    //предоставляет ему AccessToken и Profile
    // Facebook SDK сохраняет эти данные в общих настройках и задает их в начале сеанса.
    //Чтобы узнать, выполнил ли человек вход, проверьте AccessToken.getCurrentAccessToken() и Profile.getCurrentProfile().
    boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
    //AccessToken.getCurrentAccessToken можно загрузить вместе с SDK из кэша или из закладки приложения при холодном запуске.
//Проверить его действительность можно в методе onCreateActivity:
    //Позже вы сможете выполнить вход, например с помощью элемента OnClickListener индивидуально настроенной кнопки:
    //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();//для обработки откликов входа.
        graphApi=Controller.getApi();
        posts=new ArrayList<>();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        recyclerView=(RecyclerView) findViewById(R.id.posts_recycle_view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        PostsAdapter adapter=new PostsAdapter(posts);
        recyclerView.setAdapter(adapter);


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Если вход выполнен успешно, параметру LoginResult передаются новый AccessToken
                //а также последние предоставленные или отклоненные разрешения.
                // App code
                Log.i("Script","onSuccess");

            }

            @Override
            public void onCancel() {
                // App code
                Log.i("Script","onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("Script","onError");
            }

        });

        //Для успешного входа не требуется registerCallback
        //Можно отслеживать текущие изменения маркера доступа с помощью описанного ниже класса AccessTokenTracker.

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //В методе onActivityResult вызовите callbackManager.onActivityResult,
        //чтобы передать результаты входа в LoginManager через callbackManager.
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        //Каждое действие и фрагмент, интегрируемые с функцией входа или публикации Facebook SDK,
        //должны передавать onActivityResult в callbackManager.
        graphApi.getUserById(50).enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                posts.addAll(response.body());
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"An error occured during networking",Toast.LENGTH_SHORT).show();
            }
        });


    }


}
