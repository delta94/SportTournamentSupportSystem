package doan2020.SportTournamentSupportSystem.dto;

public class GroupStageSettingDTO {
	private Long id;
	
	private Long competitionId;

	private int maxTeamPerTable;

	private int advanceTeamPerTable;
	
	private boolean hasHomeMatch;
	private Long formatId;
	
	private String status;

	private String url;

	public GroupStageSettingDTO() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getMaxTeamPerTable() {
		return maxTeamPerTable;
	}

	public void setMaxTeamPerTable(int maxTeamPerTable) {
		this.maxTeamPerTable = maxTeamPerTable;
	}

	public int getAdvanceTeamPerTable() {
		return advanceTeamPerTable;
	}

	public void setAdvanceTeamPerTable(int advanceTeamPerTable) {
		this.advanceTeamPerTable = advanceTeamPerTable;
	}



	public boolean isHasHomeMatch() {
		return hasHomeMatch;
	}

	public void setHasHomeMatch(boolean hasHomeMatch) {
		this.hasHomeMatch = hasHomeMatch;
	}

	public Long getFormatId() {
		return formatId;
	}

	public void setFormatId(Long formatId) {
		this.formatId = formatId;
	}

	public Long getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Long competitionId) {
		this.competitionId = competitionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	
}
