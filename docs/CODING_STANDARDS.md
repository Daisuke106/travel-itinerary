# Java コーディング規約

本規約は [Qiita — Java コーディング規約](https://qiita.com/fsd-fukufuku/items/cfbf30e568ef9113c099) をベースに、
本プロジェクト向けに整理したものです。

重要度の凡例: ★★★ 必須 / ★★ 強く推奨 / ★ 推奨

---

## 1. 命名規約

### 1.1 命名全般

#### 規約1 ★★★ 名前には英単語を使用する

原則として英単語を使用する。日本語をローマ字にしたものは使用しない。

```java
// 良い例
public void setOrderAmount() { }

// 悪い例
public void setJuchukingaku() { }
```

#### 規約2 ★ 大文字と小文字だけで名前を区別しない

コンパイラが大文字と小文字を区別することを利用した名前の区別は行わない。
紛らわしい命名も避ける。

```java
// 良い例
Date birthday;
Date myBirthday;
Date nextBirthday;

// 悪い例 (大文字小文字のみの区別)
Date birthday;
Date birthDay;
static Date BIRTHDAY;
```

---

### 1.2 パッケージ

#### 規約3 ★★ パッケージ名はすべて小文字にする

- 大文字を使わず小文字に統一する
- パッケージ名の英単語は単数形にする
- 省略語を使用しない

```java
// 良い例
com.example.travel.controller

// 悪い例 (大文字あり)
com.example.travel.Controller
// 悪い例 (複数形)
com.example.travel.controllers
// 悪い例 (省略語)
com.example.travel.ctrl
```

#### 規約5 ★★ import で `*` を省略しない

```java
// 良い例
import java.util.List;
import java.util.ArrayList;

// 容認 (同一パッケージから3クラス以上使う場合)
import com.example.travel.entity.*;
```

---

### 1.3 クラス

#### 規約6 ★★★ クラス名は役割を表す名前にする

- 1単語の命名は避け、2単語以上で命名する
- 略称ではなくフルスペルの英単語を使う

```java
// 良い例
ItineraryService
SpotVisitRepository

// 悪い例
Svc01
```

#### 規約7 ★★★ クラス名は PascalCase で記述する

最初の文字を大文字とし、各単語の先頭を大文字にする。  
アンダースコア `_`・ハイフン `-`・ドル記号 `$` は使わない。

```java
// 良い例
public class ItineraryController { }
public class SpotVisit { }

// 悪い例
public class itinerary_controller { }
public class spotvisit { }
```

#### 規約8 ★★★ クラス名はフルスペルで記述する

```java
// 良い例
public class ItineraryService { }

// 悪い例
public class ItinSvc { }
```

#### 規約9 ★★ 抽象クラスの名前には `Abstract` を付ける

```java
// 良い例
public abstract class AbstractSlipController { }
public class ItineraryController extends AbstractSlipController { }
```

#### 規約10 ★★ インタフェース名はクラス名に準ずる

インタフェースであることを明確にするため、名前の先頭に大文字 `I` を付ける。

```java
// 良い例
interface ISpotSearchService { }
public class SpotSearchService implements ISpotSearchService { }
```

#### 規約11 ★ 例外クラス名の末尾に `Exception` を付ける

```java
// 良い例
public class ItineraryNotFoundException extends RuntimeException { }
```

テストクラスの末尾には `Test` を付ける。

```java
// 良い例
class ItineraryServiceTest { }
```

---

### 1.4 メソッド

#### 規約13 ★★★ メソッド名は目的のわかる名前にする

メソッドが実行されたときに何が起こるかわかる名前を付ける。

```java
// 良い例
public Itinerary createItinerary(Itinerary itinerary) { }
public List<Spot> findByNameContaining(String keyword) { }

// 悪い例
public Itinerary process(Itinerary i) { }
```

**動詞の使い方の目安**

| 動詞 | 意味 |
|------|------|
| `find` / `get` | 取得する |
| `create` / `add` | 作成・追加する |
| `update` / `set` | 更新・設定する |
| `delete` / `remove` | 削除・除去する |
| `validate` / `check` | 検証・確認する |
| `calculate` | 計算する |
| `convert` / `to` | 変換する |
| `initialize` / `init` | 初期化する |

#### 規約14 ★★★ メソッド名は camelCase で記述する

最初の単語を小文字とし、続く単語の先頭文字を大文字にする。  
アンダースコア `_`・ハイフン `-`・ドル記号 `$` は使わない。

```java
// 良い例
public SpotVisit addSpotToDay(Long dayId, Long spotId) { }
public void validateVisitBelongsToDay(Long visitId, Long dayId) { }

// 悪い例
public SpotVisit AddSpotToDay(Long dayId, Long spotId) { }
public void validate_visit(Long visitId, Long dayId) { }
```

#### 規約15 ★★★ メソッドの役割の対称性を意識する

対になるメソッドは対義語を使って命名する。

```java
// 良い例 (対称性が明確)
public void addSpot(Spot spot) { }
public void removeSpot(Spot spot) { }

public SpotVisit moveUp(Long visitId) { }
public SpotVisit moveDown(Long visitId) { }
```

**主な対義語**

| 組み合わせ | 組み合わせ | 組み合わせ |
|-----------|-----------|-----------|
| add / remove | get / set | start / stop |
| insert / delete | open / close | first / last |
| send / receive | show / hide | next / previous |

#### 規約16 ★ ゲッターメソッド名は `get + 属性名` とする

```java
public Itinerary getItinerary() { }
```

#### 規約17 ★ セッターメソッド名は `set + 属性名` とする

```java
public void setTitle(String title) {
    this.title = title;
}
```

#### 規約18 ★ boolean を返すメソッドは true/false の識別がわかる名前にする

`is + 形容詞` / `can + 動詞` / `has + 過去分詞` / `三単現動詞` を先頭に使う。

```java
// 良い例
boolean isEmpty() { }
boolean canRemove() { }
boolean hasChanged() { }
boolean existsByIdAndItineraryDayId(Long id, Long dayId) { }

// 悪い例
boolean isCheck() { }   // チェックか？という意味が不明
boolean setAmount() { } // セッターが boolean を返すのは不適切
```

---

### 1.5 変数・定数

#### 規約19 ★★★ 変数には意味のある名前をつける

```java
// 良い例
SpotVisit spotVisit = createSpotVisit(day, spot);
int nextVisitOrder = repository.findMaxVisitOrderByDayId(dayId) + 1;

// 悪い例
SpotVisit obj = createSpotVisit(day, spot);
int n = repository.findMaxVisitOrderByDayId(dayId) + 1;
```

ただしループカウンタ (`i`, `j`, `k`) と例外オブジェクト (`e`) は慣習として許容する。

#### 規約20 ★★★ 変数名は camelCase で記述する

```java
// 良い例
double unitPrice = 0.0;
String spotName;

// 悪い例
double UnitPrice = 0.0;
String spot_name;
```

#### 規約21 ★ boolean 型の変数は true/false の識別がわかる名前にする

```java
// 良い例
boolean isEmpty;
boolean hasChanged;
boolean existsStock;
```

#### 規約23 ★★ 引数名とフィールド名が同じになることを回避する

同名になる場合は引数側の先頭に `a` / `an` を付けるか、`this` を使う。

```java
// 良い例
public void setTitle(String aTitle) {
    title = aTitle;
}

// またはthisを使う
public void setTitle(String title) {
    this.title = title;
}
```

#### 規約24 ★★ 定数名は `_` で区切った大文字表記とする

```java
// 良い例
public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
public static final int MAX_MEMBER_COUNT = 100;

// 悪い例
public static final String defaultDateFormat = "yyyy/MM/dd";
public static final int MaxMemberCount = 100;
```

---

## 2. コーディング規約

### 2.1 全般

#### 規約25 ★★ 1つのクラスは1つのソースファイルで定義する

ソースファイル名はクラス名と同じ PascalCase にする。

#### 規約27 使われないコードは書かない

未使用の private メソッドやローカル変数はコードに放置せず削除する。

#### 規約28 ★ アクセス修飾子は適切な権限で宣言する

| 修飾子 | 使用場面 |
|--------|---------|
| `public` | 外部から呼ばれるメソッド・定数フィールド |
| `protected` | サブクラスから呼ばれるメソッド |
| `private` | 自クラスのみで使うメソッド・全フィールド |

- フィールドは原則 `private`
- 定数フィールドのみ `public static final` を容認

---

### 2.2 スタイル

#### 規約33 ★★ 110文字を超える行は改行または文を分割する

```java
// 良い例 (長い引数はカンマの後で改行)
public SpotVisit addSpotToDay(Long itineraryDayId, Long spotId,
        LocalTime arrivalTime, String memo, Integer budget) { }
```

#### 規約34 ★★ 行の途中での改行は、カンマの後・演算子の前・節の前とする

改行後のインデントは4文字分。

#### 規約35 ★★ クラスとメソッド宣言行の末尾に `{` を記述する

```java
// 良い例
public class ItineraryService {
    public Itinerary create(Itinerary itinerary) {
    }
}

// 悪い例 (別行に {)
public class ItineraryService
{
}
```

#### 規約36 ★★ `{` の後ろにステートメントを記述しない

```java
// 良い例
if (bindingResult.hasErrors()) {
    return "itinerary/form";
}

// 悪い例
if (bindingResult.hasErrors()) { return "itinerary/form"; }
```

#### 規約37 ★★ インデントは半角スペース4文字とする

タブ文字は使用しない。

#### 規約40 ★ 無駄な空行は入れず意味のある切れ目で入れる

- `package` 文と `import` 文の間は1行空ける
- `import` 文の最後とクラス宣言の間は1行空ける
- メソッド定義とメソッド定義の間は1行空ける

#### 規約41 ★★ 1行に2つ以上のステートメントを書かない

```java
// 良い例
index[0] = 10;
index[1] = 19;

// 悪い例
index[0] = 10; index[1] = 19;
```

#### 規約42 ★ 比較演算子は `<` か `<=` を使う

不等号の向きを統一して読み誤りを防ぐ。

```java
// 良い例
if (0 < visitOrder) { }
if (visitOrder <= maxOrder) { }
```

#### 規約44 ★★ カンマ・セミコロン・コロンの後ろや演算子の前後に空白を入れる

```java
// 良い例
int result = a + b;
for (int i = 0; i < list.size(); i++) { }

// 悪い例
int result=a+b;
for(int i=0;i<list.size();i++) { }
```

---

### 2.3 クラス定義

#### 規約32 ★★ クラス定義の記述順序を守る

1. package 文
2. import 文
3. クラスの Javadoc
4. class / interface 宣言
5. static フィールド (クラス変数)
6. インスタンス変数
7. コンストラクタ
8. メソッド

---

### 2.4 メソッド定義

#### 規約49 ★★ メソッドの最大行数は150行とする

超える場合はサブメソッドに処理を委譲してリファクタリングする。

#### 規約50 ★★ 循環的複雑度の上限を19とする

| 複雑度 | 評価 |
|--------|------|
| 10以下 | 読みやすく、テスト容易 |
| 11〜19 | リファクタリングの要否をレビュー |
| 20以上 | リファクタリング必須 |
| 50超 | テスト不能 |

#### 規約52 ★ メソッドの引数の順序には根拠がある

- 重要なインプットを先頭に
- オブジェクト型を先に、プリミティブ型を後に
- 対称性のあるメソッド同士はパラメーターの並べ方を揃える

#### 規約53 ★ 配列やコレクションを返すメソッドではnullを返さない

空コレクション (`Collections.emptyList()` 等) を返す。

```java
// 良い例
public List<SpotVisit> findVisits(Long dayId) {
    if (dayId == null) {
        return Collections.emptyList();
    }
    return repository.findByItineraryDayId(dayId);
}

// 悪い例
public List<SpotVisit> findVisits(Long dayId) {
    if (dayId == null) {
        return null; // 呼び出し元でnullチェックが必要になる
    }
    return repository.findByItineraryDayId(dayId);
}
```

#### 規約54 ★★ 引数の数は少なく保つ (上限10個)

同じ型の引数が多数並ぶことも避ける。引数が多い場合は Bean に束ねることを検討する。

#### 規約55 ★ 引数の正当性を検査する

`public` メソッドとコンストラクタの引数は、正当性を検査してから内部処理に引き継ぐ。

```java
// 良い例
public void addSpotToDay(Long dayId, Long spotId) {
    if (dayId == null || spotId == null) {
        throw new IllegalArgumentException("dayId と spotId は必須です");
    }
    // 処理...
}
```

---

### 2.5 変数・定数

#### 規約57 ★ 定数は `static final` で宣言する

```java
private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
public static final int MAX_MEMBER_COUNT = 100;
```

#### 規約58 ★ リテラルは原則として使わず定数を使う

0、""、null、true、false を除くリテラルは定数化する。

```java
// 良い例
private static final int MAX_MEMO_LENGTH = 1000;
if (memo.length() > MAX_MEMO_LENGTH) { }

// 悪い例
if (memo.length() > 1000) { }
```

#### 規約59 ★★ 配列の宣言は型名に `[]` を付ける

```java
// 良い例
String[] dateArray;
int[] totalCounter;

// 悪い例
String dateArray[];
int totalCounter[];
```

#### 規約64 ★ インスタンス変数は private で宣言する

フィールドは原則 `private`。ゲッター・セッターは必要なものだけ定義する。

#### 規約68 ★★ ローカル変数名とフィールド名が同じになることを回避する

#### 規約70 ★ ローカル変数は使用する直前に宣言と初期化を行う

---

### 2.6 文字列操作

#### 規約71 ★ 更新される文字列には StringBuilder を使用する

```java
// 良い例 (ループ内での文字列連結)
StringBuilder sb = new StringBuilder();
for (String name : names) {
    sb.append(name).append(", ");
}

// 悪い例 (ループ内で String 連結はオブジェクトが大量生成される)
String result = "";
for (String name : names) {
    result += name + ", ";
}
```

#### 規約72 ★ 更新されない文字列には String を使用する

---

### 2.10 制御構造

#### 規約84 ★★ 制御文の `{}` は省略しない

```java
// 良い例
if (hasErrors()) {
    return "form";
}

// 悪い例
if (hasErrors())
    return "form";
```

#### 規約86 ★ for 文と while 文を正しく使い分ける

| 文 | 使用場面 |
|----|---------|
| `for` | カウンタ・イテレータを使って回数が決まっているループ |
| 拡張 `for` | 配列・コレクションの全要素に処理を適用する |
| `while` | カウンタ以外の条件でループを終了する |
| `do-while` | 最低1回実行する必要があるループ |

#### 規約88 ★ 配列やコレクションを処理するループに拡張 for 文を使う

```java
// 良い例
for (SpotVisit visit : visits) {
    visit.setVisitOrder(i++);
}

// 推奨しない
for (int i = 0; i < visits.size(); i++) {
    visits.get(i).setVisitOrder(i);
}
```

#### 規約91 ★ switch 文では case ごとに break を書く

#### 規約92 ★★ switch 文では default を必ず書き break も書く

---

### 2.11 コレクション

#### 規約95 ★ オブジェクト集合の処理には Stream API を使用する (Java 8+)

```java
// 良い例
List<String> names = spots.stream()
        .map(Spot::getName)
        .collect(Collectors.toList());

// 推奨しない (拡張for文でも問題はないが Stream の方が宣言的)
List<String> names = new ArrayList<>();
for (Spot spot : spots) {
    names.add(spot.getName());
}
```

---

### 2.13 例外処理

#### 規約100 catch 文でキャッチする例外は詳細な例外クラスでキャッチする

```java
// 良い例
try {
    service.addSpot(dayId, spotId);
} catch (IllegalArgumentException e) {
    // ビジネスエラーは専用メッセージ
    redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
} catch (Exception e) {
    // システムエラーはログに記録し汎用メッセージを返す
    log.error("予期しないエラー: dayId={}", dayId, e);
    redirectAttributes.addFlashAttribute("errorMessage", "処理に失敗しました。");
}

// 悪い例
try {
    service.addSpot(dayId, spotId);
} catch (Exception e) {
    // Exception で全部捕まえると問題の特定が困難
}
```

#### 規約101 ★★ Exception クラスのオブジェクトを生成してスローしない

```java
// 良い例
throw new IllegalArgumentException("スポットが見つかりません: id=" + id);

// 悪い例
throw new Exception("エラー");
```

#### 規約103 ★★ catch ブロックでは必ず処理をする

空の catch ブロックは禁止。最低でもログ出力を行う。

```java
// 良い例
} catch (Exception e) {
    log.error("処理中にエラーが発生しました", e);
}

// 悪い例
} catch (Exception e) {
    // 何もしない ← 禁止
}
```

---

### 2.9 インスタンス

#### 規約82 ★★ オブジェクト同士は equals メソッドで比較する

```java
// 良い例
if (spotName.equals("道後温泉")) { }
if (spot == null) { }       // null との比較は == を使う

// 悪い例
if (spotName == "道後温泉") { } // 参照比較になり期待通り動かない
```

#### 規約80 ★★ equals を実装した場合は hashCode も実装する

#### 規約83 ★ instanceof をキャスト可否判断に使う

---

## 3. コメントおよびアノテーション規約

### 3.1 全般

#### 規約107 ★ コメントは説明したいコードの直前の行に記述する

#### 規約108 ★★★ コメントは必要なものだけを簡潔に書く

- コードを読めばわかることはコメントしない
- **なぜ** そのコードが必要かを書く（**何を** するかはコードが説明する）

```java
// 良い例 (WHY を説明)
// H2コンソールはiframeを使用するため SAMEORIGIN を許可 (デフォルトは DENY)
.frameOptions(frame -> frame.sameOrigin())

// 悪い例 (WHAT しか説明していない)
// frameOptionsを設定する
.frameOptions(frame -> frame.sameOrigin())
```

#### 規約109 コメントアウトしたプログラムの断片を放置しない

### 3.3 アノテーション

#### 規約111 ★ オーバーライドするメソッドには `@Override` アノテーションを使用する

```java
// 良い例
@Override
public String toString() { }

// 悪い例 (@Override なし)
public String toString() { }
```

---

## 4. Checkstyle による自動チェック

本プロジェクトでは `checkstyle.xml` に基づいて以下のルールを自動検証します。

### チェック実行方法

```bash
# 規約チェックのみ実行
./mvnw checkstyle:check

# ビルド + テスト + 規約チェック (CI/CD でこのコマンドを使う)
./mvnw verify
```

### 自動チェック対象の主要ルール

| カテゴリ | チェック内容 | 対応規約 |
|---------|-------------|---------|
| 命名 | パッケージ名が小文字のみか | 規約3 |
| 命名 | クラス名が PascalCase か | 規約7 |
| 命名 | メソッド名が camelCase か | 規約14 |
| 命名 | 変数名が camelCase か | 規約20 |
| 命名 | 定数名が UPPER\_SNAKE\_CASE か | 規約24 |
| スタイル | 行の長さが 110 文字以内か | 規約33 |
| スタイル | タブ文字を使っていないか | 規約37 |
| スタイル | インデントが4スペースか | 規約37 |
| スタイル | `{` が行末にあるか | 規約35 |
| スタイル | 制御文に `{}` があるか | 規約84 |
| スタイル | 1行に複数ステートメントがないか | 規約41 |
| メソッド | メソッドが 150 行以内か | 規約49 |
| メソッド | 循環的複雑度が 19 以内か | 規約50 |
| メソッド | 引数が 10 個以内か | 規約54 |
| 例外 | catch ブロックが空でないか | 規約103 |
| インポート | 未使用 import がないか | 規約27 |
| アノテーション | @Override が付いているか | 規約111 |
| その他 | equals 実装時に hashCode もあるか | 規約80 |

### 自動チェックの対象外 (レビューで確認するルール)

以下のルールは静的解析では検出が難しいため、コードレビューで確認する。

- 規約1: 名前に英単語を使っているか (ローマ字混在の検出)
- 規約6: クラス名が役割を表しているか (意味論的な判断が必要)
- 規約13: メソッド名が目的を表しているか
- 規約15: 対称性のあるメソッドが適切に命名されているか
- 規約108: コメントが必要なものだけか

---

## 5. 用語辞書

本プロジェクトで使用する日英対応表。

| 日本語 | 英語 | 備考 |
|--------|------|------|
| 旅程 | itinerary | |
| 日程 | day / itinerary day | |
| スポット | spot | |
| 訪問記録 | spot visit | |
| 観光地 | sightseeing | |
| 飲食店 | restaurant | |
| 宿泊施設 | accommodation | |
| 交通 | transport | |
| 追加する | add | |
| 削除する | remove / delete | 一覧から外す場合は remove |
| 登録する | register / create | |
| 更新する | update | |
| 検索する | find / search | DB 検索は find、全文検索は search |
| 検証する | validate | |
| 並び替える | reorder | |
