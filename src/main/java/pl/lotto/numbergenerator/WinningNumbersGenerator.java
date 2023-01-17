package pl.lotto.numbergenerator;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.temporal.TemporalAdjusters.next;

class WinningNumbersGenerator {


    private final NumbersGenerator numbersGenerator;
    private final WinningNumbersRepository winningNumbersRepository;
    private final Clock clock;



    WinningNumbersGenerator(NumbersGenerator numbersGenerator, WinningNumbersRepository winningNumbersRepository, Clock clock) {
        this.numbersGenerator = numbersGenerator;
        this.winningNumbersRepository = winningNumbersRepository;
        this.clock = clock;
    }

    public void generateWinningNumbers() {
        List<Integer> numbers = numbersGenerator.generateNumbers();

        LocalDateTime drawDate = LocalDateTime.now()
                .with(next(SATURDAY)).withHour(DrawTime.HOURS.value)
                .withMinute(DrawTime.MINUTES.value).withSecond(DrawTime.SECONDS.value).withNano(DrawTime.NANO.value);

        winningNumbersRepository.insert(new WinningNumbers(UUID.randomUUID(), numbers, drawDate.toString()));
    }
}