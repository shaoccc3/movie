package com.ispan.theater.repository;

import com.ispan.theater.domain.Screening;
import com.ispan.theater.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {


    @Query("delete from Ticket  c where c.screening = :screening")
    @Modifying
    void deleteByScreening(@Param("screening") Screening screening);

}