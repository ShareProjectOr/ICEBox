package example.jni.com.coffeeseller.presenter;

import android.widget.TextView;

import java.util.List;

import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.BindMaterial;
import example.jni.com.coffeeseller.model.listeners.IBindMaterial;
import example.jni.com.coffeeseller.model.listeners.OnBindMaterialCallBackListener;
import example.jni.com.coffeeseller.views.viewinterface.IAddMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.IBindMaterialView;

/**
 * Created by Administrator on 2018/4/27.
 */

public class BindMaterialPresenter {
    private IBindMaterial iBindMaterial;
    private IBindMaterialView iBindMaterialView;
    private IAddMaterialView addMaterialView;

    public BindMaterialPresenter(IBindMaterialView iBindMaterialView, IAddMaterialView addMaterialView) {
        iBindMaterial = new BindMaterial();
        this.iBindMaterialView = iBindMaterialView;
        this.addMaterialView = addMaterialView;
    }

    public void BindMaterial(TextView textView, String binkerID, String bunkerType) {
        iBindMaterial.bindMaterial(iBindMaterialView, addMaterialView.getcontext(), textView, binkerID,bunkerType, new OnBindMaterialCallBackListener() {
            @Override
            public void BindSuccess(List<Material> list) {
                addMaterialView.notifySetDataChange(new MaterialSql(addMaterialView.getcontext()));
            }

            @Override
            public void BindFailed(String reponse) {
                iBindMaterialView.ShowResult(reponse);
            }
        });
    }
}
