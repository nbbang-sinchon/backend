package nbbang.com.nbbang.domain.party.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.QMember;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.*;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ManyPartyRepositorySupportImpl implements ManyPartyRepositorySupport {

    private final JPAQueryFactory query;

    @Override
    public Page<Party> findAllParties(Pageable pageable, PartyListRequestFilterDto filter, Long cursorId, Long memberId) {
        QParty party = QParty.party;
        QMember member = QMember.member;

        JPQLQuery<Party> q = query.selectFrom(party)
                .leftJoin(party.owner, member)
                .fetchJoin();

        // 제목 검색을 제공합니다
        String search = filter.getSearch();
        if (search != null) {
            q.where(party.title.contains(search));
        }

        // 위치 필터를 제공합니다
        List<Place> places = filter.getPlaces();
        if (places != null) {
            q.where(placeEquals(places));
        }

        // 파티 상태 필터를 제공합니다
        List<PartyStatus> statuses = filter.getStatuses();
        if (statuses != null) {
            q.where(statusEquals(statuses));
        }

        // 위시리스트 필터를 제공합니다
        Boolean isWishlist = filter.getIsWishlist();
        if (isWishlist != null) {
            q.where(party.wishlists.any().member.id.eq(memberId));
        }

        // 커서 페이징을 제공합니다
        if (cursorId != null) {
            q.where(party.id.lt(cursorId));
        }

        // 소팅은 id 기준으로 제공합니다
        q.orderBy(party.id.desc());

        // 페이징을 제공합니다
        if (pageable != null) {
            q.offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }

        List<Party> res = q.fetch();
        //Long count = query.selectFrom(party)
        //        .stream().count();
        return new PageImpl<>(res, pageable, 0L);
    }

    private BooleanBuilder placeEquals(List<Place> places) {
        QParty party = QParty.party;
        BooleanBuilder builder = new BooleanBuilder();
        for (Place p : places) {
            builder.or(party.place.eq(p));
        }
        return builder;
    }

    private BooleanBuilder statusEquals(List<PartyStatus> statuses) {
        QParty party = QParty.party;
        BooleanBuilder builder = new BooleanBuilder();
        for (PartyStatus s : statuses) {
            builder.or(party.status.eq(s));
        }
        return builder;
    }

}
