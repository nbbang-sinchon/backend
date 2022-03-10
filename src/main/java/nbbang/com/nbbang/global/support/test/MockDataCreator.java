package nbbang.com.nbbang.global.support.test;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.bbangpan.repository.PartyMemberRepository;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyHashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.HashtagService;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
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
import java.util.List;
import java.util.UUID;

import static nbbang.com.nbbang.domain.member.dto.Place.*;
import static nbbang.com.nbbang.domain.party.domain.PartyStatus.*;

@Component
@Transactional
@Profile("!test")
@Slf4j
public class MockDataCreator implements CommandLineRunner {

    @PersistenceContext EntityManager em;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyRepository partyRepository;
    @Autowired HashtagRepository hashtagRepository;
    @Autowired PartyMemberRepository memberPartyRepository;
    @Autowired ChatService chatService;
    @Autowired HashtagService hashtagService;
    @Autowired PartyService partyService;
    @Autowired PartyHashtagRepository partyHashtagRepository;
    @Autowired PartyMemberService partyMemberService;
    @Autowired MemberService memberService;

    private Long luffyId;
    private Long korungId;
    private Long mock1Id;
    private Long mock2Id;
    private Long mock3Id;
    private Long mock4Id;

    private LocalDateTime initTime = LocalDateTime.of(2022, 02, 10, 16, 30);

    private LocalDateTime nextTime() {
        initTime = initTime.plusMinutes(1);
        return initTime;
    }

    @Override
    public void run(String... args) throws Exception {
        //createMockMembers();
        //createMockDetailedParty1();
        //createMockParties();
    }

    private void createMockMembers() {
        luffyId = createMockMember("루피", SINCHON);
        korungId = createMockMember("코렁", SINCHON);
        mock1Id = createMockMember("철수", YEONHUI);
        mock2Id = createMockMember("고영희", CHANGCHEON);
        mock3Id = createMockMember("두번째 철수", SINCHON);
        mock4Id = createMockMember("잼민이", YEONHUI);
    }

    private void createMockDetailedParty1() {
        Party party = createMockParty("BHC 뿌링클 오늘 7시", "오늘 연대 서문에서 치킨 같이 시켜 먹을 파티 구해요 또는 각자 시키고 배달비만 n빵 하실분?? 2~3명 모아봅니다 ㅎㅎ 뿌링클 교촌 다 좋아요 ㅎㅎ", 4, luffyId, SINCHON, OPEN, null, null, null, "치킨", "배달비", "BHC", "뿌링클");
        Long pid = party.getId();
        addMember(party, korungId);
        createMockMessage(luffyId, pid, "안녕하세요");
        createMockMessage(luffyId, pid, "hello");
        createMockMessage(luffyId, pid, "뿌링클 넘나 먹고 싶다.");
        addMember(party, mock1Id);
        createMockMessage(luffyId, pid, "안녕하세요");
        createMockMessage(luffyId, pid, "뿌링클 너무너무 먹고 싶다.");
        exitMember(party, mock1Id);
        createMockMessage(luffyId, pid, "왜 나가지");
        createMockMessage(korungId, pid, "내일 점심에 버거킹 드실분");
        addMember(party, mock1Id);
        createMockMessage(luffyId, pid, "안녕하세요");

        for (int i = 0; i < 10; i++) {
            createMockMessage(luffyId, pid, "테스트 메시지 입니다." + i * 3);
            createMockMessage(mock1Id, pid, "테스트 메시지 입니다." + i * 3 + 1);
            createMockMessage(korungId, pid, "테스트 메시지 입니다." + i * 3 + 2);
        }
        addMember(party, mock2Id);
        for (int i = 0; i < 10; i++) {
            String content = "뿌링클 너무";
            for (int j = 0; j < i; j++) {
                content += "너무";
            }
            createMockMessage(luffyId, pid, content + " 먹고 싶다.");
        }
        exitMember(party, mock2Id);
    }

