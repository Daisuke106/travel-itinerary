package com.example.travel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

/**
 * スポット訪問記録エンティティ
 *
 * 「どの日程で、どのスポットに、何時に訪問するか」を管理する中間テーブル。
 *
 * ItineraryDay ←→ Spot の多対多を、
 * 「訪問時刻・順番・メモ」付きで管理するために中間テーブル化している。
 *
 * 例:
 *   1日目 → 10:00 栗林公園 (1番目)
 *   1日目 → 12:00 うどん屋  (2番目)
 *   1日目 → 14:00 金刀比羅宮 (3番目)
 */
@Entity
@Table(name = "spot_visits")
@Getter
@Setter
@NoArgsConstructor
public class SpotVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 訪問順 (1, 2, 3...) */
    @Column(nullable = false)
    private int visitOrder;

    /** 到着予定時刻 */
    @Column
    private LocalTime arrivalTime;

    /** 出発予定時刻 */
    @Column
    private LocalTime departureTime;

    /** メモ (予約番号・注意事項など) */
    @Column(columnDefinition = "TEXT")
    private String memo;

    /** 予算 (円) */
    @Column
    private Integer budget;

    /** 親の日程 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_day_id", nullable = false)
    private ItineraryDay itineraryDay;

    /** 訪問するスポット */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;
}
