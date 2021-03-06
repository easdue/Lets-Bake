package nl.erikduisters.letsbake.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by Erik Duisters on 04-12-2017.
 */
public abstract class BaseActivity<VM extends ViewModel> extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public ViewModelProvider.Factory viewModelFactory;

    protected VM viewModel;

    private boolean isFragmentStateLocked;

    @Nullable
    private Unbinder unbinder;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        unbinder = ButterKnife.bind(this);

        isFragmentStateLocked = true;

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass());
    }

    protected abstract @LayoutRes int getLayoutResId();
    protected abstract Class<VM> getViewModelClass();

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        isFragmentStateLocked = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        isFragmentStateLocked = true;
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }

        super.onDestroy();
    }
}
