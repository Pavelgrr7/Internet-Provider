package com.pavelryzh.provider.dto.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteContractResponseDto {
    private boolean subscriberDeleted;
    private Long deletedSubscriberId; // ID удаленного абонента, чтобы фронтенд знал, кого убрать из списка
}
