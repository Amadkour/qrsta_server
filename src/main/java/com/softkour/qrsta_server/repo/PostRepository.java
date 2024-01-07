package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    public List<Post> findBySessionId(long sessionId);

    public List<Post> findByCourseId(long courseId);
}
