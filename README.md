# 旅行しおり自動生成ツール

Spring Boot + PostgreSQL で作る旅行しおりWebアプリ。

## 技術スタック

| 層 | 技術 |
|---|---|
| Frontend | Thymeleaf + Bootstrap 5 |
| Backend | Spring Boot 3.3 / Java 21 |
| DB (開発) | H2 インメモリDB |
| DB (本番) | PostgreSQL |
| ビルド | Maven |

## ディレクトリ構成

```
src/main/java/com/example/travel/
├── TravelItineraryApplication.java  # エントリーポイント
├── DataInitializer.java             # 開発用サンプルデータ
├── controller/
│   └── ItineraryController.java     # URL ルーティング
├── service/
│   └── ItineraryService.java        # ビジネスロジック
├── repository/
│   ├── ItineraryRepository.java     # 旅程 DB アクセス
│   └── SpotRepository.java          # スポット DB アクセス
└── entity/
    ├── Itinerary.java               # 旅程テーブル
    ├── ItineraryDay.java            # 日程テーブル
    ├── Spot.java                    # スポットテーブル
    └── SpotVisit.java              # スポット訪問記録テーブル
```

## DB 設計

```
itineraries (旅程)
  └── itinerary_days (日程) 1対多
        └── spot_visits (訪問記録) 1対多
              └── spots (スポット) 多対1
```

## 起動方法

### 開発環境 (H2使用・推奨)

```bash
# プロジェクトルートで実行
mvn spring-boot:run
```

- アプリ: http://localhost:8080/itineraries
- H2コンソール: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:traveldb`
  - User: `sa` / Password: (空)

### 本番環境 (PostgreSQL)

```bash
# PostgreSQLのDB作成
createdb travel_itinerary

# 環境変数を設定してから起動
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
mvn spring-boot:run -Dspring.profiles.active=prod
```

## 開発ロードマップ

- [x] Week 1: プロジェクト作成・DB設計・旅程CRUD
- [ ] Week 2: 旅程詳細画面・スポット追加機能
- [ ] Week 3: Leaflet.js で地図・ルート表示
- [ ] Week 4: PDF出力機能 (OpenPDF)
- [ ] Week 5+: 天気API・交通API連携
