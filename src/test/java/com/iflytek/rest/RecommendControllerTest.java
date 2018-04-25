package com.iflytek.rest;


import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 推荐接口测试
 * @author yu
 */
public class RecommendControllerTest extends ControllerBaseTest {


    /**
     * 测试recOtt接口
     * @throws Exception
     */
    @Test
    public void testRecOtt() throws Exception {
        MvcResult result = mockMvc.perform(post("/iflytek/api/suggestion/rec_ott")
                .contentType(MediaType.TEXT_XML)
                .content(testData())
        ).andExpect(status().isOk()).andDo(print()).andReturn();
        System.out.println("result:"+result.getResponse().getContentAsString());
    }

    /**
     * 测试播放结束推荐接口
     * @throws IOException
     */
    @Test
    public void testRecVodEnd() throws Exception{
        MvcResult result = mockMvc.perform(post("/iflytek/api/suggestion/rec_vod_end")
                .contentType(MediaType.TEXT_XML)
                .content(testData())
        ).andExpect(status().isOk()).andDo(print()).andReturn();
        System.out.println("result:"+result.getResponse().getContentAsString());
    }

    /**
     * 测试播放相关推荐接口
     * @throws Exception
     */
    @Test
    public void testRecVodRelate() throws Exception{
        MvcResult result = mockMvc.perform(post("/iflytek/api/suggestion/rec_vod_relate")
                .contentType(MediaType.TEXT_XML)
                .content(testData())
        ).andExpect(status().isOk()).andDo(print()).andReturn();
        System.out.println("result:"+result.getResponse().getContentAsString());
    }

    private String testData(){
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "\n" +
                "<operation_in> \n" +
                "  <verify_code>210094032167747711503958</verify_code>  \n" +
                "  <sysfunc_id>60500020</sysfunc_id>  \n" +
                "  <service_name>BSM_crm_queryMenuInfo</service_name>  \n" +
                "  <accept_info> \n" +
                "    <accept_city>590</accept_city>  \n" +
                "    <accept_org_id>9990000</accept_org_id>  \n" +
                "    <accept_province>5910</accept_province>  \n" +
                "    <accept_id>1234567890</accept_id> \n" +
                "  </accept_info>  \n" +
                "  <request_source>201028</request_source>  \n" +
                "  <request_time>20080109101506</request_time>  \n" +
                "  <content> \n" +
                "    <uids>y_00000000000000,222,333</uids>  \n" +
                "    <request_time>20170125152411001</request_time>  \n" +
                "    <app_id>yishiteng20170125</app_id>  \n" +
                "    <secure_msg>jpghljcgaahhechpdigjlmhegkepebbbckdggmcjehfjppglmfefdloiooofpmpkdiniombjaglbokhjeekapnfhnkcgaihp</secure_msg> \n" +
                "  </content> \n" +
                "</operation_in>";
        return xml;
    }
}
