package com.bookmanagement.service;

import com.bookmanagement.dto.UserInformationResponse;
import com.bookmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInformationResponse getInformation(String userId) {
        return userRepository.findById(Long.parseLong(userId))
                .map(user -> {
                    UserInformationResponse response = new UserInformationResponse();
                    response.setDOb(user.getDateOfBirth());
                    response.setName(user.getFirstName() + " " + user.getLastName());
                    response.setSex(user.getSex());
                    response.setEmail(user.getEmail());
                    response.setPhoneNumber(user.getPhoneNumber());
                    return response;
                })
                .orElse(null);
    }
}
