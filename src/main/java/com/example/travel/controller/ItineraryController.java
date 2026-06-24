package com.example.travel.controller;

import com.example.travel.entity.Itinerary;
import com.example.travel.service.ItineraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 旅程コントローラー
 *
 * URL のルーティングと画面への値の受け渡しを担当する。
 * ビジネスロジックは Service に委譲する。
 */
@Controller
@RequestMapping("/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;

    /**
     * 旅程一覧画面
     * GET /itineraries
     */
    @GetMapping
    public String index(Model model) {
        model.addAttribute("itineraries", itineraryService.findAll());
        return "itinerary/index";  // templates/itinerary/index.html
    }

    /**
     * 旅程詳細画面
     * GET /itineraries/{id}
     */
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("itinerary", itineraryService.findById(id));
        return "itinerary/show";
    }

    /**
     * 旅程作成フォーム表示
     * GET /itineraries/new
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("itinerary", new Itinerary());
        return "itinerary/form";
    }

    /**
     * 旅程作成処理
     * POST /itineraries
     */
    @PostMapping
    public String create(
            @Valid @ModelAttribute Itinerary itinerary,
            BindingResult bindingResult,           // バリデーション結果
            RedirectAttributes redirectAttributes, // リダイレクト先にメッセージを渡す
            Model model) {

        if (bindingResult.hasErrors()) {
            return "itinerary/form";  // エラーがあればフォームに戻る
        }

        try {
            // マスアサインメント対策: フォームからIDが送られてきても新規作成として扱う
            itinerary.setId(null);
            Itinerary created = itineraryService.create(itinerary);
            redirectAttributes.addFlashAttribute("successMessage", "旅程を作成しました！");
            return "redirect:/itineraries/" + created.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "itinerary/form";
        }
    }

    /**
     * 旅程編集フォーム表示
     * GET /itineraries/{id}/edit
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("itinerary", itineraryService.findById(id));
        return "itinerary/form";
    }

    /**
     * 旅程更新処理
     * POST /itineraries/{id} (HTML formはPUTが使えないためPOSTで代替)
     */
    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute Itinerary itinerary,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "itinerary/form";
        }

        itineraryService.update(id, itinerary);
        redirectAttributes.addFlashAttribute("successMessage", "旅程を更新しました！");
        return "redirect:/itineraries/" + id;
    }

    /**
     * 旅程削除
     * POST /itineraries/{id}/delete
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        itineraryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "旅程を削除しました");
        return "redirect:/itineraries";
    }
}
