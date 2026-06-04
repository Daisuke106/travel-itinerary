package com.example.travel.service;

import com.example.travel.entity.ItineraryDay;
import com.example.travel.entity.Spot;
import com.example.travel.entity.SpotVisit;
import com.example.travel.repository.ItineraryRepository;
import com.example.travel.repository.SpotRepository;
import com.example.travel.repository.SpotVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

/**
 * スポット訪問記録サービス
 *
 * 【Controller → Service → Repository の流れ】
 * Controller はHTTPリクエストの受け口。「何をしたいか」だけを伝える。
 * Service はビジネスロジックの実装。「どうやるか」を知っている。
 * Repository はDB操作の窓口。「データをどう取得・保存するか」を知っている。
 *
 * Controller が Repository を直接呼ばない理由:
 * → ロジックが複数のRepositoryにまたがる場合、Serviceに書けばControllerはシンプルのまま。
 */
@Service
@RequiredArgsConstructor
public class SpotVisitService {

    private final SpotVisitRepository spotVisitRepository;
    private final SpotRepository spotRepository;
    private final ItineraryRepository itineraryRepository;

    /**
     * 日程にスポットを追加する
     *
     * 【処理の流れ】
     * 1. 日程とスポットをDBから取得
     * 2. 今の最大visitOrderを調べて +1 した値を新しい訪問順にする
     * 3. SpotVisit を作って保存
     *
     * @param itineraryDayId 追加先の日程ID
     * @param spotId         追加するスポットID
     * @param arrivalTime    到着予定時刻（nullも可）
     * @param memo           メモ（nullも可）
     * @param budget         予算（nullも可）
     */
    @Transactional
    public SpotVisit addSpotToDay(Long itineraryDayId, Long spotId,
            LocalTime arrivalTime, String memo, Integer budget) {

        // 日程を取得（存在しなければ例外）
        ItineraryDay day = itineraryRepository
                .findById(getDayItineraryId(itineraryDayId)) // ← 後述のヘルパー
                .map(itinerary -> itinerary.getDays().stream()
                        .filter(d -> d.getId().equals(itineraryDayId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("日程が見つかりません: id=" + itineraryDayId)))
                .orElseThrow(() -> new IllegalArgumentException("日程が見つかりません: id=" + itineraryDayId));

        // スポットを取得
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("スポットが見つかりません: id=" + spotId));

        // 次の訪問順を決める（今の最大 + 1）
        int nextOrder = spotVisitRepository.findMaxVisitOrderByDayId(itineraryDayId) + 1;

        // SpotVisit を組み立てて保存
        SpotVisit visit = new SpotVisit();
        visit.setItineraryDay(day);
        visit.setSpot(spot);
        visit.setVisitOrder(nextOrder);
        visit.setArrivalTime(arrivalTime);
        visit.setMemo(memo);
        visit.setBudget(budget);

        return spotVisitRepository.save(visit);
    }

    /**
     * スポット訪問記録を更新する
     */
    @Transactional
    public SpotVisit update(Long visitId, LocalTime arrivalTime,
            LocalTime departureTime, String memo, Integer budget) {
        SpotVisit visit = findById(visitId);
        visit.setArrivalTime(arrivalTime);
        visit.setDepartureTime(departureTime);
        visit.setMemo(memo);
        visit.setBudget(budget);
        return spotVisitRepository.save(visit);
    }

    /**
     * スポット訪問記録を日程から削除する
     */
    @Transactional
    public void remove(Long visitId) {
        SpotVisit visit = findById(visitId);
        Long dayId = visit.getItineraryDay().getId();

        spotVisitRepository.delete(visit);

        // 削除後に visitOrder を1〜N に振り直す（歯抜け防止）
        reorderVisits(dayId);
    }

    /**
     * 訪問順を上に移動（visitOrder を -1 する）
     *
     * 【スワップの考え方】
     * order=2 のスポットを上に動かす → order=1 と入れ替える
     * 1. order=1 のSpotVisitを探す
     * 2. order=1 → order=2 に変更
     * 3. 元のSpotVisit を order=2 → order=1 に変更
     */
    @Transactional
    public void moveUp(Long visitId) {
        SpotVisit visit = findById(visitId);
        if (visit.getVisitOrder() <= 1)
            return; // 先頭なら何もしない

        Long dayId = visit.getItineraryDay().getId();
        List<SpotVisit> visits = spotVisitRepository.findByItineraryDayIdOrderByVisitOrderAsc(dayId);

        // 1つ上のSpotVisitを探してスワップ
        visits.stream()
                .filter(v -> v.getVisitOrder() == visit.getVisitOrder() - 1)
                .findFirst()
                .ifPresent(prev -> {
                    prev.setVisitOrder(visit.getVisitOrder());
                    visit.setVisitOrder(visit.getVisitOrder() - 1);
                    spotVisitRepository.save(prev);
                    spotVisitRepository.save(visit);
                });
    }

    /**
     * 訪問順を下に移動（visitOrder を +1 する）
     */
    @Transactional
    public void moveDown(Long visitId) {
        SpotVisit visit = findById(visitId);
        Long dayId = visit.getItineraryDay().getId();
        int maxOrder = spotVisitRepository.findMaxVisitOrderByDayId(dayId);

        if (visit.getVisitOrder() >= maxOrder)
            return; // 末尾なら何もしない

        List<SpotVisit> visits = spotVisitRepository.findByItineraryDayIdOrderByVisitOrderAsc(dayId);

        visits.stream()
                .filter(v -> v.getVisitOrder() == visit.getVisitOrder() + 1)
                .findFirst()
                .ifPresent(next -> {
                    next.setVisitOrder(visit.getVisitOrder());
                    visit.setVisitOrder(visit.getVisitOrder() + 1);
                    spotVisitRepository.save(next);
                    spotVisitRepository.save(visit);
                });
    }

    /**
     * IDでSpotVisitを取得
     */
    @Transactional(readOnly = true)
    public SpotVisit findById(Long id) {
        return spotVisitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("訪問記録が見つかりません: id=" + id));
    }

    // =========================================================
    // private ヘルパー
    // =========================================================

    /**
     * dayIdから旅程IDを引く（簡易実装: JPQL で直接引く）
     *
     * 【なぜこんな書き方をするか】
     * ItineraryDay の Repository を別途作っても良いが、
     * 今はItineraryRepository経由でアクセスできるので省略している。
     * Week3以降でDayRepositoryを作るときに綺麗にリファクタできる。
     */
    private Long getDayItineraryId(Long dayId) {
        // SpotVisitRepository経由でdayのitineraryIdを取る代わりに
        // itineraryRepository の全検索から探す（スポット数が少ない前提の簡易実装）
        return itineraryRepository.findAll().stream()
                .filter(it -> it.getDays().stream().anyMatch(d -> d.getId().equals(dayId)))
                .map(it -> it.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("日程に対応する旅程が見つかりません: dayId=" + dayId));
    }

    /**
     * visitOrder を 1 から連番に振り直す
     * スポット削除後に呼ぶことで「1, 3, 4」が「1, 2, 3」になる
     */
    private void reorderVisits(Long dayId) {
        List<SpotVisit> visits = spotVisitRepository.findByItineraryDayIdOrderByVisitOrderAsc(dayId);
        for (int i = 0; i < visits.size(); i++) {
            visits.get(i).setVisitOrder(i + 1);
        }
        spotVisitRepository.saveAll(visits);
    }
}
