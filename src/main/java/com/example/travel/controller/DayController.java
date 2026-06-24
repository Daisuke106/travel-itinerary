package com.example.travel.controller;

import com.example.travel.entity.Spot;
import com.example.travel.repository.SpotRepository;
import com.example.travel.service.SpotVisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;

/**
 * 日程コントローラー
 *
 * 「旅程詳細画面 (show.html) でスポットを追加/削除/並び替える」操作を担当。
 *
 * 【URLの設計】
 * /days/{dayId}/visits/add → スポットを日程に追加
 * /days/{dayId}/visits/{id}/remove → スポットを日程から削除
 * /days/{dayId}/visits/{id}/move-up → 訪問順を上に
 * /days/{dayId}/visits/{id}/move-down → 訪問順を下に
 *
 * 操作後は旅程詳細画面 (/itineraries/{itineraryId}) にリダイレクトする。
 * → 「POST → Redirect → GET」パターン (PRGパターン) でF5リロードの二重送信を防ぐ
 */
@Slf4j
@Controller
@RequestMapping("/days/{dayId}")
@RequiredArgsConstructor
public class DayController {

    private final SpotVisitService spotVisitService;
    private final SpotRepository spotRepository;

    /**
     * スポット選択フォーム表示
     * GET /days/{dayId}/add-spot
     *
     * 旅程詳細画面から「スポットを追加」ボタンを押したときに表示する画面。
     * 登録済みスポット一覧から選んで日程に追加できる。
     */
    @GetMapping("/add-spot")
    public String addSpotForm(@PathVariable Long dayId,
            @RequestParam Long itineraryId,
            @RequestParam(required = false) String keyword,
            Model model) {

        // スポット検索（キーワードがあれば絞り込み）
        var spots = (keyword != null && !keyword.isBlank())
                ? spotRepository.findByNameContainingIgnoreCase(keyword)
                : spotRepository.findAll();

        model.addAttribute("dayId", dayId);
        model.addAttribute("itineraryId", itineraryId);
        model.addAttribute("spots", spots);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", Spot.SpotCategory.values());
        return "day/add-spot";
    }

    /**
     * スポットを日程に追加する
     * POST /days/{dayId}/visits/add
     *
     * 【@RequestParam の使い方】
     * フォームの <input name="spotId"> の値を受け取る。
     * required = false にすると未入力でもエラーにならない（nullが入る）。
     *
     * 【@DateTimeFormat】
     * HTML の <input type="time"> は "HH:mm" 形式で送られてくる。
     * Javaの LocalTime に自動変換するためのアノテーション。
     */
    @PostMapping("/visits/add")
    public String addSpot(@PathVariable Long dayId,
            @RequestParam Long itineraryId,
            @RequestParam Long spotId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime arrivalTime,
            @RequestParam(required = false) String memo,
            @RequestParam(required = false) Integer budget,
            RedirectAttributes redirectAttributes) {

        try {
            spotVisitService.addSpotToDay(dayId, spotId, arrivalTime, memo, budget);
            redirectAttributes.addFlashAttribute("successMessage", "スポットを追加しました");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            log.error("スポット追加中にエラーが発生しました: dayId={}, spotId={}", dayId, spotId, e);
            redirectAttributes.addFlashAttribute("errorMessage", "スポットの追加に失敗しました。しばらく経ってから再度お試しください。");
        }

        // 旅程詳細ページに戻る（PRGパターン）
        return "redirect:/itineraries/" + itineraryId;
    }

    /**
     * スポットを日程から削除する
     * POST /days/{dayId}/visits/{visitId}/remove
     */
    @PostMapping("/visits/{visitId}/remove")
    public String removeSpot(@PathVariable Long dayId,
            @PathVariable Long visitId,
            @RequestParam Long itineraryId,
            RedirectAttributes redirectAttributes) {

        try {
            spotVisitService.validateVisitBelongsToDay(visitId, dayId);
            spotVisitService.remove(visitId);
            redirectAttributes.addFlashAttribute("successMessage", "スポットを削除しました");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/itineraries/" + itineraryId;
    }

    /**
     * 訪問順を上に移動
     * POST /days/{dayId}/visits/{visitId}/move-up
     */
    @PostMapping("/visits/{visitId}/move-up")
    public String moveUp(@PathVariable Long dayId,
            @PathVariable Long visitId,
            @RequestParam Long itineraryId,
            RedirectAttributes redirectAttributes) {

        try {
            spotVisitService.validateVisitBelongsToDay(visitId, dayId);
            spotVisitService.moveUp(visitId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/itineraries/" + itineraryId;
    }

    /**
     * 訪問順を下に移動
     * POST /days/{dayId}/visits/{visitId}/move-down
     */
    @PostMapping("/visits/{visitId}/move-down")
    public String moveDown(@PathVariable Long dayId,
            @PathVariable Long visitId,
            @RequestParam Long itineraryId,
            RedirectAttributes redirectAttributes) {

        try {
            spotVisitService.validateVisitBelongsToDay(visitId, dayId);
            spotVisitService.moveDown(visitId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/itineraries/" + itineraryId;
    }
}