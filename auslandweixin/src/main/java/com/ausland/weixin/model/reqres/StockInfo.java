package com.ausland.weixin.model.reqres;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StockInfo {
    String errorDetails;
    String productId;
    String productName;
    String subProductFeature;
}
