package roomescape.repository.domain.reservationtime.implement;

import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.reservationtime.ReservationTimes;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.reservationtime.vo.ReservationTimeStartAt;
import roomescape.repository.domain.reservationtime.ReservationTimeJdbcRepository;
import roomescape.repository.domain.reservationtime.entity.ReservationTimeEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReservationTimeRepositoryImpl implements ReservationTimeRepository {

    private final ReservationTimeJdbcRepository repository;

    public ReservationTimeRepositoryImpl(ReservationTimeJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        return repository.save(ReservationTimeEntity.from(reservationTime))
                .toDomain();
    }

    @Override
    public Optional<ReservationTime> findById(ReservationTimeId id) {
        return repository.findById(id.id())
                .map(ReservationTimeEntity::toDomain);
    }

    @Override
    public boolean existByStartAt(ReservationTimeStartAt startAt) {
        return repository.findByStartAt(startAt.startAt()).isPresent();
    }

    @Override
    public ReservationTimes findAll() {
        return new ReservationTimes(
                repository.findAll().stream()
                        .map(ReservationTimeEntity::toDomain)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void delete(ReservationTimeId id) {
        repository.delete(id.id());
    }

    @Override
    public ReservationTimes findExcludeById(List<ReservationTimeId> ids) {
        return new ReservationTimes(
                repository.findExcludeById(
                                ids.stream().map(ReservationTimeId::id)
                                        .collect(Collectors.toList())
                        ).stream()
                        .map(ReservationTimeEntity::toDomain)
                        .collect(Collectors.toList())
        );
    }
}
