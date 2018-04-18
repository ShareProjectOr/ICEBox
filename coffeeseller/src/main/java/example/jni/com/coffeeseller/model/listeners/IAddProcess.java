package example.jni.com.coffeeseller.model.listeners;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

import cof.ac.inter.ContainerConfig;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface IAddProcess {
    void AddProcess(Context mContext,List<ContainerConfig> list,OnAddProcessCallBackListener addProcessCallBackListener);
    void EditProcess(Context mContext,List<ContainerConfig> list,int position,OnAddProcessCallBackListener addProcessCallBackListener);
    void RemoveProcess(Context mContext,List<ContainerConfig> list,int position,OnAddProcessCallBackListener addProcessCallBackListener);
}
