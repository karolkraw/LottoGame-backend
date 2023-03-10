package pl.lotto.numberreceiver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.lotto.numberreceiver.dto.LotteryTicketDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class NumberReceiverFacadeTest {

    TicketRepository ticketRepository;
    LocalDateTime randomDate;
    LocalDateTime drawDate;

    @BeforeEach
    void init() {
        ticketRepository = new TicketRepositoryInMemoryImpl();
        randomDate = LocalDateTime.of(2017, 2, 13, 15, 56);
        //clock = Clock.fixed(randomDate.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        drawDate = LocalDateTime.now().with(next(SATURDAY)).withHour(12).withMinute(0).withSecond(0).withNano(0);
    }

    @ParameterizedTest
    @MethodSource("createListsWithNumbersOutOfRange")
    public void should_return_invalid_ticket_when_user_gave_six_numbers_out_of_range(List<Integer> numbersFromUser) {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createFacadeForTest(ticketRepository);

        //when
        LotteryTicketDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertAll(() -> assertThat(result.id()).isNull(),
                () -> assertThat(result.numbers()).isEqualTo(numbersFromUser),
                () -> assertThat(result.drawDate()).isNull(),
                () -> assertThat(result.message()).isEqualTo("invalid"));
    }

    private static Stream<Arguments> createListsWithNumbersOutOfRange() {
        return Stream.of(
                Arguments.of(List.of(0, 1, 2, 3, 4, 5)),
                Arguments.of(List.of(1, 2, 3, 4, 5, 100))
        );
    }

    @Test
    public void should_return_ticket_with_correct_credentials_when_user_gave_six_numbers_in_range_of_1_to_99() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createFacadeForTest(ticketRepository);
        List<Integer> numbersFromUser = List.of(1, 2, 3, 4, 5, 6);

        //when
        LotteryTicketDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        assertAll(() -> assertThat(result.id()).isNotNull(),
                () -> assertThat(result.numbers()).isEqualTo(numbersFromUser),
                () -> assertThat(result.drawDate()).isEqualTo(drawDate.toString()),
                () -> assertThat(result.message()).isEqualTo("valid"));
    }


    @ParameterizedTest
    @MethodSource("createListsWithWrongAmountOfNumbers")
    public void should_return_invalid_ticket_when_user_gave_not_six_numbers(List<Integer> numbersFromUser) {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createFacadeForTest(ticketRepository);

        // when
        LotteryTicketDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertAll(() -> assertThat(result.id()).isNull(),
                () -> assertThat(result.numbers()).isEqualTo(numbersFromUser),
                () -> assertThat(result.drawDate()).isNull(),
                () -> assertThat(result.message()).isEqualTo("invalid"));
    }

    private static Stream<Arguments> createListsWithWrongAmountOfNumbers() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of(1, 2, 3, 4, 5)),
                Arguments.of(List.of(1, 2, 3, 4, 5, 6, 7)),
                Arguments.of(List.of(1))
        );
    }


    @Test
    public void should_return_invalid_ticket_when_user_gave_at_least_one_duplicate() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createFacadeForTest(ticketRepository);
        List<Integer> numbersFromUser = List.of(1, 2, 3, 4, 4, 5);

        // when
        LotteryTicketDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertAll(() -> assertThat(result.id()).isNull(),
                () -> assertThat(result.numbers()).isEqualTo(numbersFromUser),
                () -> assertThat(result.drawDate()).isNull(),
                () -> assertThat(result.message()).isEqualTo("invalid"));
    }


    @ParameterizedTest
    @MethodSource("createListsWithProperAmountOfNumbers")
    public void should_add_ticket_to_repository_when_valid_numbers_given(List<Integer> numbersFromUser) {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createFacadeForTest(ticketRepository);

        //when
        LotteryTicketDto ticket = numberReceiverFacade.inputNumbers(numbersFromUser);
        LotteryTicketDto result = numberReceiverFacade.retrieveUserNumbers(ticket.id());

        //Then
        assertThat(result.message()).isEqualTo("valid");
    }

    private static Stream<Arguments> createListsWithProperAmountOfNumbers() {
        return Stream.of(
                Arguments.of(List.of(1, 2, 3, 4, 5, 6)),
                Arguments.of(List.of(11, 22, 33, 44, 55, 66)),
                Arguments.of(List.of(1, 22, 38, 4, 59, 99))
        );
    }

    @Test
    void should_generate_unique_id() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createFacadeForTest(ticketRepository);
        Set<String> allUsersId = new HashSet<>();
        int repetitions = 50;
        int generatedIdByOneRepetition = 3;

        //when
        for (int i = 0; i < repetitions; i++) {
            String id = numberReceiverFacade.inputNumbers(List.of(1, 2, 3, 4, 5, 6)).id();
            allUsersId.add(id);
            id = numberReceiverFacade.inputNumbers(List.of(1, 3, 5, 7, 9, 11)).id();
            allUsersId.add(id);
            id = numberReceiverFacade.inputNumbers(List.of(22, 33, 1, 2, 3, 4)).id();
            allUsersId.add(id);
        }

        //then
        assertThat(allUsersId.size()).isEqualTo(repetitions * generatedIdByOneRepetition);
    }
}
