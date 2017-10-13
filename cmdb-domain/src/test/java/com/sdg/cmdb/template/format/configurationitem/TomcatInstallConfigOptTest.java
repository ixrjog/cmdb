package com.sdg.cmdb.template.format.configurationitem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liangjian on 16/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class TomcatInstallConfigOptTest {

    @Test
    public void test() {
        //TODO 这里写用例代码
        //TomcatSetenv ts = TomcatSetenv.builder(this.buildData());
        //System.out.println(ts.toBody());
        TomcatInstallConfigOpt tico = new TomcatInstallConfigOpt();
        tico.put(TomcatInstallConfigOpt.buider("itemcenterqc", "8", "8"));
        tico.put(TomcatInstallConfigOpt.buider("logetl", "7", "7"));
        tico.put(TomcatInstallConfigOpt.buider("lpm", "7", "7"));
        tico.put(TomcatInstallConfigOpt.buider("baoherp", "7", "7"));
        tico.put(TomcatInstallConfigOpt.buider("callcenter", "7", "7"));
        System.out.println(tico.toBody());
    }

}




