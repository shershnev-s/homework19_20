package by.tut.shershnev_s.repository;

import by.tut.shershnev_s.repository.model.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemRepository {

    List<Item> findAll(Connection connection) throws SQLException;

    List<Item> findItemsByStatus(Connection connection, String status) throws SQLException;

    Item add(Connection connection, Item item) throws SQLException;

    void updateById(Connection connection, Item item) throws SQLException;

    void deleteItemByStatus(Connection connection, String status) throws SQLException;
}
