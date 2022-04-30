package nbbang.com.nbbang.domain.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.hashtag.domain.PartyHashtag;
import nbbang.com.nbbang.domain.partymember.domain.PartyMember;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.dto.single.PartyUpdateServiceDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.webjars.NotFoundException;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
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

    @Builder.Default
    private Integer goalNumber=1;

    //@ValueOfEnum(enumClass = PartyStatus.class)
    @Enumerated(STRING)
    private PartyStatus status;

    //@ValueOfEnum(enumClass = Place.class)
    @Enumerated(STRING)
    private Place place;

    @Builder.Default
    private Integer deliveryFee=0;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

    private String bank;

    private String accountNumber;

    @Builder.Default
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true,mappedBy = "party")
    private List<PartyHashtag> partyHashtags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "party" )
    private List<PartyMember> partyMembers = new ArrayList<>();

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

    public List<PartyHashtag> deletePartyHashtags(List<String> contents) {
        List<PartyHashtag> deletedPartyHashtags = partyHashtags.stream().filter(partyHashtag -> contents.contains(partyHashtag.getContent())).collect(Collectors.toList());
        partyHashtags.removeAll(deletedPartyHashtags);
        return deletedPartyHashtags;
    }

    public void setOwner(Member member) {
        this.owner = member;
    }

    public void addPartyMember(PartyMember partyMember) {
        this.getPartyMembers().add(partyMember);
    }

    public void exitPartyMember(Long partyMemberId) {
        partyMembers.removeIf(mp -> mp.getId().equals(partyMemberId));
    }
    public void exitPartyMember(PartyMember partyMember) {
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

    public Integer countPartyMemberNumber() {
        return partyMembers.size();
    }

    public Boolean isWishlistOf(Long memberId) {
        return wishlists.stream().anyMatch(w -> w.getMember().getId().equals(memberId));
    }

    public Boolean isWishlistOf(Member member) {
        return wishlists.stream().anyMatch(w -> w.getMember().equals(member));
    }

    public void changeDeliveryFee(Integer deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public void changeAccount(Account account) {
        this.bank = account.getBank();
        this.accountNumber = account.getAccountNumber();
    }

    public static Field getField(String field) throws NoSuchFieldException {
        return Party.class.getDeclaredField(field);
    }


}
