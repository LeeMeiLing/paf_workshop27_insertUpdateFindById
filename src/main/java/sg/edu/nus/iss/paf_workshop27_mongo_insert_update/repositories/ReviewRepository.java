package sg.edu.nus.iss.paf_workshop27_mongo_insert_update.repositories;

import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.Comment;
import sg.edu.nus.iss.paf_workshop27_mongo_insert_update.models.Review;

@Repository
public class ReviewRepository {
    
    @Autowired 
    MongoTemplate mongoTemplate;

    /*
     * db.reviews.insert(
        {
            user: "Mei Ling",
            rating: 10,
            comment: "good",
            ID: 1,
            posted: new Date(),
            name: "Die Macher"
        })
     */
    public String insertReview(Review review){

        // create document to be inserted
        Document toInsert = review.toNewDocument();

        // insert the document & get back the document created
        Document insertedDoc = mongoTemplate.insert(toInsert, "reviews");

        // retrieve the document ObjectId
        ObjectId id = insertedDoc.getObjectId("_id");

        System.out.println(insertedDoc.toString()); // debug

        return id.toString();
    }

    /*
     * db.reviews.find(
            { _id: ObjectId("64290b3d9c37e7a7c1e9ab2b")}
        )
     */
    public Optional<Document> findReviewById(String review_id){

        try{
            ObjectId id = new ObjectId(review_id); // will throw error if review ID invalid
            Document result = mongoTemplate.findById(id, Document.class, "reviews");
            return Optional.of(result);
        }catch(Exception ex){
            return Optional.empty();
        }
 
    }

    /*
     * db.reviews.update(
        {
            _id: ObjectId("64290b3d9c37e7a7c1e9ab3a")
        },
        {
            $push: {edited: {comment: "interesting", rating:7, posted: new Date()}}
        }
    )
     */
    public void updateComment(String review_id, Comment comment){

        System.out.println("in repo  updateComment");

        ObjectId id = new ObjectId(review_id);
        Document toUpdate = comment.toDocument();

        // filter query
        Query query = Query.query(
            Criteria.where("_id").is(id)
        );

        // updateOperation
        Update updateOps = new Update().push("edited", toUpdate);

        // perform query and obtain UpdateResult
        UpdateResult result = mongoTemplate.updateFirst(query, updateOps, Document.class ,"reviews");

        System.out.println(">>> updated document = " + result.getModifiedCount());
    
    }


}
