package com.pavelryzh.provider.dto.user.subscriber;

import lombok.Data;

@Data
public class SubscriberListItemDto {
    private Long id;
    private String fullName;
    private String login;
    private String email;

}
