package example.jni.com.coffeeseller.views.viewinterface;

import java.util.List;

import example.jni.com.coffeeseller.bean.CommitMaterialObject;

/**
 * Created by Administrator on 2018/4/27.
 */

public interface ICommitMaterialView {
    List<CommitMaterialObject> getList();

    void ShowResult(String result);
}
