package com.gq.crm.service;


import com.gq.base.BaseService;
import com.gq.crm.entity.SaleChance;
import com.gq.crm.query.SaleChanceQuery;

import java.util.Map;

/**
 * @date 2023/6/13
 */


public  interface SaleChanceService  {

   /**
    *
    * @param saleChanceQuery
    * @return
    */
   Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery);

   /**
    * 添加
    * 效验参数的合法性
    * @param saleChance
    */
   void saveSaleChance(SaleChance saleChance);

   void updateSaleChance(SaleChance saleChance);

   SaleChance selectByPrimaryKey(Integer id);

    void deleteSaleChance(Integer[] ids);

    void updateSaleChanceDevResultVo(Integer id, Integer devResult);
}
