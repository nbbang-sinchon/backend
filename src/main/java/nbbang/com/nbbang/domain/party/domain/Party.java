package nbbang.com.nbbang.domain.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.chat.domain.ChatSession;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.dto.single.PartyUpdateServiceDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.webjars.NotFoundException;

import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;
import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.HASHTAG_NOT_FOUND;

@EntityListeners(AuditingEntityListener.class)
@Entity @Getter @Builder
@AllArgsConstructor
public class Party {
    @Id @GeneratedValue
    @Column(name = "party_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createTime;

    private Integer goalNumber;

    //@ValueOfEnum(enumClass = PartyStatus.class)
    @Enumerated(STRING)
    private PartyStatus status;

    //@ValueOfEnum(enumClass = Place.class)
    @Enumerated(STRING)
    private Place place;

    // private LocalDateTime cancelTime;

    @Builder.Default
    private Integer deliveryFee=0;

    @Builder.Default
    private Integer activeNumber=0;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

    private String bank;

    private String account;

    @Builder.Default // https://www.inflearn.com/questions/151658
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<PartyHashtag> partyHashtags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "party")
    private List<PartyMember> partyMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "party")
    private List<ChatSession> chatSessions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "party")
    private List<PartyWishlist> wishlists = new ArrayList<>();

    protected Party() {}

    public void addPartyHashtag(PartyHashtag partyHashtag){
        partyHashtags.add(partyHashtag);
    }

    public List<String> getHashtagContents() {
        return partyHashtags.stream().map(h -> h.getHashtag().getContent()).collect(Collectors.toList());
    }

    public PartyHashtag deletePartyHashtag(String content){
        PartyHashtag findPartyHashtag = partyHashtags.stream().filter(partyHashtag -> partyHashtag.getHashtag().getContent() == content)
                .findAny().orElseThrow(() -> new NotFoundException(HASHTAG_NOT_FOUND));
        partyHashtags.remove(findPartyHashtag);
        return findPartyHashtag;
    }

    public void addOwner(Member member) {

        this.owner = member;
    }

    public void addMemberParty(PartyMember partyMember) {
        this.getPartyMembers().add(partyMember);
    }

    public void exitMemberParty(PartyMember partyMember) {
        partyMembers.removeIf(mp -> mp.equals(partyMember));
    }


    public void changeGoalNumber(Integer goalNumber) {
        this.goalNumber = goalNumber;
    }

    public void changeStatus(PartyStatus status) {
        this.status = status;
    }

    public void update(PartyUpdateServiceDto partyUpdateServiceDto) {
        partyUpdateServiceDto.getTitle().ifPresent(title->this.title=title);
        partyUpdateServiceDto.getContent().ifPresent(content->this.content=content);
        partyUpdateServiceDto.getPlace().ifPresent(place->this.place=place);
        partyUpdateServiceDto.getGoalNumber().ifPresent(goalNumber->this.goalNumber=goalNumber);
    }

    public void updateActiveNumber(Integer updateNumbder){
        activeNumber = Optional.ofNullable(activeNumber).orElse(0) + updateNumbder;
    }

    public Long removeChatSession(ChatSession chatSession) {
        chatSessions.remove(chatSession);
        return id;
    }

    public Integer countPartyMemberNumber() {
        return partyMembers.size();
    }

    public Boolean isWishlistOf(Long memberId) {
        return wishlists.stream().anyMatch(w -> w.getMember().getId().equals(memberId));
    }

    public Boolean isWishlistOf(Member member) {
        return wishlists.stream().anyMatch(w -> w.getMember().equals(member));
    }
}
