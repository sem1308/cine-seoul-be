package uos.cineseoul.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.dto.insert.InsertScreenDTO;
import uos.cineseoul.dto.insert.InsertSeatDTO;
import uos.cineseoul.dto.request.SelectScheduleSeatDto;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.repository.MovieRepository;
import uos.cineseoul.utils.enums.SeatGrade;
import uos.cineseoul.utils.enums.SeatState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class ScheduleSeatServiceTest {
    @Autowired
    ScheduleSeatService scheduleSeatService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ScreenService screenService;

    @Autowired
    SeatService seatService;

    @Autowired
    ObjectMapper objectMapper;

    // 픽스처
    Schedule schedule;
    Seat seat;
    @BeforeEach
    public void init(){
        // 상영관 등록
        InsertScreenDTO insertScreenDTO = InsertScreenDTO.builder().name("상영관 A").build();
        Screen screen = screenService.insert(insertScreenDTO);

        // 상영관에 좌석 등록
        InsertSeatDTO insertSeatDTO = InsertSeatDTO.builder().screen(screen).seatGrade(SeatGrade.B).col("1").row("A").build();
        seat = seatService.insert(insertSeatDTO);

        List<Seat> seatList = new ArrayList<>();
        seatList.add(seat);
        screen.setSeats(seatList);

        Movie movie = Movie.mock();
        movieRepository.save(movie); // test를 위한 영화 mock object 생성

        InsertScheduleDTO insertScheduleDTO = InsertScheduleDTO.builder().screen(screen).order(1).schedTime(LocalDateTime.now()).movie(movie).build();
        schedule = scheduleService.insert(insertScheduleDTO);
    }

    @DisplayName("selectScheduleSeat : 상영일정_좌석번호로 상영일정_좌석을 성공적으로 선택한다.")
    @Test
    public void selectScheduleSeat() {
        //given
        ScheduleSeat scheduleSeat = scheduleSeatService.findScheduleSeat(schedule.getSchedNum(),seat.getSeatNum());

        //when
        scheduleSeatService.selectScheduleSeat(scheduleSeat.getScheduleSeatNum());

        //then
        Assertions.assertThat(scheduleSeat.getState()).isEqualTo(SeatState.SELECTED);
    }


    @DisplayName("selectScheduleSeat concurrently : 동시에 여러 사람이 상영일정_좌석번호로 상영일정_좌석을 선택할 때 성공적으로 한 명만 선택된다.")
    @Test
    public void selectScheduleSeatByNumConcurrently() throws Exception {
        //given
        int numberOfThreads = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        ScheduleSeat scheduleSeat = scheduleSeatService.findScheduleSeat(schedule.getSchedNum(),seat.getSeatNum());

        AtomicInteger atomicInt = new AtomicInteger(0);
        //when
        //then

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    scheduleSeatService.selectScheduleSeat(scheduleSeat.getScheduleSeatNum());
                    atomicInt.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + ": 성공!!!");
                } catch (IllegalStateException e) {
                    System.out.println(Thread.currentThread().getName() + ": 실패...");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Assertions.assertThat(atomicInt.get()).isEqualTo(1);
    }
//
//    @DisplayName("selectScheduleSeat concurrently : 동시에 여러 사람이 상영일정_좌석번호로 상영일정_좌석을 선택할 때 한 명만 선택되지 않는다.")
//    @Test
//    public void selectScheduleSeatByNumConcurrentlyFailed() throws Exception {
//        //given
//        int numberOfThreads = 3;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//
//        ScheduleSeat scheduleSeat = scheduleSeatService.findScheduleSeat(schedule.getSchedNum(),seat.getSeatNum());
//        String url = "/schedule/seat/select/"+scheduleSeat.getScheduleSeatNum()+"/no";
//
//        AtomicInteger atomicInt = new AtomicInteger(0);
//        //when
//        //then
//
//        for (int i = 0; i < numberOfThreads; i++) {
//            executorService.submit(() -> {
//                try {
//                    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url))
//                        .andReturn();
//
//                    System.out.println(Thread.currentThread().getName() + ": " + result.getResponse().getContentAsString());
//                    if(result.getResponse().getStatus() == HttpStatus.OK.value()){
//                        atomicInt.incrementAndGet();
//                        System.out.println("[성공!!!!]");
//                    }else{
//                        System.out.println("[실패....]");
//                    }
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        executorService.shutdown();
//
//        Assertions.assertThat(atomicInt.get()).isNotEqualTo(1);
//    }
}