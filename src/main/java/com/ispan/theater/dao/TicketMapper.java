package com.ispan.theater.dao;


import com.ispan.theater.domain.Ticket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface TicketMapper {

    @Insert({
            "<script>",
            "INSERT INTO Ticket (Screening_id, auditorium_id, seat_id, is_available, movie_id, create_date, modify_date)",
            "VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(",
            "#{item.screening.id},",
            "#{item.auditorium.id},",
            "#{item.seat.id},",
            "#{item.isAvailable},",
            "#{item.movie.id},",
            "#{item.createDate},",
            "#{item.modifyDate}",
            ")",
            "</foreach>",
            "</script>"
    })
    void insertBatch(List<Ticket> tickets);

    @Update({
            "<script>",
            "<foreach collection='list' item='item' separator=';'>",
            "UPDATE Ticket",
            "SET",
            "auditorium_id = #{item.auditorium.id},",
            "seat_id = #{item.seat.id},",
            "is_available = #{item.isAvailable},",
            "movie_id = #{item.movie.id},",
            "create_date = #{item.createDate},",
            "modify_date = #{item.modifyDate}",
            "WHERE Screening_id = #{item.screening.id}",
            "</foreach>",
            "</script>"
    })
    void updateBatch(@Param("list") List<Ticket> tickets);
}

