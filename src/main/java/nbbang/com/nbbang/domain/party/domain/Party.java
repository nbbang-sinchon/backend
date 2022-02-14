package nbbang.com.nbbang.domain.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.MemberParty;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;

import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity @Getter @Builder
@AllArgsConstructor
public class Party {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime;

    private Integer goalNumber;

    @Enumerated(STRING)
    private PartyStatus status;

    @Enumerated(STRING)
    private Place place;

    private LocalDateTime cancelTime;

    private Integer deliveryFee;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member owner;

    private Boolean isBlocked;

    @OneToMany(mappedBy = "party")
    private List<Hashtag> hashtags;

    @OneToMany(mappedBy = "party")
    private List<MemberParty> memberParties;

    protected Party() {}
}
