package com.example.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 観光スポットエンティティ
 *
 * 旅行先のスポット情報を管理する。
 * 例: 「栗林公園」「道後温泉」「金刀比羅宮」
 *
 * SpotVisit(訪問記録) とは別テーブルにすることで、
 * 同じスポットを複数の旅程で使いまわせる
 */
@Entity
@Table(name = "spots")
@Getter
@Setter
@NoArgsConstructor
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** スポット名 (例: 道後温泉) */
    @NotBlank(message = "スポット名は必須です")
    @Column(nullable = false, length = 100)
    private String name;

    /** 住所 */
    @Column(length = 200)
    private String address;

    /** 緯度 (Google Maps連携用) */
    @Column
    private Double latitude;

    /** 経度 (Google Maps連携用) */
    @Column
    private Double longitude;

    /** カテゴリ (観光地 / 飲食店 / 宿泊施設 / 交通) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpotCategory category = SpotCategory.SIGHTSEEING;

    /** メモ・説明 */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** 公式URL */
    @Pattern(regexp = "^(https?://.*)?$", message = "URLは http:// または https:// で始めてください")
    @Column(length = 500)
    private String websiteUrl;

    /** 電話番号 */
    @Column(length = 20)
    private String phoneNumber;

    // --- スポットカテゴリの列挙型 ---
    public enum SpotCategory {
        SIGHTSEEING("観光地"),
        RESTAURANT("飲食店"),
        ACCOMMODATION("宿泊施設"),
        TRANSPORT("交通");

        private final String displayName;

        SpotCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
