package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.util.List;

public interface PartyRepositorySupport {

    List<Party> findByPlaceAndNotSelf(Long partyId);

    List<Party> findByPlaceAndNotSelf(Long partyId, Place place);

    Party findWithPartyMember(Long partyId);

}
