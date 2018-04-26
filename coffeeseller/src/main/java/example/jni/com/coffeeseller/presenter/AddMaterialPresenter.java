package example.jni.com.coffeeseller.presenter;

import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.AddMaterial;
import example.jni.com.coffeeseller.model.listeners.IAddMaterial;
import example.jni.com.coffeeseller.model.listeners.OnAddMaterialCallBackListener;
import example.jni.com.coffeeseller.views.viewinterface.IAddMaterialView;

/**
 * Created by Administrator on 2018/4/25.
 */

public class AddMaterialPresenter {
    private IAddMaterial mIAddMaterial;
    private IAddMaterialView iAddMaterialView;

    public AddMaterialPresenter(IAddMaterialView iAddMaterialView) {
        this.iAddMaterialView = iAddMaterialView;
        mIAddMaterial = new AddMaterial();
    }

    public void AddMaterial() {
        mIAddMaterial.addMaterial(iAddMaterialView.getcontext(), iAddMaterialView.getBunkersID(), iAddMaterialView.getSql(), new OnAddMaterialCallBackListener() {
            @Override
            public void addEnd(MaterialSql sql) {
                iAddMaterialView.notifySetDataChange(sql);
            }
        });
    }

    public void addSepcialMaterial() {
        mIAddMaterial.addSpecialMaterial(iAddMaterialView.getcontext(), iAddMaterialView.getBunkersID(), new OnAddMaterialCallBackListener() {
            @Override
            public void addEnd(MaterialSql sql) {
                iAddMaterialView.notifySetDataChange(sql);
            }
        });
    }
}
