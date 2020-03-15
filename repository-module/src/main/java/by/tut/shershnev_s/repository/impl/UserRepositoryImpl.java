package by.tut.shershnev_s.repository.impl;

import by.tut.shershnev_s.repository.UserRepository;
import by.tut.shershnev_s.repository.model.RoleEnum;
import by.tut.shershnev_s.repository.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User getUserByName(Connection connection, String username) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, username, password, role FROM user WHERE username=?; "
                )
        ) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = getUsers(rs);
                    return user;
                }
            }
        }
        return null;
    }

    private User getUsers(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        String role = rs.getString("role");
        RoleEnum roleEnum = RoleEnum.valueOf(role);
        user.setRole(roleEnum);
        return user;
    }

}
