package ua.fvadevand.carmaintenance.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrivatbankApi {

    @GET("p24api/aviasstations?json&price")
    Call<ResponseBody> getData(@Query("region") String regionCode);

}
