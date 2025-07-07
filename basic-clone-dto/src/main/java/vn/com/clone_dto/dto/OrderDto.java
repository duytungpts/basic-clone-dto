package vn.com.clone_dto.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private List<Long> productIds;
    private Double totalAmount;
}

