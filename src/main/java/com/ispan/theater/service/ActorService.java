package com.ispan.theater.service;

import com.ispan.theater.domain.Actor;
import com.ispan.theater.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;

    public Actor findById(Integer id) {
        return actorRepository.findById(id).get();
    }
    public void insert(String name) {
        Actor actor = new Actor();
        actor.setName(name);
        actorRepository.save(actor);
    }
    public void delete(Integer id) {
        actorRepository.deleteById(id);
    }
    public List<Actor> findAll() {
        return actorRepository.findAll();
    }
    public List<Actor> findByName(String name) {
        return actorRepository.findByName(name);
    }
    public Actor updateActorName(Integer id){
        Actor actor = actorRepository.findById(id).get();
        actor.setName(actor.getName());
        return actor;
    }
    public void insertPhoto(Integer id,Byte[] photo){
        Actor actor = actorRepository.findById(id).get();
        

    }

}
