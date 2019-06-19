package com.rpc.service.service;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import com.rpc.service.model.DataWrapper;
import com.rpc.service.model.ItemData;
import com.rpc.service.model.AggregatedByLocationData;
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
        Map<String, Long> okSensors = new HashMap<>();
        Map<String, Long> brokenSensors = new HashMap<>();
        //Map data we need
        for(ItemData item: data){
            if (item.getId_detected().equals("None")) okSensors.merge(item.getId_location(), 1L, (oldVal, newVal) -> oldVal + newVal);
            if (item.getId_detected().equals("Nan")) brokenSensors.merge(item.getId_location(), 1L, (oldVal, newVal) -> oldVal + newVal);
        }

        List<AggregatedByLocationData> list = new ArrayList<>();
        //first create all obj that exists in both maps
        for (String ok : okSensors.keySet()) {
            if (brokenSensors.containsKey(ok)) {
                AggregatedByLocationData aggregatedByLocationData = new AggregatedByLocationData(ok,brokenSensors.get(ok), okSensors.get(ok));
                list.add(aggregatedByLocationData);
                brokenSensors.remove(ok);
            } else {
                AggregatedByLocationData aggregatedByLocationData = new AggregatedByLocationData(ok, okSensors.get(ok), 0L);
                list.add(aggregatedByLocationData);
            }
        }
        //Add what left
        for(String br: brokenSensors.keySet()){
            AggregatedByLocationData aggregatedByLocationData = new AggregatedByLocationData(br, 0L, brokenSensors.get(br));
            list.add(aggregatedByLocationData);
        }

        return list;
    }

    @Override
    public DataWrapper<AggregatedByLocationData> wrappedCountWorkingSensors(@JsonRpcParam("data") List<ItemData> data) {
        List<AggregatedByLocationData> list = countWorkingSensors(data);
        DataWrapper<AggregatedByLocationData> wrapper = new DataWrapper<>(list);
        return wrapper;
    }


}
