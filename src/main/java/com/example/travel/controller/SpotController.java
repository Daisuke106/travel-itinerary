package com.example.travel.controller;

import com.example.travel.entity.Spot;
import com.example.travel.repository.SpotRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

/**
 * スポットコントローラー
 *
 * 【URLマッピングの設計方針】
 *   /spots          → スポット一覧
 *   /spots/new      → 新規作成フォーム
 *   /spots/{id}     → 詳細
 *   /spots/{id}/edit → 編集フォーム
 *
 *   RESTfulなURL設計にすることで「どのURLが何をするか」が直感的になる。
 */
@Controller
@RequestMapping("/spots")
@RequiredArgsConstructor
public class SpotController {

    private final SpotRepository spotRepository;

    /**
     * スポット一覧
     * GET /spots
     *
     * 【Model の使い方】
     *   model.addAttribute("キー名", 値) でHTMLテンプレートに値を渡す。
     *   Thymeleaf側では th:each="${spots}" のように参照できる。
     */
    @GetMapping
    public String index(Model model,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Spot.SpotCategory category) {

        // 検索条件に応じてスポット一覧を取得
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("spots", spotRepository.findByNameContainingIgnoreCase(keyword));
        } else if (category != null) {
            model.addAttribute("spots", spotRepository.findByCategory(category));
        } else {
            model.addAttribute("spots", spotRepository.findAll());
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("categories", Spot.SpotCategory.values()); // カテゴリ選択肢
        return "spot/index";
    }

    /**
     * スポット新規作成フォーム
     * GET /spots/new
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("spot", new Spot());
        model.addAttribute("categories", Spot.SpotCategory.values());
        return "spot/form";
    }

    /**
     * スポット作成処理
     * POST /spots
     *
     * 【@Valid と BindingResult の組み合わせ】
     *   @Valid   → Spot エンティティの @NotBlank などバリデーションを実行
     *   BindingResult → バリデーション結果を受け取る変数
     *   bindingResult.hasErrors() が true なら入力エラーあり → フォームに戻す
     *
     *   【重要】 BindingResult は @Valid の直後に書かないとエラーが飛ぶ！
     */
    @PostMapping
    public String create(@Valid @ModelAttribute Spot spot,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", Spot.SpotCategory.values());
            return "spot/form";
        }

        spotRepository.save(spot);
        redirectAttributes.addFlashAttribute("successMessage",
                "「" + spot.getName() + "」を登録しました");
        return "redirect:/spots";
    }

    /**
     * スポット編集フォーム
     * GET /spots/{id}/edit
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("スポットが見つかりません: id=" + id));
        model.addAttribute("spot", spot);
        model.addAttribute("categories", Spot.SpotCategory.values());
        return "spot/form";
    }

    /**
     * スポット更新処理
     * POST /spots/{id}
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Spot spot,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", Spot.SpotCategory.values());
            return "spot/form";
        }

        // 既存レコードのIDを保持したまま上書き保存
        spot.setId(id);
        spotRepository.save(spot);
        redirectAttributes.addFlashAttribute("successMessage",
                "「" + spot.getName() + "」を更新しました");
        return "redirect:/spots";
    }

    /**
     * スポット削除
     * POST /spots/{id}/delete
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("スポットが見つかりません: id=" + id));
        String name = spot.getName();
        spotRepository.delete(spot);
        redirectAttributes.addFlashAttribute("successMessage",
                "「" + name + "」を削除しました");
        return "redirect:/spots";
    }
}