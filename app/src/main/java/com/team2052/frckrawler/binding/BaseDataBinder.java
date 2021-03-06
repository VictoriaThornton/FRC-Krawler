package com.team2052.frckrawler.binding;

import android.app.Activity;
import android.support.annotation.UiThread;
import android.view.View;

import com.google.firebase.crash.FirebaseCrash;

public abstract class BaseDataBinder<V> {
    protected Activity mActivity;
    protected View mRootView;

    public abstract void updateData(V data);

    public void onError(Throwable e) {
        e.printStackTrace();
        FirebaseCrash.report(e);
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void onCompleted() {
    }

    public void setmRootView(View mRootView) {
        this.mRootView = mRootView;
    }

    @UiThread
    public abstract void bindViews();
}
