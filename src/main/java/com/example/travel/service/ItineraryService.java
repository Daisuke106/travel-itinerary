package com.example.travel.service;

import com.example.travel.entity.Itinerary;
import com.example.travel.entity.ItineraryDay;
import com.example.travel.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 旅程サービス
 *
 * Controller から呼ばれるビジネスロジック層。
 * Repository への直接アクセスは Controller から行わず、
 * 必ずこの Service を経由する。
 */
@Service
@RequiredArgsConstructor  // Lombokが final フィールドのコンストラクタを自動生成
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    /**
     * 全旅程を新しい順に取得
     */
    @Transactional(readOnly = true)
    public List<Itinerary> findAll() {
        return itineraryRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * IDで旅程を取得
     * 存在しない場合は例外を投げる
     */
    @Transactional(readOnly = true)
    public Itinerary findById(Long id) {
        return itineraryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("旅程が見つかりません: id=" + id));
    }

    /**
     * 旅程を新規作成
     *
     * 開始日〜終了日の分だけ ItineraryDay を自動生成する。
     * 例: 7/20〜7/22 なら 1日目・2日目・3日目 を作成
     */
    @Transactional
    public Itinerary create(Itinerary itinerary) {
        // 日付バリデーション
        if (itinerary.getEndDate().isBefore(itinerary.getStartDate())) {
            throw new IllegalArgumentException("終了日は開始日以降に設定してください");
        }

        // 日程を自動生成
        LocalDate current = itinerary.getStartDate();
        int dayNumber = 1;
        while (!current.isAfter(itinerary.getEndDate())) {
            ItineraryDay day = new ItineraryDay();
            day.setDayNumber(dayNumber);
            day.setDate(current);
            day.setTitle(dayNumber + "日目");
            itinerary.addDay(day);
            current = current.plusDays(1);
            dayNumber++;
        }

        return itineraryRepository.save(itinerary);
    }

    /**
     * 旅程を更新
     */
    @Transactional
    public Itinerary update(Long id, Itinerary updated) {
        Itinerary existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setDepartureLocation(updated.getDepartureLocation());
        existing.setMemberCount(updated.getMemberCount());
        existing.setNotes(updated.getNotes());
        return itineraryRepository.save(existing);
    }

    /**
     * 旅程を削除 (日程・スポット訪問も cascade で削除される)
     */
    @Transactional
    public void delete(Long id) {
        itineraryRepository.deleteById(id);
    }
}
