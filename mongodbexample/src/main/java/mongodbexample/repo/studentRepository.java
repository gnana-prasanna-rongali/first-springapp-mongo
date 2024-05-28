package mongodbexample.repo;

import mongodbexample.models.student;
import mongodbexample.response.ApiResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface studentRepository extends MongoRepository<student,Integer> {
    void deleteById(int id);

    default Optional<student> findById(int id) {
        return null;
    }

    Optional<student> findById(ResponseEntity<ApiResponse<student>> studentById);
}
