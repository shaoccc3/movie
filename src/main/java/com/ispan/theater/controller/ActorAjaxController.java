package com.ispan.theater.controller;

import com.ispan.theater.domain.Actor;
import com.ispan.theater.service.ActorService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin
public class ActorAjaxController {
    @Autowired
    private ActorService actorService;

    @GetMapping("/backstage/act")
    public String actList() {
        System.out.println("TESTTESTTEST");
        List<Actor> actors = actorService.findAll();
        JSONArray jsonArray = new JSONArray();
        for (Actor actor : actors) {
            jsonArray.put(actor);
        }
        return jsonArray.toString();
    }
    @PostMapping("/backstage/act")
    public String actInsert(@RequestBody String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject response = new JSONObject();
        Actor actor = actorService.findByName(jsonObject.getString("name"));
        if (actor == null) {
            actorService.insert(jsonString);
            response.put("msg", "更新成功");
            response.put("succeed","succeed");
        }
        else{
            response.put("msg", "更新失敗");
            response.put("fail","fail");
        }
        return response.toString();
    }
}
