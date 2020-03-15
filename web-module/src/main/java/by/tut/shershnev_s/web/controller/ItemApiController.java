package by.tut.shershnev_s.web.controller;

import by.tut.shershnev_s.service.ItemService;
import by.tut.shershnev_s.service.model.ItemDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemApiController {

    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<ItemDTO> getItems() {
        List<ItemDTO> items = itemService.findAllForApi();
        return items;
    }

    @PostMapping("/items")
    public ItemDTO addItem(@Valid @RequestBody ItemDTO itemDTO) {
        return itemService.add(itemDTO);
    }

    @DeleteMapping("/items/delete/{status}")
    public void deleteItem(@PathVariable String status) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setStatus(status);
        itemService.deleteByStatus(itemDTO);
    }

    @PutMapping("/items/update/{id}")
    public void updateById(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
        itemDTO.setId(id);
        itemService.updateById(itemDTO);
    }
}
