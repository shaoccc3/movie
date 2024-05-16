package com.ispan.theater.repository;

import java.util.List;
import java.util.Map;

import com.ispan.theater.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
	@Query(value="select t.* from Ticket as t join Screening as s on t.Screening_id=s.Screening_id where s.Screening_id = :screeningId ",nativeQuery = true)
	public List<Map<String,Object>> getTickets(@Param("screeningId")Integer screeningId);
	
//	@Query(value="select t.* from Ticket as t join Screening as s on t.Screening_id=s.Screening_id where s.Screening_id = :screeningId ",nativeQuery = true)
//	public List<Ticket> getTickets1(@Param("screeningId")Integer screeningId);
	
//	@Query(value="select t from Ticket as t where t.screening.id = :screeningId ")
//	public List<Ticket> getTickets1(@Param("screeningId")Integer screeningId);
//	//t.screening.id
	
	@Query(value="select t.* from Ticket as t  where t.Screening_id = :screeningId ",nativeQuery = true)
	public List<Map<String,Object>> getTickets1(@Param("screeningId")Integer screeningId);
	
	@Query("select t from Ticket t where t.screening.id = :screeningId")
	public List<Ticket> test(@Param("screeningId")Integer screeningId);

	@Query("select count(t.id) from Ticket t where t.screening.id = :screeningId")
	public Integer existsByScreenCount(@Param("screeningId")Integer screeningId);

	@Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM Ticket t WHERE t.Screening_id = :screeningId) THEN 1 ELSE 0 END", nativeQuery = true)
	public int existsByScreenId(@Param("screeningId") Integer screeningId);

	@Query("select t from Ticket t where t.screening.id = :screeningid")
	public List<Ticket> findByScreenId(@Param("screeningid")Integer screeningid);



}