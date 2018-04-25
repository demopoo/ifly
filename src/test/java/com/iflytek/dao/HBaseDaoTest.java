package com.iflytek.dao;

import com.iflytek.ApplicationStart;
import com.iflytek.dao.HBaseDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = ApplicationStart.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HBaseDaoTest {

    @Autowired
    public HBaseDao hBaseDao;


    @Test
    public void testGetAppKey() throws IOException{
        System.out.println("yes:"+hBaseDao.getAppKey("2017012515141731"));
    }

    @Test
    public void testGetFocus() throws IOException {
        String  result = hBaseDao.getDbResult("22","rec");
        System.out.println(result);
    }

}
