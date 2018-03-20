package example.jni.com.coffeemanagersystem.presenter;

import example.jni.com.coffeemanagersystem.model.AddFragment;
import example.jni.com.coffeemanagersystem.model.IAddFragment;
import example.jni.com.coffeemanagersystem.views.viewinterface.IAddFragmentView;

/**
 * Created by Administrator on 2018/3/2.
 */

public class AddFragmentPresenter {
    private IAddFragmentView iAddFragmentView;
    private IAddFragment iAddFragment;

    public AddFragmentPresenter(IAddFragmentView iAddFragmentView) {
        this.iAddFragmentView = iAddFragmentView;
        iAddFragment = new AddFragment();
    }

    public void AddFragment() {
     //   iAddFragmentView.getObservable().subscribe();
        iAddFragment.addFragment(iAddFragmentView.getActivity(), iAddFragmentView.getFragment(), iAddFragmentView.getLayoutId());
    }
}
