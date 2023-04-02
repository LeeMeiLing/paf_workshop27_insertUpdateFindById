package sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Payload {
    /*
     * All content must include the following form field
         name – name of the person that posted the rating
         rating – between 0 and 10
         comment – this can be optional
         game id – must be a valid game id from the games collection
     */

     @NotBlank(message="Name is mandatory")
     private String name;
     
     @NotNull(message="Rating is mandatory")
     @Min(value = 0, message = "Enter a rating value of 0 to 10")
     @Max(value = 10, message = "Enter a rating value of 0 to 10")
     private Integer rating;

     @NotNull
     private String comment;

     @NotNull(message="Game Id is mandatory")
     private Integer gameId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "Payload [name=" + name + ", rating=" + rating + ", comment=" + comment + ", gameId=" + gameId + "]";
    }

    
  
}
