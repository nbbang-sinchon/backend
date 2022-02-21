package nbbang.com.nbbang.global.support.test;

import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.repository.PartyMemberRepository;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyHashtag;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyHashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.HashtagService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.security.Role;
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
    @Autowired
    PartyMemberRepository memberPartyRepository;
    @Autowired ChatService chatService;
    @Autowired HashtagService hashtagService;
    @Autowired PartyService partyService;
    @Autowired PartyHashtagRepository partyHashtagRepository;

    private Long luffyId;
    private Long korungId;
    private Long mock1Id;
    private Long mock2Id;
    private Long mock3Id;
    private Long mock4Id;


    private Long party1Id;
    private Long party2Id;
    private Long party3Id;
    private Long party4Id;
    private Long party5Id;
    private Long party6Id;
    private Long party7Id;




    @Override
    public void run(String... args) throws Exception {
        createMembers();
        createParty1();
        createParty2();
        createParty3();
        createParty4();
        createParty5();
        createParty6();
        createParty7();


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
                .email("test1eqjklasdj@gamil.com")
                .role(Role.BLOCKED)
                .build();
        memberRepository.save(luffy);
        luffyId = luffy.getId();

        Member korung = Member.builder()
                .nickname("코렁")
                .avatar("https://w.namu.la/s/bc75175b063a43a123523fba625ed9d185f6a180a3c1e9ae9409db0c110266e4f69e7aa434253687819b7dd5b36cccf4bd508977f6f76e3b9353db545d75168bfde624ca06dbf51818304d0bddc8c11cd1bdea60c7fb56113091172d05dd2dd5")
                .place(Place.YEONHUI)
                .email("test2fjasdiofj@gamil.com")
                .role(Role.USER)
                .build();
        memberRepository.save(korung);
        korungId = korung.getId();

        Member mock1 = Member.builder()
                .nickname("철수")
                .place(Place.CHANGCHEON)
                .email("test3djsiaoljdlask@gamil.com")
                .role(Role.USER)
                .build();
        memberRepository.save(mock1);
        mock1Id = mock1.getId();

        Member mock2 = Member.builder()
                .nickname("고영희")
                .place(Place.SINCHON)
                .email("test4qejioasjd@gamil.com")
                .role(Role.USER)
                .build();
        memberRepository.save(mock2);
        mock2Id = mock2.getId();

        Member mock3 = Member.builder()
                .nickname("철수2")
                .place(Place.YEONHUI)
                .email("test5pqwoieopasdj@gamil.com")
                .role(Role.USER)
                .build();
        memberRepository.save(mock3);
        mock3Id = mock3.getId();

        Member mock4 = Member.builder()
                .nickname("잼민")
                .place(Place.CHANGCHEON)
                .email("test6dsaildjuoisa@gamil.com")
                .role(Role.USER)
                .build();
        memberRepository.save(mock4);
        mock4Id = mock4.getId();

    }

    // 해시태그 추가 임시 메소드
    public void addHashtags(Party party, String ... contents) {
        Arrays.asList(contents).stream().forEach(hc -> {
            Hashtag h = Hashtag.createHashtag(hc);
            hashtagRepository.save(h);
            PartyHashtag ph = PartyHashtag.createPartyHashtag(party, h);
            party.addPartyHashtag(ph);
            partyHashtagRepository.save(ph);
        });
    }

    public void addMember(Party party, Long memberId) {
        memberPartyRepository.save(PartyMember.builder()
                .member(memberRepository.findById(memberId).get())
                .party(party)
                .price(1000)
                .build());
    }

    @Transactional
    public void createParty1() {
        Party party = Party.builder()
                .title("BHC 뿌링클 오늘 7시")
                .content("오늘 연대 서문에서 치킨 같이 시켜 먹을 파티 구해요 또는 각자 시키고 배달비만 n빵 하실분?? 2~3명 모아봅니다 ㅎㅎ 뿌링클 교촌 다 좋아요 ㅎㅎ")
                .createTime(LocalDateTime.of(2022, 02, 12, 12, 40))
                .goalNumber(4)
                .owner(memberRepository.findById(luffyId).get())
                .place(Place.SINCHON)
                .deliveryFee(300)
                .status(PartyStatus.OPEN)
                .build();
        partyRepository.save(party);
        party1Id = party.getId();
        addHashtags(party, "치킨", "배달비", "BHC", "뿌링클");
        memberPartyRepository.save(PartyMember.builder()
                .member(memberRepository.findById(korungId).get())
                .party(party)
                .price(1000)
                .build());
    }

    @Transactional
    public void createParty2() {
        Party party = Party.builder()
                .title("BHC 맛초킹 내일 8시")
                .content("뿌링클 넘나 먹구 싶다잉 근데 돈없")
                .createTime(LocalDateTime.of(2022, 02, 14, 12, 40))
                .goalNumber(4)
                .owner(memberRepository.findById(korungId).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.YEONHUI)
                .deliveryFee(300)
                .status(PartyStatus.FULL)
                .build();
        partyRepository.save(party);
        party2Id = party.getId();
        //hashtagRepository.save(Hashtag.builder().content("콤보").party(party).build());
        addHashtags(party, "치킨", "배달비", "BHC", "뿌링클", "맛초킹");
        memberPartyRepository.save(PartyMember.builder()
                .member(memberRepository.findById(mock1Id).get())
                .party(party)
                .price(1000)
                .build());
    }

    @Transactional
    public void createParty3() {
        Party party = Party.builder()
                .title("롯데리아 오늘 8시")
                .content("롯데리아 넘나 먹구 싶다잉")
                .createTime(LocalDateTime.of(2022, 02, 10, 15, 40))
                .goalNumber(4)
                .owner(memberRepository.findById(mock1Id).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.CHANGCHEON)
                .deliveryFee(300)
                .status(PartyStatus.CLOSED)
                .build();
        partyRepository.save(party);
        party3Id = party.getId();
        //hashtagRepository.save(Hashtag.builder().content("콤보").party(party).build());
        addHashtags(party, "롯데리아", "배달비", "롯데", "햄버거");
        memberPartyRepository.save(PartyMember.builder()
                .member(memberRepository.findById(korungId).get())
                .party(party)
                .price(1000)
                .build());
    }

    @Transactional
    public void createParty4() {
        Party party = Party.builder()
                .title("내일 점심에 버거킹 드실분")
                .content("내일 점심에 버거킹 드실분 구함")
                .createTime(LocalDateTime.of(2022, 02, 10, 15, 40))
                .goalNumber(3)
                .owner(memberRepository.findById(mock1Id).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.YEONHUI)
                .deliveryFee(300)
                .status(PartyStatus.OPEN)
                .build();

        partyRepository.save(party);
        party3Id = party.getId();
        addHashtags(party, "버거킹", "햄버거");
        //hashtagRepository.save(Hashtag.builder().content("콤보").party(party).build());
        memberPartyRepository.save(PartyMember.builder()
                .member(memberRepository.findById(korungId).get())
                .party(party)
                .price(1000)
                .build());
        addMember(party, luffyId);
    }

    @Transactional
    public void createParty5() {
        Party party = Party.builder()
                .title("푸라닭 콘소메이징 맛있어요ㅛㅛㅛㅛㅛㅛ")
                .content("푸라닭 콘소메이징 맛있어요ㅛㅛㅛㅛㅛㅛ")
                .createTime(LocalDateTime.of(2022, 02, 10, 15, 40))
                .goalNumber(8)
                .owner(memberRepository.findById(luffyId).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.CHANGCHEON)
                .deliveryFee(300)
                .status(PartyStatus.FULL)
                .build();

        partyRepository.save(party);
        party5Id = party.getId();
        addHashtags(party, "치킨", "푸라닭");
        addMember(party, mock1Id);
        addMember(party, mock2Id);
        addMember(party, mock3Id);
        addMember(party, mock4Id);
    }

    @Transactional
    public void createParty6() {
        Party party = Party.builder()
                .title("초밥 시켜먹어요")
                .content("초밥 맛있어요ㅛㅛㅛㅛㅛㅛ")
                .createTime(LocalDateTime.of(2022, 02, 10, 15, 40))
                .goalNumber(4)
                .owner(memberRepository.findById(korungId).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.CHANGCHEON)
                .deliveryFee(300)
                .status(PartyStatus.CLOSED)
                .build();

        partyRepository.save(party);
        party5Id = party.getId();
        //hashtagRepository.save(Hashtag.builder().content("콤보").party(party).build());
        addHashtags(party, "초밥", "스시");
        addMember(party, luffyId);
        addMember(party, mock1Id);
        addMember(party, mock2Id);
    }

    @Transactional
    public void createParty7() {
        Party party = Party.builder()
                .title("떡복이 ㄱ?")
                .content("떡복이 드실분")
                .createTime(LocalDateTime.of(2022, 02, 10, 15, 40))
                .goalNumber(4)
                .owner(memberRepository.findById(luffyId).get())
                //.hashtags(Arrays.asList(Hashtag.builder().content("콤보").build(), Hashtag.builder().content("배달비").build(),Hashtag.builder().content("야식").build(),Hashtag.builder().content("사이드 가능").build()))
                .place(Place.CHANGCHEON)
                .deliveryFee(300)
                .status(PartyStatus.OPEN)
                .build();

        partyRepository.save(party);
        party5Id = party.getId();
        //hashtagRepository.save(Hashtag.builder().content("콤보").party(party).build());
        addHashtags(party, "떡복이", "엽떡", "국대");
        addMember(party, luffyId);
        addMember(party, mock1Id);
        addMember(party, mock2Id);
    }

    @Transactional
    public void createMessage1() {
        chatService.sendMessage(luffyId, party1Id, "뿌링클 넘나 먹고 싶다.", LocalDateTime.of(2022, 02, 10, 16, 40));
        chatService.sendMessage(korungId, party1Id, "내일 점심에 버거킹 드실분", LocalDateTime.of(2022, 02, 10, 16, 45));
    }

}