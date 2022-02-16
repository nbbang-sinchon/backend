package nbbang.com.nbbang.domain.party.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.domain.QParty;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ManyPartyRepositorySupportImpl implements ManyPartyRepositorySupport {

    private final JPAQueryFactory query;

    @Override
    public Page<Party> findAllByRequestDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto) {
        System.out.println("===========-=-=-=-=-=-=ㅇㅅㅇ=-=-=-=====================");
        QParty party = QParty.party;
        JPQLQuery<Party> q = query.selectFrom(party)
                .where(party.title.contains(requestFilterDto.getSearch()));
        if (requestFilterDto.getIsOngoing()) {
            q.where(party.status.eq(PartyStatus.ON));
        }
        if (requestFilterDto.getPlaces() != null) {
            q.where(placeEquals(requestFilterDto.getPlaces()));
        }
        q.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Party> res = q.fetch();
        Long count = query.selectFrom(party)
                .stream().count();
        return new PageImpl<>(res, pageable, count);
    }

    private BooleanBuilder placeEquals(List<Place> places) {
        QParty party = QParty.party;
        BooleanBuilder builder = new BooleanBuilder();
        for (Place p : places) {
            builder.or(party.place.eq(p));
        }
        return builder;
    }

    @Override
    public Page<Party> findMyParties(Pageable pageable, Long memberId) {
        QParty party = QParty.party;
        JPQLQuery<Party> q = query.selectFrom(party)
                .where(party.owner.id.eq(memberId));
        q.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Party> res = q.fetch();
        Long count = query.selectFrom(party)
                .stream().count();
        return new PageImpl<>(res, pageable, count);
    }
}
