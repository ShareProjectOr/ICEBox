package example.jni.com.coffeeseller.presenter;

import example.jni.com.coffeeseller.model.CommitMaterial;
import example.jni.com.coffeeseller.model.listeners.ICommitMaterial;
import example.jni.com.coffeeseller.model.listeners.OnCommitMaterialCallBackListener;
import example.jni.com.coffeeseller.views.viewinterface.ICommitMaterialView;

/**
 * Created by Administrator on 2018/4/27.
 */

public class CommitMaterialPresenter {
    private ICommitMaterial iCommitMaterial;
    private ICommitMaterialView iCommitMaterialView;

    public CommitMaterialPresenter(ICommitMaterialView iCommitMaterialView) {
        this.iCommitMaterialView = iCommitMaterialView;
        iCommitMaterial = new CommitMaterial();
    }

    public void CommitMaterial() {
        iCommitMaterialView.ShowLoading();
        ;
        iCommitMaterial.Commit(iCommitMaterialView.getList(), new OnCommitMaterialCallBackListener() {
            @Override
            public void commitSuccess() {
                iCommitMaterialView.HideLoading();
                iCommitMaterialView.ShowResult("提交成功");
            }

            @Override
            public void commitFailed(String response) {
                iCommitMaterialView.HideLoading();
                iCommitMaterialView.ShowResult(response);
            }
        });
    }
    public void CommitMaterial2() {
        iCommitMaterialView.ShowLoading();
        ;
        iCommitMaterial.Commit(iCommitMaterialView.getList(), new OnCommitMaterialCallBackListener() {
            @Override
            public void commitSuccess() {
                iCommitMaterialView.HideLoading();
                iCommitMaterialView.ShowResult("提交成功");
                iCommitMaterialView.ChangePage();
            }

            @Override
            public void commitFailed(String response) {
                iCommitMaterialView.HideLoading();
                iCommitMaterialView.ShowResult(response);
            }
        });
    }
}
