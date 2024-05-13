package tw.com.ispan.datainjection.contoller;


import com.github.houbb.opencc4j.util.ZhConverterUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tw.com.ispan.datainjection.dto.ActorData;
import tw.com.ispan.datainjection.service.ActorService;

import java.io.IOException;

@RestController
@CrossOrigin
public class DataInsertController {
    @Autowired
    private ActorService actorService;

    @GetMapping("/fetchActors/{startId}/{endId}")
    public String fetchAndProcessActors(@PathVariable int startId, @PathVariable int endId) {
        for (int actorId = startId; actorId <= endId; actorId++) {
            ActorData actorData = actorService.fetchAndPrepareActorData(actorId);
            if (actorData != null) {
                actorService.sendActorDataToAnotherServer(actorData);
            } else {
                System.out.println("Skipping actor ID: " + actorId);
            }
        }
        return "Process completed from ID " + startId + " to ID " + endId;
    }

}
