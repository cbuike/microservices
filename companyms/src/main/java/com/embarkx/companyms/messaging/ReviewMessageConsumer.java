package com.embarkx.companyms.messaging;


import com.embarkx.companyms.CompanyService;
import com.embarkx.companyms.dto.ReviewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMessageConsumer {

    private final CompanyService companyService;

    @RabbitListener(queues = "companyRatingQueue")
    public void receiveMessage(ReviewMessage message) {
        companyService.updateCompanyRating(message);
    }
}
