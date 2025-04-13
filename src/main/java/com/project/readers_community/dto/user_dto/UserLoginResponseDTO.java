package com.project.readers_community.dto.user_dto;


import com.project.readers_community.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {

    private String username;
    private String email;
    private List<Role> roles;


}