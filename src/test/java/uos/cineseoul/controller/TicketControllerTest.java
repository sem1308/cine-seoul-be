package uos.cineseoul.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.create.CreateTicketAudienceDTO;
import uos.cineseoul.dto.create.CreateTicketDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.repository.*;
import uos.cineseoul.service.ScheduleSeatService;
import uos.cineseoul.util.JwtTokenUtil;
import uos.cineseoul.utils.JwtTokenProvider;
import uos.cineseoul.utils.enums.AudienceType;
import uos.cineseoul.utils.enums.TicketState;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class TicketControllerTest {
    @Autowired
    MockMvc mockMvc;

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

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenProvider tokenProvider;

    // 픽스처
    Screen screen = Screen.mock();
    Seat seat = Seat.mock(screen);
    Movie movie = Movie.mock();
    Schedule schedule = Schedule.mock(screen,movie);
    ScheduleSeat scheduleSeat;

    User user = User.mock();
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

        userRepository.save(user);

        // 좌석 선택
        scheduleSeat.select(user);
    }

//    @Test
//    void lookUpTicketList() {
//    }
//
//    @Test
//    void lookUpAudienceTypeList() {
//    }
//
//    @Test
//    void lookUpTicketByNum() {
//    }

    @DisplayName("register : 티켓 등록에 성공한다.")
    @Test
    void register() throws Exception {
        // given
        String url = "/ticket/auth";

        Integer stdPrice = seat.getSeatGrade().getPrice();
        Integer salePrice = stdPrice - AudienceType.G.getDiscountPrice();

        List<Long> seatNumList = new ArrayList<>();
        seatNumList.add(seat.getSeatNum());

        List<CreateTicketAudienceDTO> audienceTypeDTOList = new ArrayList<>();
        audienceTypeDTOList.add(CreateTicketAudienceDTO.builder().audienceType(AudienceType.G).count(1).build());

        CreateTicketDTO createTicketDTO = CreateTicketDTO.builder()
            .schedNum(schedule.getSchedNum())
            .stdPrice(stdPrice)
            .seatNumList(seatNumList)
            .audienceTypeDTOList(audienceTypeDTOList)
            .build();

        String jsonCreateTicketDTO = objectMapper.writeValueAsString(createTicketDTO);

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(url)
            .content(jsonCreateTicketDTO)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(JwtTokenProvider.HEADER_NAME, JwtTokenUtil.createToken(tokenProvider, user)));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ticketSeats.size()").value(seatNumList.size()))
            .andExpect(jsonPath("$.user.userNum").value(user.getUserNum()))
            .andExpect(jsonPath("$.schedule.schedNum").value(schedule.getSchedNum()))
            .andExpect(jsonPath("$.ticketState").value(TicketState.N.toString()))
            .andExpect(jsonPath("$.stdPrice").value(stdPrice))
            .andExpect(jsonPath("$.salePrice").value(salePrice));
    }

//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
//
//    @Test
//    void cancelAndRegister() {
//    }
}