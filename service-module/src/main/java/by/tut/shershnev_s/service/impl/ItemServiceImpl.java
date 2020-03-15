package by.tut.shershnev_s.service.impl;

import by.tut.shershnev_s.repository.ConnectionRepository;
import by.tut.shershnev_s.repository.ItemRepository;
import by.tut.shershnev_s.repository.model.Item;
import by.tut.shershnev_s.repository.model.RoleEnum;
import by.tut.shershnev_s.repository.model.StatusEnum;
import by.tut.shershnev_s.service.ItemService;
import by.tut.shershnev_s.service.model.ItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ConnectionRepository connectionRepository, ItemRepository itemRepository) {
        this.connectionRepository = connectionRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemDTO> findAll() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String role = auth.getName();
                List<ItemDTO> itemDTOS = getItemsForUserRole(connection, role);
                connection.commit();
                return itemDTOS;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                logger.error("Can't find items");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            logger.error("Can't create connection");
        }
        return Collections.emptyList();
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item item = convertDTOToItem(itemDTO);
                item = itemRepository.add(connection, item);
                connection.commit();
                return convertItemToDTO(item);
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                logger.error("Can't add item");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            logger.error("Can't create connection");
        }
        return null;
    }

    @Override
    public void updateById(ItemDTO itemDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item item = convertDTOToItem(itemDTO);
                itemRepository.updateById(connection, item);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                logger.error("Can't update item");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            logger.error("Can't create connection");
        }
    }

    @Override
    public void deleteByStatus(ItemDTO itemDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String status = itemDTO.getStatus();
                if (status.equals(StatusEnum.COMPLETED.name())) {
                    itemRepository.deleteItemByStatus(connection, status);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                logger.error("Can't delete item");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            logger.error("Can't create connection");
        }
    }

    @Override
    public List<ItemDTO> findAllForApi() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Item> items = itemRepository.findAll(connection);
                List<ItemDTO> itemDTOS = items.stream()
                        .map(this::convertItemToDTO)
                        .collect(Collectors.toList());
                connection.commit();
                return itemDTOS;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                logger.error("Can't find items");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            logger.error("Can't create connection");
        }
        return Collections.emptyList();
    }

    private StatusEnum convertStringIntoEnum(String status) {
        return StatusEnum.valueOf(status);
    }

    private Item convertDTOToItem(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setStatus(convertStringIntoEnum(itemDTO.getStatus()));
        return item;
    }

    private ItemDTO convertItemToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setStatus(item.getStatus().name());
        return itemDTO;
    }

    private List<ItemDTO> getItemsForUserRole(Connection connection, String role) throws SQLException {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        if (role.equals(RoleEnum.ADMIN.name())) {
            List<Item> items = itemRepository.findAll(connection);
            itemDTOS = items.stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList());
        } else if (role.equals(RoleEnum.USER.name())) {
            String status = StatusEnum.COMPLETED.name();
            List<Item> items = itemRepository.findItemsByStatus(connection, status);
            itemDTOS = items.stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList());
        }
        return itemDTOS;
    }


}
