package com.iflytek.service;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.iflytek.entity.req.RecommendCommonParams;
import com.iflytek.entity.req.RequestContent;
import com.iflytek.entity.res.ResponseContent;
import com.sun.javaws.jnl.XMLFormat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author yu
 */
public class RecServiceTest extends ServiceBaseTest {


    @Autowired
    private RecService recService;


    @Test
    public void testGetFocusRec() throws IOException{
        RecommendCommonParams params = new RecommendCommonParams();

        RequestContent reqContent = new RequestContent();
        reqContent.setUids("y_00000000000000");

        params.setContent(reqContent);

        ResponseContent content = recService.getAllRec(params);

        XmlMapper mapper = new XmlMapper();
        System.out.println(mapper.writeValueAsString(content));
    }
}
