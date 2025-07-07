package vn.com.clone_dto.req_clone_dto;

import lombok.Getter;
import java.util.List;

@Getter
public class OrderDtoReq {

    private Long id;
    private Long userId;
    private List<Long> productIds;
    private Double totalAmount;
}
