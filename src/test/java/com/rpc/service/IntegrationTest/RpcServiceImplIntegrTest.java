package com.rpc.service.IntegrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rpc.service.model.AggregatedByLocationData;
import com.rpc.service.model.DataWrapper;
import com.rpc.service.model.ItemData;
import org.junit.Before;
import org.junit.BeforeClass;
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

    private static List<ItemData> sendItemData;
    private static List<AggregatedByLocationData> aggregatedAnswer;
    private static ObjectMapper objectMapper;
    private static DataWrapper<ItemData> itemDataDataWrapper;
    private static DataWrapper<AggregatedByLocationData> resultDataDataWrapper;


    @Before
    public void setUp(){
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

     @BeforeClass
     public static void beforeAll(){
         sendItemData = new ArrayList<>();
         sendItemData.add(new ItemData("id_sample1","numId1","Sochi","as1","None","asd33"));
         sendItemData.add(new ItemData("id_sample2","numId2","3245.456","as2","Nan","asd2s34"));
         sendItemData.add(new ItemData("id_sample3","numId3","New York","as3","None","asd35d"));

         aggregatedAnswer = new ArrayList<>();
         aggregatedAnswer.add(new AggregatedByLocationData("New York", 1L,0L));
         aggregatedAnswer.add(new AggregatedByLocationData("Sochi", 1L,0L));
         aggregatedAnswer.add(new AggregatedByLocationData("3245.456", 0L,1L));

         objectMapper = new ObjectMapper();
         itemDataDataWrapper = new DataWrapper<>(sendItemData);
         resultDataDataWrapper = new DataWrapper<>(aggregatedAnswer);
     }

    @Test
    public void countWorkingSensorsIntgrTest_passValidDataReturnOk() throws Exception {

        JsonNode sentJson = objectMapper.createObjectNode();

        ((ObjectNode) sentJson).put("id", 1);
        ((ObjectNode) sentJson).put("jsonrpc", "2.0");
        ((ObjectNode) sentJson).put("method", "countWorkingSensors");

        JsonNode node = objectMapper.convertValue(itemDataDataWrapper, JsonNode.class);

        ((ObjectNode) sentJson).put("params", node);
        
        String validJson = objectMapper.writeValueAsString(sentJson);

        JsonNode expectedAnswerJson = objectMapper.createObjectNode();

        ((ObjectNode) expectedAnswerJson).put("jsonrpc", "2.0");
        ((ObjectNode) expectedAnswerJson).put("id", 1);
      
        JsonNode agrNode = objectMapper.convertValue(aggregatedAnswer, JsonNode.class);
        ((ObjectNode) expectedAnswerJson).put("result", agrNode);

        String expectedAnswerAsString =  objectMapper.writeValueAsString(expectedAnswerJson);

        MvcResult result = this.mvc.perform(post("/service")
                .content(validJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String returnBodyAsString = result.getResponse().getContentAsString();
        assertEquals(expectedAnswerAsString.trim(),returnBodyAsString.trim());

    }


    @Test
    public void countWorkingSensorsIntgrTest_passInvalidData() throws Exception {
        assertNotNull(wac);
        String invalidData = "{This can be anything actually}";
        String expectedResult ="{\"jsonrpc\":\"2.0\",\"id\":\"null\",\"error\":{\"code\":-32700,\"message\":\"JSON parse error\"}}\n"; // error from JSON-RPC 2.0 documentation

        MvcResult result = this.mvc.perform(post("/service")
                .content(invalidData))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals(expectedResult,result.getResponse().getContentAsString());
    }
    @Test
    public void countWorkingSensorsIntgrTest_wrongMethodName() throws Exception {
        JsonNode rootNode = objectMapper.createObjectNode();

        ((ObjectNode) rootNode).put("id", 1);
        ((ObjectNode) rootNode).put("jsonrpc", "2.0");
        ((ObjectNode) rootNode).put("method", "WRONG_METHOD_NAME");

        JsonNode node = objectMapper.convertValue(itemDataDataWrapper, JsonNode.class);
        ((ObjectNode) rootNode).put("params", node);
        String notSoValidData = objectMapper.writeValueAsString(rootNode);

        String expectedResult ="{\"jsonrpc\":\"2.0\",\"id\":1,\"error\":{\"code\":-32601,\"message\":\"method not found\"}}\n"; // error from JSON-RPC 2.0 documentation


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

        JsonNode node = objectMapper.convertValue(itemDataDataWrapper, JsonNode.class);
        ((ObjectNode) rootNode).put("params", node);
        String validData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        JsonNode expectedAnswerJson = objectMapper.createObjectNode();

        ((ObjectNode) expectedAnswerJson).put("jsonrpc", "2.0");
        ((ObjectNode) expectedAnswerJson).put("id", 1);

      //  itemDataDataWrapper
        JsonNode agrNode = objectMapper.convertValue(resultDataDataWrapper, JsonNode.class);

        ((ObjectNode) expectedAnswerJson).put("result", agrNode);

        String expectedAnswerAsString =  objectMapper.writeValueAsString(expectedAnswerJson);

        MvcResult result = this.mvc.perform(post("/service")
                .content(validData))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String returnBodyAsString = result.getResponse().getContentAsString();
        assertEquals(expectedAnswerAsString.trim(),returnBodyAsString.trim());
    }

}
