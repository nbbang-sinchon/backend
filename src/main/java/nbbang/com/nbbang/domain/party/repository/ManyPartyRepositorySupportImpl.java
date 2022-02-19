package nbbang.com.nbbang.domain.party.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.QMemberParty;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.*;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.PartyListRequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ManyPartyRepositorySupportImpl implements ManyPartyRepositorySupport {

    private final JPAQueryFactory query;


    /**
     *
     * @param pageable
     * @param filter
     * @param cursorId
     * @param memberId
     * @param partyId
     * @return
     */
    @Override
    public Page<Party> findAllParties(Pageable pageable, PartyListRequestFilterDto filter, Long cursorId, Long memberId, List<String> hashtags, Long... partyId) {
        QParty party = QParty.party;
        QPartyHashtag hashtag = QPartyHashtag.partyHashtag;
        JPQLQuery<Party> q = query.selectFrom(party);

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

        // 커서 페이징을 제공합니다
        if (cursorId != null) {
            q.where(party.id.loe(cursorId));
        }

        // 자신이 속한 파티 필터링을 제공합니다
        if (memberId != null) {
            q.where(isMemberOfParty(memberId));
        }

        // 표시하지 않을 파티를 제공합니다
        for (Long idBlock : partyId) {
            if (idBlock != null) {
                q.where(party.id.ne(idBlock));
            }
        }

        // 해시태그를 포함한 파티만 조회합니다
        if (hashtags != null) {
            for (String content : hashtags) {
                q.where(party.partyHashtags.any().hashtag.content.eq(content));
            }
        }

        // 소팅은 id 기준으로 제공합니다 (비지니스 로직이 그럼)
        q.orderBy(party.id.desc());

        // 페이징을 제공합니다
        if (pageable != null) {
            q.offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }

        List<Party> res = q.fetch();

        Long count = query.selectFrom(party)
                .stream().count();

        return new PageImpl<>(res, pageable, count);
    }


    @Override
    public Page<Party> findAllByRequestDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto) {
        System.out.println("===========-=-=-=-=-=-=ㅇㅅㅇ=-=-=-=====================");
        QParty party = QParty.party;
        JPQLQuery<Party> q = query.selectFrom(party)
                .where(party.title.contains(requestFilterDto.getSearch()));
        if (requestFilterDto.getIsOngoing()) {
            q.where(isPartyOngoing());
        }
        if (requestFilterDto.getPlaces() != null) {
            q.where(placeEquals(requestFilterDto.getPlaces()));
        }
        q.orderBy(party.id.desc());
        q.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Party> res = q.fetch();
        Long count = query.selectFrom(party)
                .stream().count();
        return new PageImpl<>(res, pageable, count);
    }

    private BooleanBuilder isPartyOngoing() {
        QParty party = QParty.party;
        return new BooleanBuilder()
                .or(party.status.eq(PartyStatus.OPEN));
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

    private BooleanBuilder isMemberOfParty(Long memberId) {
        QParty party = QParty.party;
        QMemberParty mp = QMemberParty.memberParty;
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(party.owner.id.eq(memberId));
        builder.or(party.memberParties.any().member.id.eq(memberId));
        return builder;
    }

    @Override
    public Page<Party> findMyParties(Pageable pageable, PartyListRequestFilterDto filter, Long memberId) {
        QParty party = QParty.party;
        JPQLQuery<Party> q = query.selectFrom(party)
                .where(isMemberOfParty(memberId));
        q.orderBy(party.id.desc());

        q.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Party> res = q.fetch();
        Long count = query.selectFrom(party)
                .stream().count();
        return new PageImpl<>(res, pageable, count);
    }

    @Override
    public Page<Party> findAllByCursoredFilterDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto, Long cursorId) {
        QParty party = QParty.party;
        JPQLQuery<Party> q = query.selectFrom(party)
                .where(party.id.loe(cursorId))
                .where(party.title.contains(requestFilterDto.getSearch()));
        if (requestFilterDto.getIsOngoing()) {
            q.where(isPartyOngoing());
        }
        if (requestFilterDto.getPlaces() != null) {
            q.where(placeEquals(requestFilterDto.getPlaces()));
        }
        q.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(party.id.desc());
        List<Party> res = q.fetch();
        Long count = query.selectFrom(party).stream().count();
        return new PageImpl<>(res, pageable, count);
    }
}
