package com.example.tesshared.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;
import com.example.tesshared.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationsFragment extends Fragment {
    SharedPreferences sharedPreferences;
    int id_user;
    final String SHARED_PREFERENCES_NAME = "shared_preferences";
    final String SESSION_STATUS = "session";
    public final static String TAG_TOKEN = "token";
    public final static int TAG_ID = 0;
    BaseApiHelper mApiService;
    public static final String URL = "http://10.0.2.2:8000/api/";
    private NotificationsViewModel notificationsViewModel;
    TextView mNama,mEmail;
    Button mLogout,mEdit;
    View mView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        mNama = root.findViewById(R.id.tvNama);
        mEmail = root.findViewById(R.id.tvEmail);
        mEdit = root.findViewById(R.id.toEdit);
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        id_user = sharedPreferences.getInt(String.valueOf(TAG_ID),0);
        Log.e("ID_USER", "onCreateView: "+id_user);
        mApiService = UtilsApi.getAPIService();


        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileDetail.class));
            }
        });
        return root;
    }

    private void load(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BaseApiHelper api = retrofit.create(BaseApiHelper.class);
        Call<ValueUser> call = api.viewUser(id_user);
        call.enqueue(new Callback<ValueUser>() {
            @Override
            public void onResponse(Call<ValueUser> call, Response<ValueUser> response) {
                Log.e("AS", "onResponse: "+response.body().getEmail());
                mEmail.setText(response.body().getEmail());
                mNama.setText(response.body().getName());
            }

            @Override
            public void onFailure(Call<ValueUser> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }
}