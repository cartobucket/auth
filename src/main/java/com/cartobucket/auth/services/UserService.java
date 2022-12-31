package com.cartobucket.auth.services;

import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.User;

public interface UserService {
    User createUser(User user, Profile profile, String password);
}
