package com.team2052.frckrawler.fragments.scout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.common.base.Optional;
import com.team2052.frckrawler.R;
import com.team2052.frckrawler.consumer.OnCompletedListener;
import com.team2052.frckrawler.consumer.SpinnerConsumer;
import com.team2052.frckrawler.database.MetricHelper;
import com.team2052.frckrawler.database.MetricValue;
import com.team2052.frckrawler.db.Event;
import com.team2052.frckrawler.db.Metric;
import com.team2052.frckrawler.db.Robot;
import com.team2052.frckrawler.di.FragmentComponent;
import com.team2052.frckrawler.fragments.BaseDataFragment;
import com.team2052.frckrawler.subscribers.RobotStringSubscriber;
import com.team2052.frckrawler.views.metric.MetricWidget;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 11/26/2015.
 */
public abstract class BaseScoutFragment extends BaseDataFragment<List<Robot>, List<String>, RobotStringSubscriber, SpinnerConsumer>
        implements OnCompletedListener, View.OnClickListener {
    protected Event mEvent;
    public static final String EVENT_ID = "EVENT_ID";
    public LinearLayout mMetricList;
    TextInputLayout mComments;
    @MetricHelper.MetricCategory
    public int scoutType = 0;

    @Override
    public abstract void inject();

    @Override
    protected Observable<? extends List<Robot>> getObservable() {
        return dbManager.robotsAtEvent(mEvent.getId());
    }

    public static class ScoutData {
        public String comments = "";
        public List<MetricValue> values = new ArrayList<>();
    }

    public Subscriber<ScoutData> scoutDataSubscriber() {
        return new Subscriber<ScoutData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ScoutData matchScoutData) {
                if (mComments.getEditText() != null)
                    mComments.getEditText().setText(matchScoutData.comments);
                setValues(matchScoutData.values);
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mMetricList = (LinearLayout) view.findViewById(R.id.metric_widget_list);
        binder.mSpinner = (Spinner) view.findViewById(R.id.robot);
        mComments = (TextInputLayout) view.findViewById(R.id.comments);

        binder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public boolean init;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!init) {
                    init = true;
                    return;
                }
                updateMetricList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        view.findViewById(R.id.button_save).setOnClickListener(this);
        binder.setOnCompletedListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = dbManager.getEventsTable().load(getArguments().getLong(EVENT_ID));
    }

    public List<Robot> getRobots() {
        return subscriber.getData();
    }

    @Nullable
    public Robot getRobot() {
        return subscriber.getData().get(binder.mSpinner.getSelectedItemPosition());
    }

    public boolean isSelectedRobotValid() {
        return getRobot() != null;
    }

    public Observable<MetricValue> metricListObservable() {
        return Observable.create(sub -> {
            final QueryBuilder<Metric> metricQueryBuilder = dbManager.getMetricsTable().query(scoutType, null, mEvent.getGame_id());
            List<Metric> metrics = metricQueryBuilder.list();
            for (int i = 0; i < metrics.size(); i++)
                sub.onNext(new MetricValue(metrics.get(i), null));
            sub.onCompleted();
        });
    }

    public Subscriber<MetricValue> metricValueSubscriber() {
        return new Subscriber<MetricValue>() {
            @Override
            public void onStart() {
                mMetricList.removeAllViews();
            }

            @Override
            public void onCompleted() {
                updateMetricList();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(MetricValue metricValue) {
                Optional<MetricWidget> widget = MetricWidget.createWidget(getActivity(), metricValue);
                if (widget.isPresent())
                    mMetricList.addView(widget.get());
            }
        };
    }

    protected abstract void updateMetricList();

    @Override
    public void onCompleted() {
        metricListObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(metricValueSubscriber());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_save) {
            saveMetrics();
        }
    }

    public List<MetricValue> getValues() {
        List<MetricValue> values = new ArrayList<>();
        for (int i = 0; i < mMetricList.getChildCount(); i++) {
            values.add(((MetricWidget) mMetricList.getChildAt(i)).getValue());
        }
        return values;
    }


    protected void setValues(List<MetricValue> metricValues) {
        if (metricValues.size() != mMetricList.getChildCount()) {
            //This shouldn't happen, but just in case
            mMetricList.removeAllViews();
            for (int i = 0; i < metricValues.size(); i++) {
                Optional<MetricWidget> widget = MetricWidget.createWidget(getActivity(), metricValues.get(i));
                if (widget.isPresent())
                    mMetricList.addView(widget.get());
            }
        } else {
            for (int i = 0; i < metricValues.size(); i++) {
                ((MetricWidget) mMetricList.getChildAt(i)).setMetricValue(metricValues.get(i));
            }
        }
    }

    protected abstract void saveMetrics();
}
