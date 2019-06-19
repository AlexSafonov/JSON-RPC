package com.rpc.service.UnitTesting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RpcServiceImplIntegrTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @Before
    public void setup(){

        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void countWorkingSensorsIntgrTest_passValidDataReturnOk() throws Exception {
        assertNotNull(wac);

        String validData = "[{\"id\":\"1\",\"jsonrpc\":\"2.0\",\"method\":\"countWorkingSensors\",\"params\":{\"data\":[" +
                "{\"id_sample\":\"76rtw\",\"num_id\":\"ffg#er111\",\"id_location\":\"Sochi\",\"id_signal_par\":\"0xcv11cs1\",\"id_detected\":\"None\",\"id_class_det\":\"req111\"}," +
                "{\"id_sample\":\"76rtw\",\"num_id\":\"ffg#er112\",\"id_location\":\"987.654\",\"id_signal_par\":\"0xcv11cs1\",\"id_detected\":\"None\",\"id_class_det\":\"req111\"}" +
                "]}}]";

        String expectedResult = "[{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"result\":[{\"location\":\"987.654\",\"numberOfOkSensors\":1,\"numberOfBrokenSensors\":0},{\"location\":\"Sochi\",\"numberOfOkSensors\":1,\"numberOfBrokenSensors\":0}]}\n" +
                "]";

        MvcResult result = this.mvc.perform(post("/service")
                .content(validData))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult,result.getResponse().getContentAsString());
    }
    @Test
    public void countWorkingSensorsIntgrTest_passInvalidData() throws Exception {
        assertNotNull(wac);
        String invalidData = "{This can be anything actually}";
        String expectedResult ="{\"jsonrpc\":\"2.0\",\"id\":\"null\",\"error\":{\"code\":-32700,\"message\":\"JSON parse error\"}}\n";

        MvcResult result = this.mvc.perform(post("/service")
                .content(invalidData))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals(expectedResult,result.getResponse().getContentAsString());
    }
    @Test
    public void countWorkingSensorsIntgrTest_wrongMethodNamea() throws Exception {
        assertNotNull(wac);
        String notSoVlidData = "[{\"id\":\"1\",\"jsonrpc\":\"2.0\",\"method\":\"WRONG_METHOD_NAME\",\"params\":{\"data\":[" +
                "{\"id_sample\":\"76rtw\",\"num_id\":\"ffg#er111\",\"id_location\":\"Sochi\",\"id_signal_par\":\"0xcv11cs1\",\"id_detected\":\"None\",\"id_class_det\":\"req111\"}," +
                "{\"id_sample\":\"76rtw\",\"num_id\":\"ffg#er112\",\"id_location\":\"987.654\",\"id_signal_par\":\"0xcv11cs1\",\"id_detected\":\"None\",\"id_class_det\":\"req111\"}" +
                "]}}]";
        String expectedResult ="[{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"error\":{\"code\":-32601,\"message\":\"method not found\"}}\n" +
                "]";

        MvcResult result = this.mvc.perform(post("/service")
                .content(notSoVlidData))
                .andDo(print())
                 .andExpect(status().is5xxServerError())
                .andReturn();
        assertEquals(expectedResult,result.getResponse().getContentAsString());
    }

    @Test
    public void wrappedCountWorkingSensorsIntgrTest_passValidDataReturnOk() throws Exception {
        assertNotNull(wac);

        String validData = "[{\"id\":\"1\",\"jsonrpc\":\"2.0\",\"method\":\"wrappedCountWorkingSensors\",\"params\":{\"data\":[" +
                "{\"id_sample\":\"76rtw\",\"num_id\":\"ffg#er111\",\"id_location\":\"Sochi\",\"id_signal_par\":\"0xcv11cs1\",\"id_detected\":\"None\",\"id_class_det\":\"req111\"}," +
                "{\"id_sample\":\"76rtw\",\"num_id\":\"ffg#er112\",\"id_location\":\"987.654\",\"id_signal_par\":\"0xcv11cs1\",\"id_detected\":\"None\",\"id_class_det\":\"req111\"}" +
                "]}}]";

        String expectedResult = "[{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"result\":{\"data\":[{\"location\":\"987.654\",\"numberOfOkSensors\":1,\"numberOfBrokenSensors\":0},{\"location\":\"Sochi\",\"numberOfOkSensors\":1,\"numberOfBrokenSensors\":0}]}}\n" +
                "]";

        MvcResult result = this.mvc.perform(post("/service")
                .content(validData))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult,result.getResponse().getContentAsString());
    }

}