    private void createMockMessage(Long memberId, Long partyId, String content) {
        chatService.sendMessage(memberId, partyId, content, nextTime());
    }

    private void createMockParties() {
        log.info("**********create mock parties*************");
        createMockParty("BHC 맛초킹 내일 8시 드실분", "뿌링클 넘나 먹구 싶다잉 근데 돈없", 4, korungId, YEONHUI, CLOSED, mock3Id, mock2Id, null, "치킨", "배달비", "BHC", "bhc","뿌링클", "맛초킹");
        createMockParty("롯데리아 오늘 8시", "롯데리아 넘나 먹구 싶다잉", 4, mock4Id, SINCHON, OPEN, mock1Id, mock2Id, null, "롯데리아", "배달비", "롯데", "햄버거");
        createMockParty("내일 점심에 버거킹 드실분", "내일 점심에 버거킹 드실분 구함", 3, mock1Id, CHANGCHEON, CLOSED, luffyId, mock2Id, null, "버거킹", "햄버거");
        createMockParty("푸라닭 콘소메이징 맛있어요ㅛㅛㅛㅛㅛㅛ", "푸라닭 콘소메이징 맛있어요ㅛㅛㅛㅛㅛㅛ", 8, luffyId, CHANGCHEON, FULL, mock1Id, mock2Id, mock4Id, "치킨", "푸라닭");
        createMockParty("초밥 시켜먹어요", "초밥 맛있어요ㅛㅛㅛㅛㅛㅛ", 4, korungId, NONE, OPEN, mock1Id, mock2Id, null, "초밥", "스시");
        createMockParty("떡복이 ㄱ?", "떡복이 드실분", 4, luffyId, CHANGCHEON, OPEN, mock1Id, mock2Id, null, "떡복이", "엽떡", "국대");
        createMockParty("먹고 싶은 빵 가득 골라 담아요", "뚜레쥬르 5천원 쿠폰 있어요", 4, luffyId, SINCHON, OPEN, mock1Id, mock2Id, null, "빵", "뚜레쥬르", "할인");
        createMockParty("설빙 드실분", "설빙 먹어요", 4, mock1Id, CHANGCHEON, FULL, mock3Id, mock2Id, null, "설빙", "빙수");
        createMockParty("아메리카나 드실분", "이게 무슨 음식인진 모르겠어요", 8, luffyId, YEONHUI, CLOSED, mock1Id,null, null, "멕시카나", "양념");
        createMockParty("DDQ 치킨 같이 드실 분", "디디큐 황금올리브 같이 먹어요", 4, mock1Id, CHANGCHEON, CLOSED, luffyId, null, null, "BBQ", "황금올리브", "배달");
        createMockParty("핵도날드 같이 드실 분", "해도날드 같이 드실 분 있으세요?", 4, luffyId, SINCHON, CLOSED, mock1Id, mock2Id, null, "멕도날드");
        createMockParty("족발 먹으실 분", "오늘 먹었는데 맛있어요", 4, luffyId, SINCHON, OPEN, mock1Id, mock4Id, null, "족발", "배달");
        createMockParty("돼지국밥 시켜 드실 분~", "가계사정상 배민원과 쿠팡주문만 배달가능하다고 합니다.", 4, korungId, CHANGCHEON, CLOSED, mock1Id, mock2Id, null, "돼지국밥", "배달", "배민원");
        createMockParty("냉면", "냉면&덮밥이라는 가게에서 배달해요", 4, mock4Id, NONE, OPEN, luffyId, mock2Id, null, "냉면", "단골집", "배달", "리뷰 이벤트", "오픈");
        createMockParty("페리카나 후라이드 치킨 드실분", "페리카나 후라이드치킨 17000원인데 같이 드실분", 2, luffyId, YEONHUI, FULL, mock1Id,null, null, "페리카나", "후라이드");
        createMockParty("BBQ 황금올리브 반반 같이 드실 분", "비비큐 황금올리브 반반 21500원 배달해 먹어요", 4, mock1Id, SINCHON, OPEN, luffyId, null, null, "BBQ", "황금올리브", "배달");
        createMockParty("자메이카 통다리 구이 BBQ 배달하실 분", "맛있는 치킨의 엉치살이 붙은 통다리만을 골라, 신비의 나라 자메이카의 300년 전톨 저크소스를 바른 매콤달콤한 맛을 느낄 수 있는 신비의 치킨!", 3, luffyId, CHANGCHEON, OPEN, mock1Id, mock2Id, null, "BBQ", "bbq", "자메이카통다리구이");
        createMockParty("버거킹 같이 드실 분", "딜리버리 서비스 메뉴의 가격은 조금 다를 수 있어요", 4, luffyId, SINCHON, CLOSED, mock1Id, mock2Id, null, "버거킹", "와퍼");
        createMockParty("짜장면", "짜장면이라는 가게에서 배달해요", 4, mock4Id, NONE, OPEN, mock3Id, mock2Id, null, "짜장면", "배달", "리뷰 이벤트", "오픈");
        createMockParty("멕시카나 양념 치킨 드실분", "양념치킨 17500원인데 같이 드실분", 2, luffyId, YEONHUI, FULL, mock1Id,null, null, "멕시카나", "양념");
        createMockParty("BBQ 황금올리브 같이 드실 분", "비비큐 황금올리브 같이 먹어요", 4, mock1Id, CHANGCHEON, OPEN, luffyId, null, null, "BBQ", "황금올리브", "배달");
        createMockParty("멕도날드 같이 드실 분", "멕도날드 같이 드실 분 있으세요?", 4, luffyId, SINCHON, CLOSED, mock1Id, mock2Id, null, "멕도날드");
        createMockParty("마라탕탕탕", "마라탕탕탕", 4, luffyId, SINCHON, OPEN, null, null, null, "마라","탕","좋아");
        createMockParty("스타벅스 배달", "스타벅스 배달", 4, luffyId, YEONHUI, OPEN, null, null, null, "커피","아메리카노","좋아");
        createMockParty("족발 보쌈 파뤼투나잇", "족발 보쌈 파뤼투나잇", 6, luffyId, YEONHUI, OPEN, null, null, null, "족발", "보쌈");
        createMockParty("빵먹고싶다", "빵먹고싶다", 2, luffyId, SINCHON, OPEN, null, null, null, "단팥빵", "호빵", "붕어빵");
        createMockParty("빵먹고싶다", "빵먹고싶다", 3, luffyId, CHANGCHEON, OPEN, null, null, null, "단팥빵", "호빵", "붕어빵");
    }

    private String randomEmail() {
        return UUID.randomUUID().toString() + "@gmail.com";
    }

    private Long createMockMember(String nickname, Place place) {
        return memberService.saveMember(nickname, place, randomEmail(), Role.USER);
    }

    private void addMember(Party party, Long memberId) {
        Member member = memberService.findById(memberId);
        partyMemberService.joinParty(party, member);
    }

    private void exitMember(Party party, Long memberId) {
        Member member = memberService.findById(memberId);
        partyMemberService.exitParty(party, member);
    }

    private Party createMockParty(String title, String content, Integer goalNumber, Long ownerId, Place place, PartyStatus partyStatus, Long member1, Long member2, Long member3, String ... hashtags) {
        Party party = Party.builder()
                .title(title)
                .content(content)
                .createTime(nextTime())
                .goalNumber(goalNumber)
                .place(place)
                .build();
        List<String> hashtagContents = Arrays.asList(hashtags);
        partyService.create(party, ownerId, hashtagContents);
        if (member1 != null) {
            addMember(party, member1);
        }
        if (member2 != null) {
            addMember(party, member2);
        }
        if (member3 != null) {
            addMember(party, member3);
        }
        party.changeStatus(partyStatus);
        return party;
    }


}