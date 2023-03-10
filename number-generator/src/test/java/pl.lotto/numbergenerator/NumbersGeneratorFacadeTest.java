package pl.lotto.numbergenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lotto.numbergenerator.dto.WinningNumbersDto;

import java.time.*;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.temporal.TemporalAdjusters.next;

class NumbersGeneratorFacadeTest {

    WinningNumbersRepository winningNumbersRepositoryTest;
    LocalDateTime drawDate;

    @BeforeEach
    void init() {
        // sample date to get next saturday of it
        LocalDateTime randomDate = LocalDateTime.of(2017, 2, 13, 15, 56);
        drawDate = randomDate.with(next(SATURDAY)).withHour(12).withMinute(0);

        winningNumbersRepositoryTest = new WinningNumbersRepositoryTest();
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        winningNumbersRepositoryTest.insert(new WinningNumbers(UUID.randomUUID().toString(), numbers, drawDate.toString()));
    }

    @Test
    void should_return_correct_numbers_when_winning_numbers_are_generated() {
        //given
        NumbersGeneratorFacade numbersGeneratorFacade = new NumbersGeneratorFacadeConfiguration()
                .createFacadeForTest(winningNumbersRepositoryTest);

        //when
        WinningNumbersDto result = numbersGeneratorFacade.retrieveWonNumbers(drawDate.toString());
        List<Integer> numbers = result.winningNumbers();
        int correctDigits = (int) numbers.stream()
                .filter(a -> a > 0 && a < 100)
                .distinct().count();

        //then
        assertThat(correctDigits).isEqualTo(6);
    }

    @Test
    void should_return_null_when_draw_not_completed() {
        //given
        LocalDateTime date = drawDate.plusDays(7);
        NumbersGeneratorFacade numbersGeneratorFacade = new NumbersGeneratorFacadeConfiguration()
                .createFacadeForTest(winningNumbersRepositoryTest);

        //when
        WinningNumbersDto result = numbersGeneratorFacade.retrieveWonNumbers(date.toString());

        //then
        assertThat(result.winningNumbers()).isNull();
    }
}