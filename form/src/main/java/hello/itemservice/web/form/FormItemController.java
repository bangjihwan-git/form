package hello.itemservice.web.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;
    
    @ModelAttribute("regions") // 모든 호출된 메서드의 model에 담아줌
    public Map<String, String> regions(){
    	// 보통 static으로 따로 빼고사용
    	Map<String, String> regions = new LinkedHashMap<>();
    	regions.put("SEOUL", "서울");
    	regions.put("BUSAN", "부산");
    	regions.put("JEJU", "제주");
    	return regions;
    }
    
    @ModelAttribute("itemTypes") // 모든 호출된 메서드의 model에 담아줌
    public ItemType[] itemTypes(){
    	return ItemType.values();
    }
    
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
    	// 보통 static으로 따로 빼고사용
    	List<DeliveryCode> deliveryCodes = new ArrayList<DeliveryCode>();
    	deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
    	deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
    	deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
    	return deliveryCodes;
    }
    
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
    	model.addAttribute("item", new Item());
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
    	
    	log.info("item.open={}", item.getOpen());
    	log.info("item.regions={}", item.getRegions());
    	log.info("item.itemType={}", item.getItemType());
    	
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

