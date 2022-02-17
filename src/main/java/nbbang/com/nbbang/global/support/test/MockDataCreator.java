package nbbang.com.nbbang.global.support.test;

import nbbang.com.nbbang.domain.bbangpan.domain.MemberParty;
import nbbang.com.nbbang.domain.bbangpan.repository.MemberPartyRepository;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Transactional
@Profile("!test")
public class MockDataCreator implements CommandLineRunner {

    @PersistenceContext EntityManager em;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyRepository partyRepository;
    @Autowired HashtagRepository hashtagRepository;
    @Autowired MemberPartyRepository memberPartyRepository;
    @Autowired ChatService chatService;

    private Long luffyId;
    private Long korungId;
    private Long mock1Id;


    private Long party1Id;
    private Long party2Id;
    private Long party3Id;

    @Override
    public void run(String... args) throws Exception {
        createMembers();
        createParty1();
        createParty2();
        createParty3();
        for (int i = 0; i < 10; i++) {
            createMessage1();
        }
    }

    @Transactional
    public void createMembers() {
        Member luffy = Member.builder()
                .nickname("루피")
                .avatar("https://w.namu.la/s/bbff81cb4fd4d3f97d245a28a360b5cb665745b2cb434287f9cbd3423978919ce5377ef034f150277564c1798660ae95825fe2bfda50baa970f97d999a81c31401c0eb130e3bae0f9e1a2d3aea2a10769e564b0fbce08a8f23360382fd6e5425")
                .place(Place.SINCHON)
                .build();
        memberRepository.save(luffy);
        luffyId = luffy.getId();

        Member korung = Member.builder()
                .nickname("코렁")
                .avatar("https://w.namu.la/s/bc75175b063a43a123523fba625ed9d185f6a180a3c1e9ae9409db0c110266e4f69e7aa434253687819b7dd5b36cccf4bd508977f6f76e3b9353db545d75168bfde624ca06dbf51818304d0bddc8c11cd1bdea60c7fb56113091172d05dd2dd5")
                .place(Place.YEONHUI)
                .build();
        memberRepository.save(korung);
        korungId = korung.getId();

        Member mock1 = Member.builder()
                .nickname("철수")
                .place(Place.CHANGCHEON)
                .build();
        memberRepository.save(mock1);
        mock1Id = mock1.getId();

    }

    @Transactional
    public void createParty1() {
        Party party = Party.builder()
                .title("BHC 뿌링클 오늘 7시")
                .content("오늘 연대 서문에서 치킨 같이 시켜 먹을 파티 구해요 또는 각자 시키고 배달비만 n빵 하실분?? 2~3명 모아봅니다 ㅎㅎ 뿌링클 교촌 다 좋아요 ㅎㅎ")
                .createTime(LocalDateTime.of(2022, 02, 12, 12, 40))
                .cancelTime(LocalDateTime.of(2022, 02, 13, 12, 40))
                .goalNumber(4)
                .owner(memberRepository.findById(luffyId).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.SINCHON)
                .deliveryFee(300)
                .status(PartyStatus.ON)
                .isBlocked(false)
                .build();
        partyRepository.save(party);
        party1Id = party.getId();
        //hashtagRepository.save(Hashtag.builder().content("콤보").party(party).build());
        memberPartyRepository.save(MemberParty.builder()
                .member(memberRepository.findById(korungId).get())
                .party(party)
                .price(1000)
                .build());
    }

    @Transactional
    public void createParty2() {
        Party party = Party.builder()
                .title("BHC 맛초킹 내일 8시")
                .content("뿌링클 넘나 먹구 싶다잉")
                .createTime(LocalDateTime.of(2022, 02, 14, 12, 40))
                .cancelTime(LocalDateTime.of(2022, 02, 15, 12, 40))
                .goalNumber(4)
                .owner(memberRepository.findById(luffyId).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.YEONHUI)
                .deliveryFee(300)
                .status(PartyStatus.SOON)
                .isBlocked(false)
                .build();
        partyRepository.save(party);
        party2Id = party.getId();
        //hashtagRepository.save(Hashtag.builder().content("콤보").party(party).build());
        memberPartyRepository.save(MemberParty.builder()
                .member(memberRepository.findById(korungId).get())
                .party(party)
                .price(1000)
                .build());
    }

    @Transactional
    public void createParty3() {
        Party party = Party.builder()
                .title("롯데리아 오늘 8시")
                .content("뿌링클 넘나 먹구 싶다잉")
                .createTime(LocalDateTime.of(2022, 02, 10, 15, 40))
                .cancelTime(LocalDateTime.of(2022, 02, 11, 15, 40))
                .goalNumber(4)
                .owner(memberRepository.findById(luffyId).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.CHANGCHEON)
                .deliveryFee(300)
                .status(PartyStatus.CANCEL)
                .isBlocked(false)
                .build();
        partyRepository.save(party);
        party3Id = party.getId();
        //hashtagRepository.save(Hashtag.builder().content("콤보").party(party).build());
        memberPartyRepository.save(MemberParty.builder()
                .member(memberRepository.findById(korungId).get())
                .party(party)
                .price(1000)
                .build());
    }

    @Transactional
    public void createMessage1() {
        chatService.sendMessage(luffyId, party1Id, "뿌링클 넘나 먹고 싶다.", LocalDateTime.of(2022, 02, 10, 16, 40));
        chatService.sendMessage(korungId, party1Id, "내일 점심에 버거킹 드실분", LocalDateTime.of(2022, 02, 10, 16, 45));
    }

}