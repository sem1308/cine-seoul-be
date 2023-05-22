
package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.InsertTicketDTO;
import uos.cineseoul.dto.UpdateTicketDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.TicketMapper;
import uos.cineseoul.repository.ScheduleSeatRepository;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.UserRepository;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;

    @Autowired
    public TicketService(TicketRepository ticketRepo, UserRepository userRepo, ScheduleSeatRepository scheduleSeatRepo) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.scheduleSeatRepo = scheduleSeatRepo;
    }

    public List<Ticket> findAll() {
        List<Ticket> ticketList = ticketRepo.findAll();
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException("티켓이 없습니다.");
        }
        return ticketList;
    }

    public Ticket findOneByNum(Long num) {
        Ticket ticket = ticketRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 티켓이 없습니다.");
        });
        return ticket;
    }
    public List<Ticket> findOneByUserNum(Long userNum) {
        List<Ticket> ticketList = ticketRepo.findByUserNum(userNum);
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException(userNum+"번 유저에 대한 티켓이 없습니다.");
        }
        return ticketList;
    }
    public List<Ticket> findOneByUserId(String userId) {
        List<Ticket> ticketList = ticketRepo.findByUserID(userId);
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException("유저 "+userId+"에 대한 티켓이 없습니다.");
        }
        return ticketList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Ticket insert(InsertTicketDTO ticketDTO) {
        User user = userRepo.findById(ticketDTO.getUserNum()).orElseThrow(()->{
            throw new ResourceNotFoundException(ticketDTO.getUserNum()+"번 유저가 없습니다.");
        });

        ScheduleSeat scheduleSeat = scheduleSeatRepo.findBySchedNumAndSeatNum(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()).orElseThrow(()->{
            throw new ResourceNotFoundException("상영일정 "+ticketDTO.getSchedNum()+"또는 좌석 "+ticketDTO.getSeatNum()+"이 없습니다.");
        });

        if(scheduleSeat.getOccupied().equals("Y")){
            throw new DuplicateKeyException("상영일정 "+ticketDTO.getSchedNum()+", 좌석 "+ticketDTO.getSeatNum()+"에 대해 이미 예약 되어있습니다.");
        }

        scheduleSeat.setOccupied("Y");
        scheduleSeatRepo.save(scheduleSeat);

        Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDTO);
        ticket.setUser(user);
        ticket.setScheduleSeat(scheduleSeat);

        Ticket savedTicket = ticketRepo.save(ticket);

        return savedTicket;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Ticket update(UpdateTicketDTO ticketDTO) {
        Ticket ticket = ticketRepo.findById(ticketDTO.getTicketNum()).orElseThrow(()->{
            throw new ResourceNotFoundException(ticketDTO.getTicketNum()+"번 티켓이 없습니다.");
        });
        TicketMapper.INSTANCE.updateFromDto(ticketDTO,ticket);

        if(!ticketDTO.getSeatNum().equals(null) && !ticketDTO.getSeatNum().equals(ticket.getScheduleSeat().getSeat().getSeatNum())){
            ScheduleSeat ssBefore = ticket.getScheduleSeat();
            ScheduleSeat ssAfter = scheduleSeatRepo.findBySchedNumAndSeatNum(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()).orElseThrow(()->{
                throw new ResourceNotFoundException("상영일정 "+ticketDTO.getSchedNum()+"또는 좌석 "+ticketDTO.getSeatNum()+"이 없습니다.");
            });
            ssBefore.setOccupied("N");
            ssAfter.setOccupied("Y");
            scheduleSeatRepo.save(ssBefore);
            scheduleSeatRepo.save(ssAfter);

            ticket.setScheduleSeat(ssAfter);
        }

        Ticket updatedTicket = ticketRepo.save(ticket);

        return updatedTicket;
    }
}
