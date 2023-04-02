package sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models;

import java.time.LocalDateTime;
import java.util.Date;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Comment {
    
    private String comment;
    private Integer rating;
    private LocalDateTime posted;

    public Comment() {
       posted = LocalDateTime.now();
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public LocalDateTime getPosted() {
        return posted;
    }
    public void setPosted(LocalDateTime posted) {
        this.posted = posted;
    }
    @Override
    public String toString() {
        return "Comment [comment=" + comment + ", rating=" + rating + ", posted=" + posted + "]";
    }

    public Document toDocument(){

        Document doc = new Document();
        doc.put("comment", comment);
        doc.put("rating", rating);
        doc.put("posted", posted);
        return doc;
    }

    public static Comment toComment(Document doc){
        Comment comment = new Comment();
        comment.setComment(doc.getString("comment"));
        comment.setRating(doc.getInteger("rating"));
        Date date = (Date) doc.get("posted");
        comment.setPosted(new java.sql.Timestamp(date.getTime()).toLocalDateTime()); // convert util.Date to LocalDateTime
        return comment;
    }
    
    public JsonObject toJson(){
        JsonObject json = Json.createObjectBuilder()
                                .add("comment",comment)
                                .add("rating",rating)
                                .add("posted",posted.toString())
                                .build();
        return json;
    }
}
