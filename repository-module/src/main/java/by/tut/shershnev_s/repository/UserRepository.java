package by.tut.shershnev_s.repository;

import by.tut.shershnev_s.repository.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserRepository {

    User getUserByName(Connection connection, String username) throws SQLException;
}
