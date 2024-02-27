package com.example.cloudsimpluswebapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DatacenterBrokerDTO {
    private int brokerCount;
}
