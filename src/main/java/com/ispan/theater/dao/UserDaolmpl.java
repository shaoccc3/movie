package com.ispan.theater.dao;

import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.User;

import jakarta.persistence.PersistenceContext;

@Repository
public class UserDaolmpl implements UserDao {
	@PersistenceContext
	private Session session;

	public Session getSession() {
		return this.session;
	}
//	@Override
//	public List<User> find (JSONObject json){
//		return null;
//	}
}
