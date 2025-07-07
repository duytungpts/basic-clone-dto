package vn.com.clone_dto.dto;


import vn.com.clone_dto.other_dto.Customer2Dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomerDto {
    private Long id;
    private String username;
    private String email;
    private List<Customer2Dto>  customer2;
    private Set<String> customer3;
    private Map<String,String> customer4;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Customer2Dto> getCustomer2() {
        return customer2;
    }

    public void setCustomer2(List<Customer2Dto> customer2) {
        this.customer2 = customer2;
    }

    public Set<String> getCustomer3() {
        return customer3;
    }

    public void setCustomer3(Set<String> customer3) {
        this.customer3 = customer3;
    }

    public Map<String, String> getCustomer4() {
        return customer4;
    }

    public void setCustomer4(Map<String, String> customer4) {
        this.customer4 = customer4;
    }
}

