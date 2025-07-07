package vn.com.clone_dto.req_clone_dto;

import lombok.Getter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import vn.com.clone_dto.other_dto.Customer2Dto;

@Getter
public class CustomerDtoReq {

    private Long id;
    private String username;
    private String email;
    private List<Customer2Dto> customer2;
    private Set<String> customer3;
    private Map<String, String> customer4;
}
