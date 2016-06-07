package com.team2052.frckrawler.subscribers;

import com.team2052.frckrawler.consumer.DataConsumer;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public abstract class BaseDataSubscriber<T, V> implements Observer<T> {
    DataConsumer<V> mConsumer;
    T data;
    V dataToBind;
    private boolean hasBoundViews;

    @Override
    public void onCompleted() {
        AndroidSchedulers.mainThread().createWorker().schedule(() -> {
            if (mConsumer != null) {
                bindViewsIfNeeded();
                mConsumer.onCompleted();
            }
        });
    }

    @Override
    public void onError(Throwable e) {
        AndroidSchedulers.mainThread().createWorker().schedule(() -> {
            if (mConsumer != null) {
                mConsumer.onError(e);
            }
        });
    }

    @Override
    public void onNext(T t) {
        setData(t);
        parseData();
        bindData();
    }

    public void bindData() {
        AndroidSchedulers.mainThread().createWorker().schedule(() -> {
            if (mConsumer != null) {
                mConsumer.updateData(dataToBind);
            }
        });
    }

    public void bindViewsIfNeeded(){
        if(!hasBoundViews && mConsumer != null){
            mConsumer.bindViews();
            hasBoundViews = true;
        }
    }

    public void setConsumer(DataConsumer<V> mConsumer) {
        this.mConsumer = mConsumer;
    }

    public abstract void parseData();

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
