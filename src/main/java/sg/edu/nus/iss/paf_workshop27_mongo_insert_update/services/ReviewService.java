package sg.edu.nus.iss.paf_workshop27_mongo_insert_update.services;

import java.io.StringReader;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.Comment;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.Payload;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.Review;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.ReviewHistoryResponse;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.ReviewResponse;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.repositories.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepo;

    // call endpoint from workshop26
    public Optional<String> findGameById(Integer gid){

        // generate the url
        String URL = "http://localhost:8080/games/";
        String url = UriComponentsBuilder.fromUriString(URL).path(Integer.toString(gid)).toUriString();

        // generate the request ( RequestEntity.get(url)  or RequestEntity.post(url) )
        RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();


        // generate new rest template
        RestTemplate template = new RestTemplate();

        // send the request across & get the response
        // read the response code
        // read the response body
        ResponseEntity<String> resp = null;
        String payload;
        Integer statusCode;

        try{
            resp = template.exchange(req, String.class);  // Json response can be read by String.class
            payload = resp.getBody(); // payload is a json game
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject game = reader.readObject();
            String game_name = game.getString("name");
            return Optional.of(game_name);

        }catch(HttpClientErrorException ex){
            payload = ex.getResponseBodyAsString();
            statusCode = ex.getStatusCode().value();
            if(statusCode == 404){
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    public void insertReview(Payload payload, String gameName){

        Review review = new Review(payload,gameName);
        reviewRepo.insertReview(review);

    }

    public Optional<ReviewResponse> findReviewById(String review_id){

         Optional<Document> doc = reviewRepo.findReviewById(review_id);
         if(doc.isPresent()){
            ReviewResponse response = ReviewResponse.docToReviewResponse(doc.get());
            System.out.println(response); // debug
            return Optional.of(response);
         }else{
            return Optional.empty();
         }

    }

    public Optional<ReviewHistoryResponse> findReviewHistoryById(String review_id){

        Optional<Document> doc = reviewRepo.findReviewById(review_id);
        if(doc.isPresent()){
           ReviewHistoryResponse response = ReviewHistoryResponse.docToReviewHistoryResponse(doc.get());
           System.out.println(response); // debug
           return Optional.of(response);
        }else{
           return Optional.empty();
        }

   }

    public void updateComment(String review_id, Comment comment) throws Exception{

        // check review id is valid
        Optional<Document> reviewFound = reviewRepo.findReviewById(review_id);

        if(reviewFound.isEmpty()){
            throw new Exception();
        }

        reviewRepo.updateComment(review_id, comment);

    }

}
    

