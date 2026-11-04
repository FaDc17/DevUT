package com.devut.repository;

import com.devut.model.EventComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCommentRepository extends JpaRepository<EventComment, Long> {
    List<EventComment> findByEventIdOrderByCreatedAtDesc(Long eventId);
}
