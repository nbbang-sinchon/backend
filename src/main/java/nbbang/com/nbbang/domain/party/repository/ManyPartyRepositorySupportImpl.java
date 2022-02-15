package nbbang.com.nbbang.domain.party.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
        System.out.println(requestFilterDto.getShowOngoing());
        if (requestFilterDto.getShowOngoing()) {
            q.where(party.status.eq(PartyStatus.ON));
        }
        q.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Party> res = q.fetch();
        Long count = query.selectFrom(party)
                .stream().count();
        return new PageImpl<>(res, pageable, count);
    }
}
