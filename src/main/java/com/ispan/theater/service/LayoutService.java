package com.ispan.theater.service;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Layout;
import com.ispan.theater.domain.LayoutId;
import com.ispan.theater.domain.Seat;
import com.ispan.theater.repository.LayoutRepository;
import com.ispan.theater.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LayoutService {
    private static final Logger log = LoggerFactory.getLogger(LayoutService.class);
    @Autowired
    private LayoutRepository layoutRepository;
    @Autowired
    private SeatRepository seatRepository;

    private static final int MAX_COL_NUM=24;
    public void insertLayout(Auditorium auditorium) {
        log.info("Inserting layout for auditorium ID: {}", auditorium.getId());
        for(int i=1;i<=13;i++){
            for(int j=1;j<=18;j++){
                Integer seatid = (i-1)*MAX_COL_NUM+j;
                Optional<Seat> optionalSeat = seatRepository.findById(seatid);
                if(optionalSeat.isPresent()){
                    Layout layout = new Layout();
                    Seat seat = optionalSeat.get();
                    LayoutId layoutId = new LayoutId(auditorium.getId(),seat.getId());
                    layout.setId(layoutId);
                    layout.setAuditorium(auditorium);
                    layout.setSeat(seat);
                    System.out.println(layout.getSeat().getId()+" "+layout.getAuditorium()+" "+layout.getId().getAuditoriumId()+" "+layout.getId().getSeatId());
                    layoutRepository.saveAndFlush(layout);
                } else {
                    log.warn("Seat not found for ID: {}", seatid);
                }
            }
        }
    }

    public List<Layout> getLayout(Auditorium auditorium){
        return layoutRepository.findByAuditorium(auditorium);
    }
}
