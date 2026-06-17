package com.usernotification.api.service;

import com.usernotification.api.payload.request.UserRequest;
import com.usernotification.api.payload.respose.UserResponse;

public interface UserService {

	UserResponse createUser(UserRequest request);

	UserResponse getUser(Long id);

}
