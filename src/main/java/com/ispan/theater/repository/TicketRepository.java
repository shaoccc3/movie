package com.ispan.theater.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
//	@Query(value="select t.* from Ticket as t join Screening as s on t.Screening_id=s.Screening_id where s.Screening_id = :screeningId ",nativeQuery = true)
//	public List<Map<String,Object>> getTickets(@Param("screeningId")Integer screeningId);
	
//	@Query(value="select t.* from Ticket as t join Screening as s on t.Screening_id=s.Screening_id where s.Screening_id = :screeningId ",nativeQuery = true)
//	public List<Ticket> getTickets1(@Param("screeningId")Integer screeningId);
	
//	@Query(value="select t from Ticket as t where t.screening.id = :screeningId ")
//	public List<Ticket> getTickets1(@Param("screeningId")Integer screeningId);
//	//t.screening.id
	
	@Query(value="select t.auditorium_id,t.seat_id,t.Screening_id,t.Ticket_id,t.is_available,m.name,s.Start_time from Ticket as t join movie as m on m.movie_id=t.movie_id join Screening as s on s.Screening_id=t.Screening_id where t.Screening_id = :screeningId ",nativeQuery = true)
	public List<Map<String,Object>> getTickets(@Param("screeningId")Integer screeningId);
	
	@Query(value="select * from Ticket as t where t.Ticket_id in(:Ticket_id) ",nativeQuery = true)
	public List<Ticket> findTicketsById(@Param("Ticket_id")List<Integer> ticketId);
	
	// update [test1].[dbo].[Ticket] set [is_available]='已售出' where Ticket_id in(287,299)
	@Modifying
	@Query(value="update Ticket set is_available= :available where Ticket_id in (:ticketId)",nativeQuery=true)
	public void setTicketAvailable(@Param("available")String available,@Param("ticketId")List<Integer> ticketId);
	
	@Query("select t from Ticket t where t.screening.id = :screeningId")
	public List<Ticket> test(@Param("screeningId")Integer screeningId);
	
}