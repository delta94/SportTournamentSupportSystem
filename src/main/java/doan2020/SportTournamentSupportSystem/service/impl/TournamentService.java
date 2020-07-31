package doan2020.SportTournamentSupportSystem.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import doan2020.SportTournamentSupportSystem.entity.TournamentEntity;
import doan2020.SportTournamentSupportSystem.repository.TournamentRepository;
import doan2020.SportTournamentSupportSystem.service.ITournamentService;

@Service
public class TournamentService implements ITournamentService {

	@Autowired
	private TournamentRepository tournamentRepository;

	@Override
	public void addOne(TournamentEntity tournament) {
		
		tournamentRepository.save(tournament);

	}

	@Override
	public void addMany(Collection<TournamentEntity> tournaments) {
		for (TournamentEntity tournament : tournaments)
			tournamentRepository.save(tournament);

	}

	@Override
	public TournamentEntity findOneById(Long id) {
		TournamentEntity res = null;
		try {
			res = tournamentRepository.findOneById(id);
		} catch (Exception e) {
			System.out.println("Has exceoption in TournamentService.findById");
			return null;
		}
		
		return res;
	}

	@Override
	public TournamentEntity findByName(String name) {
		TournamentEntity res = null;
		try {
			res = tournamentRepository.findByShortName(name);
		} catch (Exception e) {
			return null;
		}
		
		return res;
	}

	@Override
	public Collection<TournamentEntity> findAll(Pageable pageable) {
		
		return (Collection<TournamentEntity>) tournamentRepository.findAll(pageable).getContent();
	}

	@Override
	public TournamentEntity update(Long id, TournamentEntity newData) {
		
		TournamentEntity old = tournamentRepository.findOneById(id);
		old.setFullName(newData.getFullName());
		old.setShortName(newData.getShortName());
		old.setDescription(newData.getDescription());
		old.setCreator(newData.getCreator());
		old.setOpeningLocation(newData.getOpeningLocation());
		old.setOpeningTime(newData.getOpeningTime());
		old.setClosingLocation(newData.getClosingLocation());
		old.setClosingTime(newData.getClosingTime());
		old.setDonor(newData.getDonor());
		old.setStatus(newData.getStatus());
		old.setUrl(newData.getUrl());
		old = tournamentRepository.save(old);
		return old;
	}

	@Override
	public Collection<TournamentEntity> findAllByCreator(Pageable pageable, Long creatorId) {
		// TODO Auto-generated method stub
		return (Collection<TournamentEntity>) tournamentRepository.findByCreatorId(creatorId, pageable).getContent();
	}
}
