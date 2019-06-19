package com.rpc.service.IntegrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rpc.service.model.DataWrapper;
import com.rpc.service.model.ItemData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RpcServiceImplIntegrTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    List<ItemData> itemData;
    ObjectMapper objectMapper;
    DataWrapper<ItemData>  dataWrapper;

    @Before
    public void setup(){
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        itemData = new ArrayList<>();
        itemData.add(new ItemData("id_sample1","numId1","Sochi","as1","None","asd33"));
        itemData.add(new ItemData("id_sample2","numId2","3245.456","as2","Nan","asd2s34"));
        itemData.add(new ItemData("id_sample3","numId3","New York","as3","None","asd35d"));

        objectMapper = new ObjectMapper();
        dataWrapper = new DataWrapper<>(itemData);

    }

    @Test
    public void countWorkingSensorsIntgrTest_passValidDataReturnOk() throws Exception {

        JsonNode rootNode = objectMapper.createObjectNode();

        ((ObjectNode) rootNode).put("id", 1);
        ((ObjectNode) rootNode).put("jsonrpc", "2.0");
        ((ObjectNode) rootNode).put("method", "countWorkingSensors");

        JsonNode node = objectMapper.convertValue(dataWrapper, JsonNode.class);
        ((ObjectNode) rootNode).put("params", node);
        String validJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        String expectedResult = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":[{\"location\":\"New York\",\"numberOfOkSensors\":1,\"numberOfBrokenSensors\":0},{\"location\":\"Sochi\",\"numberOfOkSensors\":1,\"numberOfBrokenSensors\":0},{\"location\":\"3245.456\",\"numberOfOkSensors\":0,\"numberOfBrokenSensors\":1}]}\n";

        MvcResult result = this.mvc.perform(post("/service")
                .content(validJson))
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
        JsonNode rootNode = objectMapper.createObjectNode();

        ((ObjectNode) rootNode).put("id", 1);
        ((ObjectNode) rootNode).put("jsonrpc", "2.0");
        ((ObjectNode) rootNode).put("method", "WRONG_METHOD_NAME");

        JsonNode node = objectMapper.convertValue(dataWrapper, JsonNode.class);
        ((ObjectNode) rootNode).put("params", node);
        String notSoValidData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        String expectedResult ="{\"jsonrpc\":\"2.0\",\"id\":1,\"error\":{\"code\":-32601,\"message\":\"method not found\"}}\n";


        MvcResult result = this.mvc.perform(post("/service")
                .content(notSoValidData))
                .andDo(print())
                 .andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals(expectedResult,result.getResponse().getContentAsString());
    }

    @Test
    public void wrappedCountWorkingSensorsIntgrTest_passValidDataReturnOk() throws Exception {
        JsonNode rootNode = objectMapper.createObjectNode();

        ((ObjectNode) rootNode).put("id", 1);
        ((ObjectNode) rootNode).put("jsonrpc", "2.0");
        ((ObjectNode) rootNode).put("method", "wrappedCountWorkingSensors");

        JsonNode node = objectMapper.convertValue(dataWrapper, JsonNode.class);
        ((ObjectNode) rootNode).put("params", node);
        String validData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        String expectedResult = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"data\":[{\"location\":\"New York\",\"numberOfOkSensors\":1,\"numberOfBrokenSensors\":0},{\"location\":\"Sochi\",\"numberOfOkSensors\":1,\"numberOfBrokenSensors\":0},{\"location\":\"3245.456\",\"numberOfOkSensors\":0,\"numberOfBrokenSensors\":1}]}}\n";

        MvcResult result = this.mvc.perform(post("/service")
                .content(validData))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult,result.getResponse().getContentAsString());
    }

}
