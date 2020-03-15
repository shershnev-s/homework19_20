package by.tut.shershnev_s.repository.impl;

import by.tut.shershnev_s.repository.ItemRepository;
import by.tut.shershnev_s.repository.model.Item;
import by.tut.shershnev_s.repository.model.StatusEnum;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @Override
    public List<Item> findAll(Connection connection) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, name, status FROM item;"
                )
        ) {
            List<Item> items = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Item item = getItems(rs);
                    items.add(item);
                }
                return items;
            }
        }
    }

    @Override
    public List<Item> findItemsByStatus(Connection connection, String status) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, name, status FROM item WHERE status=?;"
                )
        ) {
            statement.setString(1, status);
            List<Item> items = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Item item = getItems(rs);
                    items.add(item);
                }
                return items;
            }
        }
    }

    @Override
    public Item add(Connection connection, Item item) throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO item(name, status) VALUES(?,?)"
                        , Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, item.getName());
            String status = item.getStatus().name();
            preparedStatement.setString(2, status);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating item failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }
            return item;
        }
    }

    @Override
    public void updateById(Connection connection, Item item) throws SQLException {
        int affectedRows = 0;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE item SET status=? WHERE id=?;"
                )
        ) {
            String status = item.getStatus().name();
            statement.setString(1, status);
            statement.setLong(2, item.getId());
            affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating item failed, no rows affected.");
            }
        }
    }

    @Override
    public void deleteItemByStatus(Connection connection, String status) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM item WHERE status=?;"
                )
        ) {
            statement.setString(1, status);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting item failed, no rows affected.");
            }
        }
    }

    private Item getItems(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setName(rs.getString("name"));
        String status = rs.getString("status");
        StatusEnum statusEnum = StatusEnum.valueOf(status);
        item.setStatus(statusEnum);
        return item;
    }
}

