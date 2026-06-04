package com.example.travel;

import com.example.travel.entity.Itinerary;
import com.example.travel.entity.Spot;
import com.example.travel.repository.SpotRepository;
import com.example.travel.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 開発用サンプルデータ投入クラス
 *
 * @Profile("dev") → 開発プロファイルのときだけ実行される
 * アプリ起動時に自動でサンプルデータをDBに入れてくれる
 */
@Component
@Profile("dev")  // 本番環境では実行されない
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ItineraryService itineraryService;
    private final SpotRepository spotRepository;

    @Override
    public void run(String... args) {
        log.info("=== サンプルデータを投入します ===");

        // スポットデータ
        Spot spot1 = new Spot();
        spot1.setName("栗林公園");
        spot1.setAddress("香川県高松市栗林町1丁目20-16");
        spot1.setLatitude(34.3377);
        spot1.setLongitude(134.0558);
        spot1.setCategory(Spot.SpotCategory.SIGHTSEEING);
        spot1.setDescription("国の特別名勝。江戸時代の回遊式庭園。");
        spotRepository.save(spot1);

        Spot spot2 = new Spot();
        spot2.setName("道後温泉本館");
        spot2.setAddress("愛媛県松山市道後湯之町5-6");
        spot2.setLatitude(33.8522);
        spot2.setLongitude(132.7858);
        spot2.setCategory(Spot.SpotCategory.ACCOMMODATION);
        spot2.setDescription("日本最古の温泉の一つ。");
        spotRepository.save(spot2);

        Spot spot3 = new Spot();
        spot3.setName("金刀比羅宮");
        spot3.setAddress("香川県仲多度郡琴平町892-1");
        spot3.setLatitude(34.1853);
        spot3.setLongitude(133.8126);
        spot3.setCategory(Spot.SpotCategory.SIGHTSEEING);
        spot3.setDescription("こんぴらさん。785段の石段で有名。");
        spotRepository.save(spot3);

        // サンプル旅程: 四国旅行
        Itinerary shikoku = new Itinerary();
        shikoku.setTitle("四国一周の旅");
        shikoku.setDepartureLocation("東京");
        shikoku.setStartDate(LocalDate.of(2026, 7, 20));
        shikoku.setEndDate(LocalDate.of(2026, 7, 25));
        shikoku.setMemberCount(3);
        shikoku.setNotes("Sunrise Setoで行く四国旅行！高松→松山→高知のルート");
        itineraryService.create(shikoku);

        // サンプル旅程: 沖縄
        Itinerary okinawa = new Itinerary();
        okinawa.setTitle("沖縄リフレッシュ旅行");
        okinawa.setDepartureLocation("東京");
        okinawa.setStartDate(LocalDate.of(2026, 8, 10));
        okinawa.setEndDate(LocalDate.of(2026, 8, 13));
        okinawa.setMemberCount(2);
        itineraryService.create(okinawa);

        log.info("=== サンプルデータ投入完了 ===");
    }
}
