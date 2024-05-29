package com.ispan.theater.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ispan.theater.domain.SymmetricKeys;
import com.ispan.theater.repository.SymmetricKeysRepository;
import com.ispan.theater.util.SymmetricKeyGeneratorUtil;

@Service
public class SymmetricKeysService  {

	@Autowired
	SymmetricKeysRepository symmetricKeysRepository;
	
	@Scheduled(fixedRate  = 100000)	
	public void createSymmetricKey()throws NoSuchAlgorithmException {
		SymmetricKeys key =new SymmetricKeys();
		
		key.setSecretKey(SymmetricKeyGeneratorUtil.generateBase64SecretKey());
		key.setCreationDate(LocalDateTime.now());
		
		symmetricKeysRepository.save(key);
		System.out.println("對稱鑰產生完成 對稱鑰:"+key.getSecretKey());
	}
	
	public SymmetricKeys getSymmetricKey() {
		return symmetricKeysRepository.findTop();
	}
	
	
}
