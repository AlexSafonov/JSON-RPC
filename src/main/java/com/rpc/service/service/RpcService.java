package com.rpc.service.service;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.rpc.service.model.DataWrapper;
import com.rpc.service.model.ItemData;
import com.rpc.service.model.AggregatedByLocationData;

import java.util.List;

@JsonRpcService("/service")
public interface RpcService {

    List<AggregatedByLocationData> countWorkingSensors(@JsonRpcParam("data") List<ItemData> data);
    DataWrapper<AggregatedByLocationData> wrappedCountWorkingSensors(@JsonRpcParam("data") List<ItemData> data);
}
