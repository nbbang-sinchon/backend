package nbbang.com.nbbang;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
public class Party {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime;

    private Integer goalNumber;

    private String partyStatus;

    private String place;

    private LocalDateTime cancelTime;

    private Integer fee;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member owner;

    private Boolean isBlocked;

}
