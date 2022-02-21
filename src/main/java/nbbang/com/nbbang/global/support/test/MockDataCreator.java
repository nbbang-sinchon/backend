package nbbang.com.nbbang.global.support.test;

import lombok.Builder;
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
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Arrays;

import static nbbang.com.nbbang.domain.member.dto.Place.*;
import static nbbang.com.nbbang.domain.party.domain.PartyStatus.*;

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


    public void createManyParties() {
        createPartyMethod("먹고 싶은 빵 가등 골라 담아요", "뚜레쥬르 5천원 쿠폰 있어요", 4, luffyId, SINCHON, OPEN, mock1Id, mock2Id, null, "빵", "뚜레쥬르", "할인");
        createPartyMethod("설빙 드실분", "설빙 먹어요", 4, mock1Id, CHANGCHEON, FULL, mock3Id, mock2Id, null, "설빙", "빙수");
        createPartyMethod("아메리카나 드실분", "이게 무슨 음식인진 모르겠어요", 8, luffyId, YEONHUI, CLOSED, mock1Id,null, null, "멕시카나", "양념");
        createPartyMethod("DDQ 치킨 같이 드실 분", "디디큐 황금올리브 같이 먹어요", 4, mock1Id, CHANGCHEON, CLOSED, luffyId, null, null, "BBQ", "황금올리브", "배달");
        createPartyMethod("핵도날드 같이 드실 분", "해도날드 같이 드실 분 있으세요?", 4, luffyId, SINCHON, CLOSED, mock1Id, mock2Id, null, "멕도날드");createPartyMethod("족발 먹으실 분", "오늘 먹었는데 맛있어요", 4, luffyId, SINCHON, OPEN, mock1Id, mock4Id, null, "족발", "배달");
        createPartyMethod("돼지국밥 시켜 드실 분~", "가계사정상 배민원과 쿠팡주문만 배달가능하다고 합니다.", 4, korungId, CHANGCHEON, CLOSED, mock1Id, mock2Id, null, "돼지국밥", "배달", "배민원");
        createPartyMethod("냉면", "냉면&덮밥이라는 가게에서 배달해요", 4, mock4Id, NONE, OPEN, luffyId, mock2Id, null, "냉면", "단골집", "배달", "리뷰 이벤트", "오픈");
        createPartyMethod("페리카나 후라이드 치킨 드실분", "페리카나 후라이드치킨 17000원인데 같이 드실분", 2, luffyId, YEONHUI, FULL, mock1Id,null, null, "페리카나", "후라이드");
        createPartyMethod("BBQ 황금올리브 반반 같이 드실 분", "비비큐 황금올리브 반반 21500원 배달해 먹어요", 4, mock1Id, SINCHON, OPEN, luffyId, null, null, "BBQ", "황금올리브", "배달");
        createPartyMethod("자메이카 통다리 구이 BBQ 배달하실 분", "맛있는 치킨의 엉치살이 붙은 통다리만을 골라, 신비의 나라 자메이카의 300년 전톨 저크소스를 바른 매콤달콤한 맛을 느낄 수 있는 신비의 치킨!", 3, luffyId, CHANGCHEON, OPEN, mock1Id, mock2Id, null, "BBQ", "bbq", "자메이카통다리구이");
        createPartyMethod("버거킹 같이 드실 분", "딜리버리 서비스 메뉴의 가격은 조금 다를 수 있어요", 4, luffyId, SINCHON, CLOSED, mock1Id, mock2Id, null, "버거킹", "와퍼");
        createPartyMethod("짜장면", "짜장면이라는 가게에서 배달해요", 4, mock4Id, NONE, OPEN, mock3Id, mock2Id, null, "짜장면", "배달", "리뷰 이벤트", "오픈");
        createPartyMethod("멕시카나 양념 치킨 드실분", "양념치킨 17500원인데 같이 드실분", 2, luffyId, YEONHUI, FULL, mock1Id,null, null, "멕시카나", "양념");
        createPartyMethod("BBQ 황금올리브 같이 드실 분", "비비큐 황금올리브 같이 먹어요", 4, mock1Id, CHANGCHEON, OPEN, luffyId, null, null, "BBQ", "황금올리브", "배달");
        createPartyMethod("멕도날드 같이 드실 분", "멕도날드 같이 드실 분 있으세요?", 4, luffyId, SINCHON, CLOSED, mock1Id, mock2Id, null, "멕도날드");


    }
    //public void createPartyMethod(String title, String content, Integer goalNumber, Long ownerId, Place place, PartyStatus partyStatus, Long member1, Long member2, Long member3, String ... hashtags)
    @Override
    public void run(String... args) throws Exception {
        createMembers();
        createParty1();
        createParty2();
        createManyParties();
        createParty3();
        createParty4();
        createParty5();
        createParty6();
        createParty7();
        createManyParties();


        for (int i = 0; i < 10; i++) {
            createMessage1();
        }
    }

    @Transactional
    public void createMembers() {
        Member luffy = Member.builder()
                .nickname("루피")
                .avatar("https://w.namu.la/s/bbff81cb4fd4d3f97d245a28a360b5cb665745b2cb434287f9cbd3423978919ce5377ef034f150277564c1798660ae95825fe2bfda50baa970f97d999a81c31401c0eb130e3bae0f9e1a2d3aea2a10769e564b0fbce08a8f23360382fd6e5425")
                .place(SINCHON)
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
                .place(SINCHON)
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
                .place(SINCHON)
                .deliveryFee(300)
                .status(OPEN)
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
                .status(FULL)
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
                .status(CLOSED)
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
        addMember(party, luffyId);
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
                .status(OPEN)
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
                .status(FULL)
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
                .status(CLOSED)
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
                .status(OPEN)
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
    public void createPartyMethod(String title, String content, Integer goalNumber, Long ownerId, Place place, PartyStatus partyStatus, Long member1, Long member2, Long member3, String ... hashtags) {
        Party party = Party.builder()
                .title(title)
                .content(content)
                .createTime(LocalDateTime.now())
                .goalNumber(goalNumber)
                .owner(memberRepository.findById(ownerId).get())
                .place(place)
                .status(partyStatus)
                .build();
        partyRepository.save(party);
        addHashtags(party, hashtags);
        if (member1 != null) {
            addMember(party, member1);
        }
        if (member2 != null) {
            addMember(party, member2);
        }
        if (member3 != null) {
            addMember(party, member3);
        }

    }

    @Transactional
    public void createMessage1() {
        chatService.sendMessage(luffyId, party1Id, "뿌링클 넘나 먹고 싶다.", LocalDateTime.of(2022, 02, 10, 16, 40));
        chatService.sendMessage(korungId, party1Id, "내일 점심에 버거킹 드실분", LocalDateTime.of(2022, 02, 10, 16, 45));
    }

}