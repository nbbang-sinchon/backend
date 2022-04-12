package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyWishlist;
import nbbang.com.nbbang.domain.party.repository.PartyWishlistRepository;
import nbbang.com.nbbang.global.error.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.*;

@Service
@RequiredArgsConstructor
public class PartyWishlistService {
    private final PartyService partyService;
    private final PartyWishlistRepository partyWishlistRepository;
    private final MemberService memberService;

    @Transactional
    public Long addWishlistIfNotDuplicate(Long partyId, Long memberId) {
        partyWishlistRepository.findByPartyIdAndMemberId(partyId, memberId).ifPresent(p -> {
            throw new UserException(WISHLIST_DUPLICATE_ADD_ERROR);
        });
        Member member = memberService.findById(memberId);
        Party party = partyService.findById(partyId);
        PartyWishlist partyWishlist = PartyWishlist.createPartyWishlist(member, party);
        partyWishlistRepository.save(partyWishlist);
        return partyWishlist.getId();
    }

    @Transactional
    public void deleteWishlist(Long partyId, Long memberId) {
        PartyWishlist w = partyWishlistRepository.findByPartyIdAndMemberId(partyId, memberId)
                .orElseThrow(() -> new NotFoundException(WISHLIST_NOT_FOUND));
        partyWishlistRepository.delete(w);
    }

}
