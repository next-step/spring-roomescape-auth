package nextstep.play;

import org.springframework.stereotype.Service;

@Service
public class PlayService {

    private final PlayDao playDao;

    public PlayService(PlayDao playDao) {
        this.playDao = playDao;
    }

    public Long play(PlayRequest request, Long memberId) {
        Play play = new Play(request.getReservationId(), memberId);
        return playDao.save(play);
    }

    public void delete(Long id, Long memberId) {
        playDao.updateHidden(id, true);
    }
}
