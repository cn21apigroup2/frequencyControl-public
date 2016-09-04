package com.cn21.frequencyControl_public;

import com.cn21.data.admin.DataManager;
import org.junit.Test;

/**
 * Created by steven on 2016/9/3.
 */
public class DataManagerTest {
    @Test
    public  void dataManagerTtest() throws  Exception{
        DataManager dataManager = DataManager.getInstance();
        dataManager.init("AAAAA","AAAAAA");

        System.out.println(dataManager.getGlobalLimited().getTimeoutOfSeconds());
    }
}
