package tw.com.ispan.datainjection.dto;



import java.util.List;


public class ActorData {
    private int id;
    private String name;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private String photo;
    public ActorData() {}

    public ActorData(int id, String name ) {
        this.id = id;
        this.name = name;
    }


    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
