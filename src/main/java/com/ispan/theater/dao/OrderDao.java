package com.ispan.theater.dao;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    List<Map<String,String>> multiConditionFindMovie(Map<String, String> requestParams);

}
