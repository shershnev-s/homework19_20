package by.tut.shershnev_s.service;

import by.tut.shershnev_s.service.model.ItemDTO;

import java.util.List;

public interface ItemService {

    List<ItemDTO> findAll();

    ItemDTO add(ItemDTO itemDTO);

    void updateById(ItemDTO itemDTO);

    void deleteByStatus(ItemDTO itemDTO);

    List<ItemDTO> findAllForApi();
}
