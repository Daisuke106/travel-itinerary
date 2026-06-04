package com.example.travel.repository;

import com.example.travel.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 旅程リポジトリ
 *
 * JpaRepository を継承するだけで基本的なCRUDが使えるようになる。
 * findAll(), findById(), save(), deleteById() などが自動生成される。
 */
@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    /**
     * タイトルであいまい検索
     * メソッド名からSQLが自動生成される Spring Data の機能
     * → SELECT * FROM itineraries WHERE title LIKE %keyword%
     */
    List<Itinerary> findByTitleContainingIgnoreCase(String keyword);

    /**
     * 出発地で検索
     */
    List<Itinerary> findByDepartureLocation(String departureLocation);

    /**
     * 最新順に全件取得 (トップページ表示用)
     */
    List<Itinerary> findAllByOrderByCreatedAtDesc();
}
