package ir.mohammadpour.app.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amin on 6/11/2017.
 */

public class ServiceGenerator {
    // 1
    public static String API_BASE_URL = "http://dev";


    public static <S> S createService(Class<S> serviceClass) {

        // 2
//    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
         OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                Request request = original.newBuilder()
                                        .method(original.method(), original.body())
                                        .build();



                                Log.v("urlRetrofit", original.url().toString()+"~ headers "+ original.headers().toString());

                                return chain.proceed(request);
                            }
                        }
                ).build();

        // 3
         Gson gson = new GsonBuilder()
                .setLenient()
                .create();
          Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson));

        // 4
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
