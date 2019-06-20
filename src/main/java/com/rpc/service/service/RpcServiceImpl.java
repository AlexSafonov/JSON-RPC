package com.rpc.service.service;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import com.rpc.service.model.AggregatedByLocationData;
import com.rpc.service.model.DataWrapper;
import com.rpc.service.model.ItemData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AutoJsonRpcServiceImpl
public class RpcServiceImpl implements RpcService{

    //Aggregate
    @Override
    public List<AggregatedByLocationData> countWorkingSensors(@JsonRpcParam("data") List<ItemData> data)  {
        Map<String, Long> numOfOkSensors = new HashMap<>();
        Map<String, Long> numOfBrokenSensors = new HashMap<>();
                for(ItemData item: data){
            if (item.getId_detected().equals("None")) numOfOkSensors.merge(item.getId_location(), 1L, (oldVal, newVal) -> oldVal + newVal);
            if (item.getId_detected().equals("Nan")) numOfBrokenSensors.merge(item.getId_location(), 1L, (oldVal, newVal) -> oldVal + newVal);
        }

        List<AggregatedByLocationData> list = new ArrayList<>();
        //first, create all obj that has both broken adn working sensors
        for (String ok : numOfOkSensors.keySet()) {
            if (numOfBrokenSensors.containsKey(ok)) {
                AggregatedByLocationData aggregatedByLocationData = new AggregatedByLocationData(ok,numOfBrokenSensors.get(ok), numOfOkSensors.get(ok));
                list.add(aggregatedByLocationData);
                numOfBrokenSensors.remove(ok);
            } else {
                AggregatedByLocationData aggregatedByLocationData = new AggregatedByLocationData(ok, numOfOkSensors.get(ok), 0L);
                list.add(aggregatedByLocationData);
            }
        }
        //then what left
        for(String br: numOfBrokenSensors.keySet()){
            AggregatedByLocationData aggregatedByLocationData = new AggregatedByLocationData(br, 0L, numOfBrokenSensors.get(br));
            list.add(aggregatedByLocationData);
        }

        return list;
    }

    @Override
    public DataWrapper<AggregatedByLocationData> wrappedCountWorkingSensors(@JsonRpcParam("data") List<ItemData> data) {
        List<AggregatedByLocationData> list = countWorkingSensors(data);
        return new DataWrapper<>(list);
    }


}
