package com.webcrawling.sample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
    /*
     * 기본적인 findBySeq 나 findAll 등은 JpaRepository에 의해 문법 규칙에 따라 별도 정의 없이도 모두 사용 가능합니다!!!
     */

    /*
     * 아래는 직접 쿼리를 사용하고 싶을 때, 참조 예제입니다.
     *
     * 네이티브 쿼리를 사용할 때 주의할 점:
     * - 데이터베이스 종속적인 SQL을 사용하게 되므로, 데이터베이스를 변경할 경우 쿼리를 수정해야 할 수 있습니다.
     * - JPA의 엔티티 캐싱 기능을 우회하므로 성능에 영향을 줄 수 있습니다.
     * - 엔티티의 필드 이름이 변경되어도 네이티브 쿼리는 자동으로 업데이트되지 않으므로 주의가 필요합니다.
     */

    // 모든 샘플 엔티티를 이름으로 정렬하여 가져오기
    @Query(value = "SELECT * FROM ba1_sample ORDER BY name", nativeQuery = true)
    List<SampleEntity> findAllSortedByName();

    // 특정 이름을 포함하는 샘플 엔티티 찾기
    @Query(value = "SELECT * FROM ba1_sample WHERE name LIKE %:name%", nativeQuery = true)
    List<SampleEntity> findByNameContaining(@Param("name") String name);

    // 특정 seq 범위 내의 샘플 엔티티 찾기
    @Query(value = "SELECT * FROM ba1_sample WHERE seq BETWEEN :start AND :end", nativeQuery = true)
    List<SampleEntity> findBySeqBetween(@Param("start") Long start, @Param("end") Long end);

    // 이름의 길이가 특정 값보다 큰 샘플 엔티티 찾기
    @Query(value = "SELECT * FROM ba1_sample WHERE LENGTH(name) > :length", nativeQuery = true)
    List<SampleEntity> findByNameLengthGreaterThan(@Param("length") int length);

    // 샘플 엔티티의 총 개수 가져오기
    @Query(value = "SELECT COUNT(*) FROM ba1_sample", nativeQuery = true)
    long countTotalSamples();

    // 특정 이름을 가진 샘플 엔티티의 seq 업데이트하기
    @Query(value = "UPDATE ba1_sample SET seq = :newSeq WHERE name = :name", nativeQuery = true)
    void updateSeqByName(@Param("newSeq") Long newSeq, @Param("name") String name);

}