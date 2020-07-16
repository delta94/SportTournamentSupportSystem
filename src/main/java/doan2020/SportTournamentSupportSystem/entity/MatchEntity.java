
package doan2020.SportTournamentSupportSystem.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sun.istack.NotNull;



@Entity
@Table(name = "matches")
@EntityListeners(AuditingEntityListener.class)
public class MatchEntity{

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private int numOfSet;
	
	private Date expectedDate;
	
	private String expectedPlace;
	
	private Date realDate;
	
	private String realPlace;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;
	

	@ManyToOne
	@JoinColumn(name = "competitionId")
	private CompetitionEntity competition;
	
	@ManyToMany(mappedBy = "matchesList")
	private Collection<TeamEntity> teams;
	
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
	private Collection<ResultEntity> results;


	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumofset() {
		return numOfSet;
	}
	
	public void setNumofset(int numOfSet) {
		this.numOfSet = numOfSet;
	}
	
	public Date getExpecteddate() {
		return expectedDate;
	}
	
	public void setExpecteddate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}
	
	public String getExpectedplace() {
		return expectedPlace;
	}
	
	public void setExpectedplace(String expectedPlace) {
		this.expectedPlace = expectedPlace;
	}
	
	public Date getRealdate() {
		return realDate;
	}
	
	public void setRealdate(Date realDate) {
		this.realDate = realDate;
	}
	
	public String getRealplace() {
		return realPlace;
	}
	
	public void setRealplace(String realPlace) {
		this.realPlace = realPlace;
	}
	
	public String getCreatedby() {
		return createdBy;
	}
	
	public void setCreatedby(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreateddate() {
		return createdDate;
	}
	
	public void setCreateddate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getModifiedby() {
		return modifiedBy;
	}
	
	public void setModifiedby(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public Date getModifieddate() {
		return modifiedDate;
	}
	
	public void setModifieddate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public CompetitionEntity getCompetition() {
		return competition;
	}
	
	public void setCompetition(CompetitionEntity competition) {
		this.competition = competition;
	}
	
	public Collection<TeamEntity> getTeamslist() {
		return teams;
	}
	
	public void setTeamslist(Collection<TeamEntity> teamsList) {
		this.teams = teamsList;
	}
	
	public Collection<ResultEntity> getResults() {
		return results;
	}
	
	public void setResults(Collection<ResultEntity> results) {
		this.results = results;
	}
	

}