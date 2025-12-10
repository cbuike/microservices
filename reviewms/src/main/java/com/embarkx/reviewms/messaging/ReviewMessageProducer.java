package com.embarkx.reviewms.messaging;

import com.embarkx.reviewms.Review;
import com.embarkx.reviewms.dto.ReviewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Review review) {
        var reviewMessage = ReviewMessage.builder()
                .id(review.getId())
                .title(review.getTitle())
                .description(review.getDescription())
                .rating(review.getRating())
                .companyId(review.getCompanyId())
                .build();
        rabbitTemplate.convertAndSend("companyRatingQueue", reviewMessage);
    }
}
