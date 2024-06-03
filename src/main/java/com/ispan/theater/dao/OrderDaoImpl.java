package com.ispan.theater.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Repository
@Transactional(readOnly = true)
public class OrderDaoImpl implements OrderDao {

    @PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Map<String, String>> multiConditionFindMovie(Map<String, String> requestParams) {
		//order by o.create_date desc offset :page rows fetch next 10 rows only 
		AtomicReference<String> queryString = new AtomicReference<>("select ROW_NUMBER() over(order by o.create_date desc) as 'no',o.order_id,u.email,m.name,SUBSTRING(convert(varchar(19),o.create_date),1,19) as create_date,o.order_amount,o.supplier,o.order_status FROM \"Order\" as o join movie as m on o.movie_id=m.movie_id join \"user\" as u on u.user_id=o.user_id WHERE 1=1");
        requestParams.entrySet().forEach(entry->queryString.set(queryString.get()+" and "+entry.getKey()+" = :"+entry.getKey()));
        System.out.println(queryString.get());//test
		Query query = entityManager.createNativeQuery(queryString.get(), Map.class);
        requestParams.entrySet().forEach(entry-> query.setParameter(entry.getKey(), entry.getValue()));
        System.out.println(query.getResultList());//test
		return null;
	}

}
