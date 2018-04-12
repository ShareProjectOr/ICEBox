package example.jni.com.coffeeseller.presenter;


import example.jni.com.coffeeseller.model.AddFragment;
import example.jni.com.coffeeseller.model.listeners.IAddFragment;
import example.jni.com.coffeeseller.views.viewinterface.IAddFragmentView;

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
        iAddFragment.replaceFragment(iAddFragmentView.getActivity(), iAddFragmentView.getFragment(), iAddFragmentView.getLayoutId());
    }
}
