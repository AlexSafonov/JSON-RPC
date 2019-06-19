package com.rpc.service.UnitTesting;


import com.rpc.service.model.AggregatedByLocationData;
import com.rpc.service.model.DataWrapper;
import com.rpc.service.model.ItemData;
import com.rpc.service.service.RpcService;
import com.rpc.service.service.RpcServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RpcServiceImplUnitTest {

    private RpcService rpcService;
    List<ItemData> itemData ;
    List<AggregatedByLocationData> expectedDataList ;


    @Before
    public void setup(){
        rpcService = new RpcServiceImpl();
        itemData = new ArrayList<>();
        itemData.add(new ItemData("id_sample1","numId1","Сочи","as1","None","asd33"));
        itemData.add(new ItemData("id_sample2","numId2","3245.456","as2","Nan","asd2s34"));
        itemData.add(new ItemData("id_sample3","numId3","New York","as3","None","asd35d"));
        expectedDataList = new ArrayList<>();
        expectedDataList.add(new AggregatedByLocationData("Сочи",1L,0L));
        expectedDataList.add(new AggregatedByLocationData("3245.456",0L,1L));
        expectedDataList.add(new AggregatedByLocationData("New York",1L,0L));
    }

    @Test
    public void countWorkingSensorsTest(){

        List<AggregatedByLocationData> result = rpcService.countWorkingSensors(itemData);

        assertTrue(expectedDataList.size() == result.size() && expectedDataList.containsAll(result) &&  result.containsAll(expectedDataList));
    }

    @Test
    public void wrappedCountWorkingSensorsTest(){

        DataWrapper<AggregatedByLocationData> dataWrapper = rpcService.wrappedCountWorkingSensors(itemData);
        assertTrue(expectedDataList.size() == dataWrapper.getData().size() && expectedDataList.containsAll(dataWrapper.getData()) &&  dataWrapper.getData().containsAll(expectedDataList));

    }

}
