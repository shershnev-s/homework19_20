package by.tut.shershnev_s.service;

import by.tut.shershnev_s.repository.model.User;
import by.tut.shershnev_s.service.model.UserDTO;

public interface UserService {

    User loadUserByUsername(String username);

}
