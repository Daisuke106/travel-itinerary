package com.example.travel.repository;

import com.example.travel.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    /** スポット名であいまい検索 */
    List<Spot> findByNameContainingIgnoreCase(String keyword);

    /** カテゴリで絞り込み */
    List<Spot> findByCategory(Spot.SpotCategory category);
}
