package sg.edu.nus.iss.paf_workshop27_mongo_insert_update.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.Comment;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.Payload;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.ReviewHistoryResponse;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.ReviewResponse;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.services.ReviewService;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewSvc;
    
    // POST /review
    // Content-Type: application/x-www-form-urlencoded
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> processReviewForm(@Valid @ModelAttribute Payload payload, BindingResult result){

        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }

        // check for valid game id
        Optional<String> gameName = reviewSvc.findGameById(payload.getGameId());
        if(gameName.isEmpty()){
            ObjectError err = new ObjectError("invalidGameId", "Game Id not found");
            result.addError(err);
            return ResponseEntity.badRequest().body(result.toString());
        }

        // if gid is valid, proceed to insert review
        reviewSvc.insertReview(payload, gameName.get());

        return ResponseEntity.ok().body("Inserted");
    }

    // PUT /review/<review_id>
    // Content-Type: application/json
    @PutMapping(path="/{review_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> processUpdateComment(@PathVariable String review_id, @RequestBody Comment comment){

        try{
            reviewSvc.updateComment(review_id,comment);
            return ResponseEntity.ok().body("Comment updated");
        }catch(Exception ex){
            // svc throw error if review_id not found
            return ResponseEntity.badRequest().body("Review Id not found");
        }

    }

    // GET /review/<review_id>
    // Accept: application/json
    @GetMapping(path = "/{review_id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReview(@PathVariable String review_id){

        try{
            Optional<ReviewResponse> resp = reviewSvc.findReviewById(review_id);
            if(resp.isPresent()){
                return ResponseEntity.ok().body(resp.get().toJson().toString());
            }else{
                return ResponseEntity.badRequest().body("Review Id not found");
            }
        }catch(Exception ex){
            return ResponseEntity.badRequest().body("No result found");
        }
    }

    
    // GET /review/<review_id>/history
    // Accept: application/json
    @GetMapping(path = "/{review_id}/history", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReviewHistory(@PathVariable String review_id){

        try{
            Optional<ReviewHistoryResponse> resp = reviewSvc.findReviewHistoryById(review_id);
            if(resp.isPresent()){
                return ResponseEntity.ok().body(resp.get().toJson().toString());
            }else{
                return ResponseEntity.badRequest().body("Review Id not found");
            }
        }catch(Exception ex){
            return ResponseEntity.badRequest().body("No result found");
        }
    }

}
