package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;

import java.util.List;

public interface PartyRepositorySupport {

    List<Party> findByPlaceAndNotSelf(Long partyId);
}
