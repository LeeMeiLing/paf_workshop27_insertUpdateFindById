package sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Review {
    /*
     *  user: <name form field>,
        rating: <rating form field>,
        comment: <comment form field>,
        ID: <game id form field>,
        posted: <date>,
        name: <The board game’s name as per ID>
     */

    private String user;
    private Integer rating;
    private String comment;
    private Integer id;
    private LocalDateTime posted;
    private String name;
    private List<Comment> edited = new ArrayList<>();

    public Review() {
    }

    public Review(Payload payload, String gameName) {
        user = payload.getName();
        rating = payload.getRating();
        comment = payload.getComment(); // try null
        id = payload.getGameId();
        posted=LocalDateTime.now();
        name = gameName;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getPosted() {
        return posted;
    }
    public void setPosted(LocalDateTime posted) {
        this.posted = posted;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
   
    public List<Comment> getEdited() {
        return edited;
    }

    public void setEdited(List<Comment> edited) {
        this.edited = edited;
    }

    @Override
    public String toString() {
        return "Review [user=" + user + ", rating=" + rating + ", comment=" + comment + ", id=" + id + ", posted="
                + posted + ", name=" + name + ", edited=" + edited + "]";
    }

    public Document toNewDocument(){
        
        Document doc = new Document();
        doc.put("user", user);
        doc.put("rating", rating);
        doc.put("comment", comment);
        doc.put("ID", id);
        doc.put("posted", posted);
        doc.put("name", name);
        return doc;

    }

    
}
