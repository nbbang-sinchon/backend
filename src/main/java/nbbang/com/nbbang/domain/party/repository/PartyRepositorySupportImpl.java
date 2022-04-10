package nbbang.com.nbbang.domain.party.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;

import static nbbang.com.nbbang.domain.party.domain.QParty.party;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PartyRepositorySupportImpl implements PartyRepositorySupport{
    private final JPAQueryFactory query;

    @Override
    public List<Party> findByPlaceAndNotSelf(Long partyId) {
        Party findParty = query.selectFrom(party).where(party.id.eq(partyId)).fetchOne();
        List<Party> parties = query.select(party)
                .from(party)
                .where(party.place.eq(findParty.getPlace()))
                .where(party.status.eq(PartyStatus.OPEN))
                .where(party.id.ne(findParty.getId()))
                .orderBy(party.createTime.desc())
                .limit(6)
                .fetch();
        return parties;
    }

    @Override
    public List<Party> findByPlaceAndNotSelf(Long partyId, Place place) {
        List<Party> parties = query.selectFrom(party)
                .where(party.place.eq(place))
                .where(party.status.eq(PartyStatus.OPEN))
                .where(party.id.ne(partyId))
                .orderBy(party.createTime.desc())
                .limit(6)
                .fetch();
        return parties;
    }

    @Override
    public Party findWithPartyMember(Long partyId) {
        return query.selectFrom(party)
                .where(party.id.eq(partyId))
                .leftJoin(party.partyMembers).fetchJoin()
                .fetchOne();
    }

}
