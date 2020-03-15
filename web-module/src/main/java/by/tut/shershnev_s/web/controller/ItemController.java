package by.tut.shershnev_s.web.controller;

import by.tut.shershnev_s.service.ItemService;
import by.tut.shershnev_s.service.model.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
public String getItems(Model model){
        List<ItemDTO> items = itemService.findAll();
        model.addAttribute("items", items);
        return "/items";
    }

}
