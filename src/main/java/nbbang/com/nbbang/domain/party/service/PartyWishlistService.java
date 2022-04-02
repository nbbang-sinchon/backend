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
    public void addWishlistIfNotDuplicate(Long memberId, Long partyId) {
        partyWishlistRepository.findByMemberIdAndPartyId(memberId, partyId).ifPresent(p -> {
            throw new UserException(WISHLIST_DUPLICATE_ADD_ERROR);
        });
        Member member = memberService.findById(memberId);
        Party party = partyService.findById(partyId);
        PartyWishlist partyWishlist = PartyWishlist.createPartyWishlist(member, party);
        partyWishlistRepository.save(partyWishlist);
    }

    @Transactional
    public void deleteWishlist(Long memberId, Long partyId) {
        PartyWishlist w = partyWishlistRepository.findByMemberIdAndPartyId(memberId, partyId)
                .orElseThrow(() -> new NotFoundException(WISHLIST_NOT_FOUND));
        partyWishlistRepository.delete(w);
    }

}
