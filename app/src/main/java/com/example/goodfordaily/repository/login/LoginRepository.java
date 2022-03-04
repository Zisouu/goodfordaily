package com.example.goodfordaily.repository.login;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.goodfordaily.model.LoginModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginRepository {

    private LoginDao loginDao;
    private LiveData<List<LoginModel>> allData;
    private CompositeDisposable compositeDisposable;

    public LoginRepository(Application application ,CompositeDisposable compositeDisposable) {

        LoginDatabase db = LoginDatabase.getDatabase(application);
        loginDao = db.loginDoa();
        allData = loginDao.getDetails();
        this.compositeDisposable = compositeDisposable;
    }

    public void deleteData() {
        loginDao.deleteAllData();
    }

    public LiveData<List<LoginModel>> getAllData() {
        return allData;
    }

    public void insertData(LoginModel data) {
        compositeDisposable.add(
                loginDao.insert(data)
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d("TAG", "onComplete: insert success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("TAG", "onError: insert ", e);
                            }
                        })
        );
    }
}