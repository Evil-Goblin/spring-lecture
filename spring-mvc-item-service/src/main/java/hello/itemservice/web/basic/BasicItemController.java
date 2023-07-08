package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "/basic/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(@RequestParam String itemName,
//                       @RequestParam Integer price,
//                       @RequestParam Integer quantity,
//                       Model model) {
//        Item item = new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);
//
//        itemRepository.save(item);
//
//        model.addAttribute("item", item);
//
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute("item") Item item) {
//        itemRepository.save(item);
//
////        model.addAttribute("item", item); // ModelAttribute 를 이용하면 자동으로 추가된다. 파라메터의 model도 제거 가능
//
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV3(@ModelAttribute Item item) {
//        // ModelAttribute 의 값을 지정하지 않을 시 데이터에 해당하는 클래스명의 첫글자를 소문자로한 값이 사용된다.
//        // 위의 경우 Item -> item
//        itemRepository.save(item);
//
////        model.addAttribute("item", item); // ModelAttribute 를 이용하면 자동으로 추가된다. 파라메터의 model도 제거 가능
//
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV3(Item item) {
//        // ModelAttribute 의 값을 지정하지 않을 시 데이터에 해당하는 클래스명의 첫글자를 소문자로한 값이 사용된다.
//        // 위의 경우 Item -> item
//        // ModelAttribute 생략가능
//        itemRepository.save(item);
//
////        model.addAttribute("item", item); // ModelAttribute 를 이용하면 자동으로 추가된다. 파라메터의 model도 제거 가능
//
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV4(Item item) {
//        // ModelAttribute 의 값을 지정하지 않을 시 데이터에 해당하는 클래스명의 첫글자를 소문자로한 값이 사용된다.
//        // 위의 경우 Item -> item
//        // ModelAttribute 생략가능
//        itemRepository.save(item);
//
////        model.addAttribute("item", item); // ModelAttribute 를 이용하면 자동으로 추가된다. 파라메터의 model도 제거 가능
//
//        return "redirect:/basic/items/" + item.getId();
//    }

    @PostMapping("/add")
    public String addItemV5(Item item, RedirectAttributes redirectAttributes) {
        Item saved = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saved.getId());
        redirectAttributes.addAttribute("status", true);
        // 초과된 attribute는 쿼리 파라메터로 전달된다.

        return "redirect:/basic/items/{itemId}";
    }

//    @PostMapping("/add")
//    public String addItemCustom(Item item, Model model) {
//        itemRepository.save(item);
//
//        model.addAttribute("item", item); // 요것조차 없어도 됐구나...
//
//        return "basic/item";
//    }

    @GetMapping("/{itemId}/edit")
    public String editFrom(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable long itemId, Item item) {
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }
}
