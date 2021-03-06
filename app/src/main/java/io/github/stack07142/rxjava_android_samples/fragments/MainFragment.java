package io.github.stack07142.rxjava_android_samples.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.stack07142.rxjava_android_samples.R;
import timber.log.Timber;

public class MainFragment extends BaseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_demo_schedulers)
    void demoConcurrencyWithSchedulers() {
        clickedOn(new ConcurrencyWithSchedulersDemoFragment());
    }

    @OnClick(R.id.btn_demo_buffer)
    void demoBuffer() {
        clickedOn(new BufferDemoFragment());
    }

    @OnClick(R.id.btn_demo_debounce)
    void demoDebounce() {
        clickedOn(new DebounceSearchEmitterFragment());
    }

    @OnClick(R.id.btn_demo_retrofit)
    void demoRetrofitCalls() {
        clickedOn(new RetrofitFragment());
    }

    @OnClick(R.id.btn_demo_two_way_data_binding)
    void demoTwoWayDataBinding() {
        clickedOn(new TwoWayDataBindingFragment());
    }

    @OnClick(R.id.btn_demo_polling)
    void demoPolling() {
        clickedOn(new PollingFragment());
    }

    @OnClick(R.id.btn_demo_form_validation_combinelatest)
    void demoFormValidation() {
        clickedOn(new FormValidationCombineLatestFragment());
    }

    @OnClick(R.id.btn_demo_pseudo_cache)
    void demoPseudoCache() {
        clickedOn(new PseudoCacheFragment());
    }

    private void clickedOn(@NonNull Fragment fragment) {
        Timber.tag(TAG).d("clickedOn:: %s", fragment.getClass().getSimpleName());

        final String tag = fragment.getClass().toString();
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(android.R.id.content, fragment, tag)
                .commit();
    }
}
