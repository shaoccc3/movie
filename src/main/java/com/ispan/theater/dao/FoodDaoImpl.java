package com.ispan.theater.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Food;
import com.ispan.theater.util.DatetimeConverter;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class FoodDaoImpl implements FoodDao {
	@PersistenceContext
	private Session session;
	public Session getSession() {
		return this.session;
	}
//	public void setSession(Session session) {
//		this.session = session;
//	}

	@Override
	public List<Food> find(JSONObject obj) {
		Integer id = obj.isNull("id") ? null : obj.getInt("id");
		String name = obj.isNull("name") ? null : obj.getString("name");
		String ename = obj.isNull("name_eng") ? null : obj.getString("name_eng");
		Double startPrice = obj.isNull("startPrice") ? null : obj.getDouble("startPrice");	
		Double endPrice = obj.isNull("endPrice") ? null : obj.getDouble("endPrice");	
		String startMake = obj.isNull("startMake") ? null : obj.getString("startMake");	
		String endMake = obj.isNull("endMake") ? null : obj.getString("endMake");	
		String startExpire = obj.isNull("startExpire") ? null : obj.getString("startExpire");	
		String endExpire = obj.isNull("endExpire") ? null : obj.getString("endExpire");	
		String startCreateDate = obj.isNull("startCreateDate") ? null : obj.getString("startCreateDate");	
		String endCreateDate = obj.isNull("endCreateDate") ? null : obj.getString("endCreateDate");	
		String startModifyDate = obj.isNull("startModifyDate") ? null : obj.getString("startModifyDate");	
		String endModifyDate = obj.isNull("endModifyDate") ? null : obj.getString("endModifyDate");	
		
		int start = obj.isNull("start") ? 0 : obj.getInt("start");
		int rows = obj.isNull("rows") ? 10 : obj.getInt("rows");
		String order = obj.isNull("order") ? "id" : obj.getString("order");
		boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");
		
		CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Food> criteriaQuery = criteriaBuilder.createQuery(Food.class);
		
		//from Food
		Root<Food> table = criteriaQuery.from(Food.class);
		
		//where start
		List<Predicate> predicates = new ArrayList<>();
		if (id!=null) {
			Predicate p = criteriaBuilder.equal(table.get("id"), id);
			predicates.add(p);
		}
		if (name!=null && name.length() != 0) {
			predicates.add(criteriaBuilder.like(table.get("name"), "%"+name+"%"));
		}
		if (name!=null && name.length() != 0) {
			predicates.add(criteriaBuilder.like(table.get("name_eng"), "%"+ename+"%"));
		}
		if (startPrice!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startPrice"), startPrice));
		}
		if (endPrice!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endPrice"), endPrice));
		}
		if (startMake!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startMake"), startMake));
		}
		if (endMake!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endMake"), endMake));
		}
		if (startExpire!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startExpire"), startExpire));
		}
		if (endExpire!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endExpire"), endExpire));
		}
		if (startCreateDate!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startCreateDate"), startCreateDate));
		}
		if (endCreateDate!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endCreateDate"), endCreateDate));
		}
		if (startModifyDate!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startModifyDate"), startModifyDate));
		}
		if (endModifyDate!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endModifyDate"), endModifyDate));
		}
		
		if(predicates!=null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQuery = criteriaQuery.where(array);
		}
		//where end
		
		//order by
		if (dir) {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.desc(table.get(order)));
		}else {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.asc(table.get(order)));
		}
		
		TypedQuery<Food> typedQuery = this.getSession().createQuery(criteriaQuery)
				.setFirstResult(start)
				.setMaxResults(rows);
		
		List<Food> result = typedQuery.getResultList();
		if(result!=null && !result.isEmpty()) {
			return result;
		} else {
			return null;
		}
		
	}
	
	@Override
	public long count(JSONObject obj) {
		Integer id = obj.isNull("id") ? null : obj.getInt("id");
		String name = obj.isNull("name") ? null : obj.getString("name");
		Double startPrice = obj.isNull("startPrice") ? null : obj.getDouble("startPrice") ;
		Double endPrice = obj.isNull("endPrice") ? null : obj.getDouble("endPrice") ;
		String startMake = obj.isNull("startMake") ? null : obj.getString("startMake");
		String endMake = obj.isNull("endMake") ? null : obj.getString("endMake");
		Integer startExpire = obj.isNull("startExpire") ? null : obj.getInt("startExpire");
		Integer endExpire = obj.isNull("endExpire") ? null : obj.getInt("endExpire");
		
		CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

//		from product
		Root<Food> table = criteriaQuery.from(Food.class);

//		select count(*)
		criteriaQuery = criteriaQuery.select(criteriaBuilder.count(table));
		
//		where start
		List<Predicate> predicates = new ArrayList<>();
		if(id!=null) {
			Predicate p = criteriaBuilder.equal(table.get("id"), id);
			predicates.add(p);
		}
		if(name!=null && name.length()!=0) {
			predicates.add(criteriaBuilder.like(table.get("name"), "%"+name+"%"));
		}
		if(startPrice!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("price"), startPrice));
		}
		if(endPrice!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("price"), endPrice));
		}
		if(startMake!=null && startMake.length()!=0) {
			java.util.Date temp = DatetimeConverter.parse(startMake, "yyyy-MM-dd");
			predicates.add(criteriaBuilder.greaterThan(table.get("make"), temp));
		}
		if(endMake!=null && endMake.length()!=0) {
			java.util.Date temp = DatetimeConverter.parse(endMake, "yyyy-MM-dd");
			predicates.add(criteriaBuilder.lessThan(table.get("make"), temp));
		}
		if(startExpire!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("expire"), startExpire));
		}
		if(endExpire!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("expire"), endExpire));
		}
		
		if(predicates!=null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQuery = criteriaQuery.where(array);
		}
//		where end
		
		TypedQuery<Long> typedQuery = this.getSession().createQuery(criteriaQuery);
		Long result = typedQuery.getSingleResult();
		if(result!=null) {
			return result.longValue();
		} else {
			return 0;
		}
	}

}
