package com.example.travel.repository;

import com.example.travel.entity.SpotVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * スポット訪問記録リポジトリ
 *
 * 【なぜこのRepositoryが必要か】
 *   SpotVisit は ItineraryDay と Spot の中間テーブル。
 *   「この日程のスポットを順番通りに取得する」や
 *   「訪問順を並び替える」操作が頻出するため専用Repositoryを用意する。
 */
@Repository
public interface SpotVisitRepository extends JpaRepository<SpotVisit, Long> {

    /**
     * 指定した日程IDのスポット訪問を訪問順で全取得
     *
     * 【Spring Data の命名規則】
     *   findBy + フィールド名 + OrderBy + フィールド名 + Asc
     *   → SELECT * FROM spot_visits WHERE itinerary_day_id = ? ORDER BY visit_order ASC
     *   メソッド名からSQLが自動生成される！
     */
    List<SpotVisit> findByItineraryDayIdOrderByVisitOrderAsc(Long itineraryDayId);

    /**
     * 指定した日程の最大visitOrderを取得（次の訪問順を決めるために使う）
     *
     * 【@Queryアノテーション】
     *   Spring Dataの命名規則で書けない複雑なクエリは
     *   @Query で JPQL（JavaのORM用SQL）を直接書く。
     *   JPQL ではテーブル名ではなくエンティティ名(SpotVisit)を使う。
     */
    @Query("SELECT COALESCE(MAX(sv.visitOrder), 0) FROM SpotVisit sv WHERE sv.itineraryDay.id = :dayId")
    int findMaxVisitOrderByDayId(@Param("dayId") Long dayId);

    /**
     * 指定した日程の全スポット訪問を削除（日程ごとリセット用）
     *
     * 【@Modifying】
     *   SELECT以外（INSERT/UPDATE/DELETE）の @Query には必須のアノテーション。
     *   これがないとSpring Dataが「読み取り専用クエリ」と誤解してエラーになる。
     */
    @Modifying
    @Query("DELETE FROM SpotVisit sv WHERE sv.itineraryDay.id = :dayId")
    void deleteAllByItineraryDayId(@Param("dayId") Long dayId);

    /**
     * 指定した visitId が dayId に属するか確認 (IDOR対策)
     *
     * 別ユーザーのvisitIdをURLに指定しても操作できないようにする。
     */
    boolean existsByIdAndItineraryDayId(Long id, Long itineraryDayId);
}