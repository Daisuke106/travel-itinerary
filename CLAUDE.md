# 旅行しおり自動生成ツール — Claude Code 設定

## プロジェクト概要

Spring Boot を使った旅行しおり（旅程管理）Web アプリ。
旅程・日程・観光スポットの CRUD 操作を提供し、PDF 出力機能も備える。

## 技術スタック

| 役割 | 技術 |
|------|------|
| 言語 / フレームワーク | Java 21 / Spring Boot 3.3 |
| テンプレートエンジン | Thymeleaf |
| DB (開発) | H2 インメモリ |
| DB (本番) | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| セキュリティ | Spring Security (CSRF 保護) |
| ユーティリティ | Lombok, OpenPDF |
| フロントエンド | Bootstrap 5, Bootstrap Icons |

## 主要コマンド

> **Java 21 が必要です。** 初回は下記の「Java 21 への切り替え」を先に行ってください。

```powershell
# 開発サーバー起動 (H2 + サンプルデータ自動投入)
.\mvnw spring-boot:run

# 本番モードで起動 (要: DB_USERNAME / DB_PASSWORD 環境変数)
.\mvnw spring-boot:run "-Dspring.profiles.active=prod"

# ビルド (JAR 生成)
.\mvnw clean package

# テスト実行
.\mvnw test

# コーディング規約チェック (Checkstyle)
.\mvnw checkstyle:check

# 規約チェック付きフルビルド
.\mvnw verify
```

### Java 21 への切り替え (PowerShell — セッションごとに必要)

```powershell
$env:JAVA_HOME = "C:\pleiades2026\java\21"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
java --version   # openjdk 21.x と表示されれば OK
```

永続化したい場合は `C:\pleiades2026\java\set-JAVA_HOME-21.cmd` を実行するか、
システム環境変数 `JAVA_HOME` を `C:\pleiades2026\java\21` に設定してください。

## ディレクトリ構成

```
src/main/java/com/example/travel/
├── config/          # SecurityConfig など横断的設定
├── controller/      # HTTP リクエスト処理 (ルーティング)
├── entity/          # JPA エンティティ (DB テーブルに対応)
├── repository/      # DB アクセス (Spring Data JPA)
├── service/         # ビジネスロジック
└── DataInitializer  # 開発用サンプルデータ投入

src/main/resources/
├── templates/
│   ├── common/      # 共通レイアウト (layout.html)
│   ├── itinerary/   # 旅程 CRUD 画面
│   ├── spot/        # スポット CRUD 画面
│   └── day/         # 日程へのスポット追加画面
└── application.yml  # プロファイル別設定
```

## データモデル

```
Itinerary (旅程)
  └── ItineraryDay[] (日程) 1対多
        └── SpotVisit[] (訪問記録) 1対多
              └── Spot (スポット) 多対1
```

## 設計パターン・規約

- **3層アーキテクチャ**: Controller → Service → Repository。Controller は Repository を直接呼ばない。
- **PRG パターン**: POST → Redirect → GET でF5リロード二重送信を防止。
- **CSRF 保護**: `th:action` を使えば Thymeleaf が自動でトークンを挿入する。HTML フォームは必ず `th:action` を使う。
- **IDOR 対策**: URL パスの `dayId` に `visitId` が属するか `SpotVisitService.validateVisitBelongsToDay()` で検証する。
- **例外メッセージ**: 内部例外は `log.error()` に留め、ユーザーへは汎用メッセージを返す。

## コーディング規約

詳細は [docs/CODING_STANDARDS.md](docs/CODING_STANDARDS.md) を参照。

**最重要ルール (★★★)**

| 対象 | 規則 | 例 |
|------|------|----|
| クラス名 | PascalCase / 役割を表す英単語 | `ItineraryService` |
| メソッド名 | camelCase / 目的が分かる名前 | `findByItineraryDayId()` |
| 変数名 | camelCase / 意味のある名前 | `spotVisitRepository` |
| 定数 | UPPER\_SNAKE\_CASE | `MAX_MEMBER_COUNT` |
| インデント | 半角スペース4つ (タブ禁止) | |
| 行長 | 110 文字以内 | |
| 制御文 | `{}` を省略しない | |

## セキュリティ上の注意

- `SecurityConfig.java` の変更時は CSRF 除外パスを慎重に確認する。
- H2 コンソール (`/h2-console`) は `dev` プロファイルのみ有効。本番環境で有効にしない。
- `application.yml` に資格情報を直書きしない。必ず `${ENV_VAR}` で環境変数から取得する。
- `.gitignore` に記載の `.env` / `application-prod.yml` / `application-secrets.yml` をコミットしない。
