package com.ausland.weixin.model.reqres;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZhongHuanFydhDetails {

	  String chrlrsj;//电子下单时间
      String chrfydh;  //分运单号
      String chrbgdh;
      String chrzl;
      String chrshzt;
      String ckdhm;
      String chrsjr;
      String chrsjrdz;
      String chrsjrdh;
      String productItems;
}
  