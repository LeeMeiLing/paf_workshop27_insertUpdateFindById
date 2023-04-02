package sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ReviewResponse {
    /*
     *  user: <name form field>,
        rating: <latest rating>,
        comment: <latest comment>,
        ID: <game id form field>,
        posted: <date>,
        name: <The board game’s name as per ID>,
        edited: <true or false depending on edits>,
        timestamp: <result timestamp>
     */
    private String user;
    private Integer rating;
    private String comment;
    private Integer id;
    private LocalDateTime posted;
    private String name;
    private Boolean edited;
    private Timestamp timestamp;

    public ReviewResponse() {
        timestamp=Timestamp.from(Instant.now());
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
    public Boolean getEdited() {
        return edited;
    }
    public void setEdited(Boolean edited) {
        this.edited = edited;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "ReviewResponse [user=" + user + ", rating=" + rating + ", comment=" + comment + ", id=" + id
                + ", posted=" + posted + ", name=" + name + ", edited=" + edited + ", timestamp=" + timestamp + "]";
    }

    /*
     * {
    "_id" : ObjectId("6429239efdf8b33a67a274eb"),
    "user" : "Fred",
    "rating" : NumberInt(5),
    "comment" : "",
    "ID" : NumberInt(8),
    "posted" : ISODate("2023-04-02T06:41:34.431+0000"),
    "name" : "Lords of Creation",
    "edited" : [
        {
            "comment" : "fun and thrilling",
            "rating" : NumberInt(9),
            "posted" : ISODate("2023-04-02T11:03:41.273+0000")
        },
        {
            "comment" : "fun and thrilling",
            "rating" : NumberInt(9),
            "posted" : ISODate("2023-04-02T11:06:56.731+0000")
        }
    ]
}
     */

    public static ReviewResponse docToReviewResponse(Document doc){

        ReviewResponse response = new ReviewResponse();
        response.setUser(doc.getString("user"));
        response.setId(doc.getInteger("ID"));
        // System.out.println(doc.get("posted").getClass()); // output: java.Util.Date
        Date date = (Date) doc.get("posted");
        response.setPosted(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()); // convert util.Date to LocalDateTime
        response.setName(doc.getString("name"));
        
        // check edited attribute exists or not
        try{
            List<Document> editedList = doc.getList("edited", Document.class);
            System.out.println("editedList = " + editedList); // debug
            int size = editedList.size();
            if(size > 0){
                response.setEdited(true);
                // get the last item in the list
                Document lastCommentDoc = editedList.get((size-1));
                Comment lastComment = Comment.toComment(lastCommentDoc);
                response.setRating(lastComment.getRating()); // get latest rating
                response.setComment(lastComment.getComment()); // check if comment is null or "", use try catch to set as null
            }else{
                response.setEdited(false);
                response.setRating(doc.getInteger("rating")); 
                response.setComment(doc.getString("comment"));
            }

        }catch(Exception ex){

            response.setEdited(false);
            response.setRating(doc.getInteger("rating")); 
            try{
                response.setComment(doc.getString("comment")); 
            }catch(Exception exception){
                response.setComment(null);
            }
           

        }
        
        return response;
    }

    /*
     *  user: <name form field>,
        rating: <latest rating>,
        comment: <latest comment>,
        ID: <game id form field>,
        posted: <date>,
        name: <The board game’s name as per ID>,
        edited: <true or false depending on edits>,
        timestamp: <result timestamp>
     */
    public JsonObject toJson(){

        JsonObject json = Json.createObjectBuilder()
                                .add("user",user)
                                .add("rating", rating)
                                .add("comment",comment) // value cannot be null, already have validation in place
                                .add("ID",id)
                                .add("posted",posted.toString())
                                .add("name",name)
                                .add("edited",edited)
                                .add("timestamp",timestamp.toString())
                                .build();
        return json;
    }
    
    
}
