package uos.cineseoul.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.enums.SeatState;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class ScheduleSeatServiceTest {
    @Autowired
    ScheduleSeatService scheduleSeatService;
    @Autowired
    ScheduleSeatRepository scheduleSeatRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ScreenRepository screenRepository;

    @Autowired
    SeatRepository seatRepository;

    // 픽스처
    Screen screen = Screen.mock();
    Seat seat = Seat.mock(screen);
    Movie movie = Movie.mock();
    Schedule schedule = Schedule.mock(screen,movie);
    ScheduleSeat scheduleSeat;

    @BeforeEach
    public void init(){
        // 상영관 등록
        screenRepository.save(screen);

        // 상영관에 좌석 등록
        seat = seatRepository.save(seat);
        screen.getSeats().add(seat);

        // 영화 등록
        movieRepository.save(movie);

        // 상영일정 등록
        scheduleRepository.save(schedule);

        // 상영일정_좌석 등록
        scheduleSeatService.insertScheduleSeat(schedule,screen);
        scheduleSeat = scheduleSeatService.findScheduleSeat(schedule.getSchedNum(), seat.getSeatNum());
    }

    @AfterEach
    public void destory(){
        scheduleSeatRepository.deleteAll();
        scheduleRepository.deleteAll();
        movieRepository.deleteAll();
        seatRepository.deleteAll();
        screenRepository.deleteAll();
    }

    @DisplayName("selectScheduleSeat : 상영일정_좌석번호로 상영일정_좌석을 성공적으로 선택한다.")
    @Test
    public void selectScheduleSeat() {
        //given
        //when
        scheduleSeatService.selectScheduleSeat(scheduleSeat.getScheduleSeatNum());
        scheduleSeat = scheduleSeatService.findScheduleSeat(scheduleSeat.getScheduleSeatNum());

        //then
        Assertions.assertThat(scheduleSeat.getState()).isEqualTo(SeatState.SELECTED);
    }

    @DisplayName("selectScheduleSeat concurrently : 동시에 여러 사람이 상영일정_좌석번호로 상영일정_좌석을 선택할 때 성공적으로 한 명만 선택된다.")
    @Test
    public void selectScheduleSeatByNumConcurrently() throws Exception {
        //given
        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger atomicInt = new AtomicInteger(0);
        //when
        //then
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    scheduleSeatService.selectScheduleSeat(scheduleSeat.getScheduleSeatNum());
                    System.out.println(Thread.currentThread().getName() + ": 성공!!!");
                    atomicInt.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(Thread.currentThread().getName() + ": 실패...");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        System.out.println(atomicInt.get() + "명 좌석 선택 성공!!");
        Assertions.assertThat(atomicInt.get()).isEqualTo(1);
    }

    @DisplayName("selectScheduleSeat concurrently failed: 동시에 여러 사람이 상영일정_좌석번호로 상영일정_좌석을 선택할 때 한 명만 선택되지 않는다.")
    @Test
    public void selectScheduleSeatByNumConcurrentlyFailed() throws Exception {
        //given
        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger atomicInt = new AtomicInteger(0);
        //when
        //then
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    scheduleSeatService.selectScheduleSeatWithoutLocking(scheduleSeat.getScheduleSeatNum());
                    System.out.println(Thread.currentThread().getName() + ": 성공!!!");
                    atomicInt.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(Thread.currentThread().getName() + ": 실패...");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        System.out.println(atomicInt.get() + "명 좌석 선택 성공!!");
        Assertions.assertThat(atomicInt.get()).isNotEqualTo(1);
    }

}