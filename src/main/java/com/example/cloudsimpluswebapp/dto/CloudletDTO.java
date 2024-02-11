package com.example.cloudsimpluswebapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CloudletDTO {
    //TODO Сделать валидацию
    private int cloudletsCount;
    private int cloudletPes;
    private int cloudletLength;
}
