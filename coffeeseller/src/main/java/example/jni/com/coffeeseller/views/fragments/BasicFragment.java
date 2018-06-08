package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/4/11.
 */

public class BasicFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = null;
        if (containerView == null) {
            int layoutid = ((savedInstanceState == null) ? 0 : savedInstanceState.getInt("layout_id"));
            containerView = inflater.inflate(layoutid, null, false);
        }
        ViewGroup parent = (ViewGroup) containerView.getParent();
        if (parent != null) {
            parent.removeView(containerView);
        }
        return containerView;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
