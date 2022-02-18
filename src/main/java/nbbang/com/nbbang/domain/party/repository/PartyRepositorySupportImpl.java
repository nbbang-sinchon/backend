package nbbang.com.nbbang.domain.party.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.domain.QParty;

import java.util.List;

@RequiredArgsConstructor
public class PartyRepositorySupportImpl implements PartyRepositorySupport{
    private final JPAQueryFactory query;
    QParty p = new QParty("p");


    @Override
    public List<Party> findByPlaceAndNotSelf(Long partyId) {
        Party party = query.selectFrom(p).where(p.id.eq(partyId)).fetchOne();
        List<Party> parties = query.select(p)
                .from(p)
                .where(p.place.eq(party.getPlace()))
                .where(p.status.eq(PartyStatus.ON))
                .where(p.id.ne(party.getId()))
                .fetch();
        return parties;
    }
}
