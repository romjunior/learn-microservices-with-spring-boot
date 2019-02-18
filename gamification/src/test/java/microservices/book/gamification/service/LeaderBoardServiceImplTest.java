package microservices.book.gamification.service;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class LeaderBoardServiceImplTest {

    private LeaderBoardServiceImpl leaderBoardService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
    }

    @Test
    public void retrieveLeaderBoardTest() {
        //given
        Long userId = 1L;
        LeaderBoardRow leaderBoardRow = new LeaderBoardRow(userId, 300L);
        List<LeaderBoardRow> expectedLeaderBoard = Collections.singletonList(leaderBoardRow);
        BDDMockito.given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoard);

        //when
        List<LeaderBoardRow> leaderBoard = leaderBoardService.getCurrentLeaderBoard();

        //then
        Assertions.assertThat(leaderBoard).isEqualTo(expectedLeaderBoard);

    }
}
