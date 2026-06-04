package com.example.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 旅程エンティティ
 *
 * 1つの旅行全体を表す。
 * 例: 「四国一周旅行 2026/07/20〜07/25」
 */
@Entity
@Table(name = "itineraries")
@Getter
@Setter
@NoArgsConstructor
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 旅行タイトル (例: 四国旅行) */
    @NotBlank(message = "タイトルは必須です")
    @Column(nullable = false, length = 100)
    private String title;

    /** 出発地 (例: 東京) */
    @NotBlank(message = "出発地は必須です")
    @Column(nullable = false, length = 100)
    private String departureLocation;

    /** 旅行開始日 */
    @NotNull(message = "開始日は必須です")
    @Column(nullable = false)
    private LocalDate startDate;

    /** 旅行終了日 */
    @NotNull(message = "終了日は必須です")
    @Column(nullable = false)
    private LocalDate endDate;

    /** 旅行メンバー数 */
    @Column(nullable = false)
    private int memberCount = 1;

    /** メモ・備考 */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /** 作成日時 (自動セット) */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 (自動セット) */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 1つの旅程は複数の日程 (ItineraryDay) を持つ
     * cascade = 旅程を削除すると日程も自動削除
     * orphanRemoval = 日程が旅程から外れたら自動削除
     */
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC")
    private List<ItineraryDay> days = new ArrayList<>();

    // --- ヘルパーメソッド ---

    /** 旅行日数を返す */
    public long getTripDays() {
        return startDate.until(endDate).getDays() + 1;
    }

    /** 日程を追加する */
    public void addDay(ItineraryDay day) {
        days.add(day);
        day.setItinerary(this);
    }
}
