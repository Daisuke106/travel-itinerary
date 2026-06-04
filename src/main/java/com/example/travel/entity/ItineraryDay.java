package com.example.travel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 日程エンティティ
 *
 * 旅程の中の1日を表す。
 * 例: 「1日目 2026/07/20 高松観光」
 */
@Entity
@Table(name = "itinerary_days")
@Getter
@Setter
@NoArgsConstructor
public class ItineraryDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 何日目か (1, 2, 3...) */
    @Column(nullable = false)
    private int dayNumber;

    /** その日の日付 */
    @Column(nullable = false)
    private LocalDate date;

    /** その日のテーマ・タイトル (例: 高松 → 松山) */
    @Column(length = 100)
    private String title;

    /** メモ */
    @Column(columnDefinition = "TEXT")
    private String memo;

    /** 親の旅程 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    /**
     * 1日は複数のスポット訪問を持つ
     */
    @OneToMany(mappedBy = "itineraryDay", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("visitOrder ASC")
    private List<SpotVisit> spotVisits = new ArrayList<>();

    /** スポット訪問を追加する */
    public void addSpotVisit(SpotVisit visit) {
        spotVisits.add(visit);
        visit.setItineraryDay(this);
    }
}
