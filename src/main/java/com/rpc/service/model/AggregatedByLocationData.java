package com.rpc.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedByLocationData {

    private String location;
    private Long numberOfOkSensors;
    private Long numberOfBrokenSensors;

}
