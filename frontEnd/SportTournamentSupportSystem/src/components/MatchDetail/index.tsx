import React from 'react';
import { connect } from 'react-redux';
import { isAfter, isBefore, setHours, setMinutes } from 'date-fns';
import DatePicker from "react-datepicker";
import { IParams, IBigRequest } from 'interfaces/common';
import { IState } from 'redux-saga/reducers';
import { MATCH_STATUS } from 'global';
import { formatStringToDate, formatDateToString, formatDateToDisplay } from 'utils/datetime';
import { updateMatchInfo, updateMatchInfoBeforeStart } from 'components/MatchSetting/actions';
import './styles.css';

interface IMatchDetailProps extends React.ClassAttributes<MatchDetail> {
  info: IParams;
  listTeam?: IParams[] | null;
  matchInfo: IParams | null;
  allMatches: IParams | null;
  beforeInfo?: IParams;
  tournamentId: number;
  lowerBracket?: boolean;
  tableId: number | null;
  matchType: 'se' | 'win' | 'lose' | 'rr' | 'sum';
  tournamentInfo: IParams | null;
  dateNextRound: Date | null;
  datePreviousRound: Date | null;

  updateMatchInfo(params: IBigRequest): void;
  updateMatchInfoBeforeStart(params: IBigRequest): void;
}

interface IMatchDetailState {
  editMode: boolean;
  winner: boolean | null;
  location: string;
  time: Date;
  timeError: boolean;
  timeErrorContent: string;
  team1Score: number;
  team2Score: number;
}


class MatchDetail extends React.Component<IMatchDetailProps, IMatchDetailState> {
  constructor(props: IMatchDetailProps) {
    super(props);
    this.state = {
      editMode: false,
      winner: null,
      location: '',
      time: new Date(),
      timeError: false,
      timeErrorContent: '',
      team1Score: 0,
      team2Score: 0,
    };
  }

  shouldComponentUpdate(nextProps: IMatchDetailProps, nextState: IMatchDetailState) {
    if (this.state.editMode !== nextState.editMode && nextState.editMode === true) {
      this.setState({
        location: nextProps.info != null ? nextProps.info.location as string : '',
        time: nextProps.info != null ? (nextProps.info.time === '' ? new Date() : formatStringToDate(nextProps.info.time as string, 'yyyy-MM-dd HH:mm:ss')) : new Date(),
      })
    }
    if (((this.props.allMatches !== nextProps.allMatches || nextProps.matchInfo !== this.props.matchInfo))) {
      const tempValue = (nextProps.allMatches!.Matchs as IParams[]).findIndex(element => element.id === nextProps.matchInfo!.id);
      if (tempValue !== -1) {
        if ((nextProps.allMatches!.Matchs as IParams[])[tempValue].winnerId == null) {
          this.setState({
            winner: null,
          });
        } else {
          if ((nextProps.allMatches!.Matchs as IParams[])[tempValue].team1Id == null) {
            if ((nextProps.allMatches!.Matchs as IParams[])[tempValue].team2Id != null) {
              this.setState({
                winner: false,
              });
            } else {
              this.setState({
                winner: null,
              });
            }
          } else {
            if ((nextProps.allMatches!.Matchs as IParams[])[tempValue].team2Id == null) {
              this.setState({
                winner: true,
              });
            } else {
              if ((nextProps.allMatches!.Matchs as IParams[])[tempValue].winnerId === (nextProps.allMatches!.Matchs as IParams[])[tempValue].team1Id) {
                this.setState({
                  winner: true,
                });
              } else {
                this.setState({
                  winner: false,
                });
              }
            }
          }
        }
      }
      if ((nextProps.allMatches!.Scores as IParams)[nextProps.matchInfo!.id as string] != null) {
        this.setState({
          team1Score: ((nextProps.allMatches!.Scores as IParams)[nextProps.matchInfo!.id as string] as IParams).team1 as number,
          team2Score: ((nextProps.allMatches!.Scores as IParams)[nextProps.matchInfo!.id as string] as IParams).team2 as number,
        });
      }
    }
    return true;
  }

  componentDidMount() {
    if (this.props.allMatches != null && this.props.matchInfo != null) {
      const tempValue = (this.props.allMatches!.Matchs as IParams[]).findIndex(element => element.id === this.props.matchInfo!.id);
      if (tempValue !== -1) {
        if ((this.props.allMatches!.Matchs as IParams[])[tempValue].winnerId == null) {
          this.setState({
            winner: null,
          });
        } else {
          if ((this.props.allMatches!.Matchs as IParams[])[tempValue].team1Id == null) {
            if ((this.props.allMatches!.Matchs as IParams[])[tempValue].team2Id != null) {
              this.setState({
                winner: false,
              });
            } else {
              this.setState({
                winner: null,
              });
            }
          } else {
            if ((this.props.allMatches!.Matchs as IParams[])[tempValue].team2Id == null) {
              this.setState({
                winner: true,
              });
            } else {
              if ((this.props.allMatches!.Matchs as IParams[])[tempValue].winnerId === (this.props.allMatches!.Matchs as IParams[])[tempValue].team1Id) {
                this.setState({
                  winner: true,
                });
              } else {
                this.setState({
                  winner: false,
                });
              }
            }
          }
        }
      }
      if ((this.props.allMatches!.Scores as IParams)[this.props.matchInfo!.id as string] != null) {
        this.setState({
          team1Score: ((this.props.allMatches!.Scores as IParams)[this.props.matchInfo!.id as string] as IParams).team1 as number,
          team2Score: ((this.props.allMatches!.Scores as IParams)[this.props.matchInfo!.id as string] as IParams).team2 as number,
        });
      }
    }
  }

  private onEditMode = () => {
    this.setState({
      editMode: true,
    });
  }

  private offEditMode = () => {
    const { timeError, timeErrorContent } = this.validateTime();
    this.setState({
      timeError,
      timeErrorContent,
    });
    if (timeError === true) {
      return;
    }
    if (this.props.matchInfo != null) {
      const params = {
        path: '',
        param: {
          id: this.props.matchInfo.id,
        },
        data: {
          tournamentId: this.props.matchInfo.tournamentId,
          location: this.state.location,
          time: formatDateToString(this.state.time, 'yyyy-MM-dd HH:mm:ss'),
          loserId: this.props.matchInfo.loserId,
          name: this.props.matchInfo.name,
          status: this.props.matchInfo.status,
          team1Bonus: this.props.matchInfo.team1Bonus,
          team1Id: this.props.matchInfo.team1Id,
          team2Bonus: this.props.matchInfo.team2Bonus,
          team2Id: this.props.matchInfo.team2Id,
          url: this.props.matchInfo.url,
          winnerId: this.props.matchInfo.winnerId,
        },
      };
      this.props.updateMatchInfo(params);
    } else {
      if (this.props.beforeInfo != null) {
        const params = {
          path: '',
          param: {
            degree: (this.props.beforeInfo.degree as number) + 1,
            nodeId: this.props.beforeInfo.id,
            tournamentId: this.props.tournamentId,
            location: this.props.lowerBracket === true ? 2 : (this.props.matchType === 'se' ? 0 : (this.props.matchType === 'win' ? 1 : 3)),
            tableId: this.props.tableId != null ? this.props.tableId : -1,
          },
          data: {
            location: this.state.location,
            time: formatDateToString(this.state.time, 'yyyy-MM-dd HH:mm:ss'),
          },
        };
        this.props.updateMatchInfoBeforeStart(params);
      } else {
        const params = {
          path: '',
          param: {
            degree: 0,
            nodeId: this.props.info.matchNo,
            tournamentId: this.props.tournamentId,
            location: -1,
            tableId: this.props.tableId != null ? this.props.tableId : -1,
          },
          data: {
            location: this.state.location,
            time: formatDateToString(this.state.time, 'yyyy-MM-dd HH:mm:ss'),
          },
        };
        this.props.updateMatchInfoBeforeStart(params);
      }

    }
    this.setState({
      editMode: false,
    });
  }

  private onChangeLocation = (value: React.ChangeEvent<HTMLInputElement>) => {
    this.setState({
      location: value.target.value,
    });
  }

  private validateTime = () => {
    let timeError = false;
    let timeErrorContent = '';
    if (isAfter(this.state.time, formatStringToDate((this.props.tournamentInfo!.Tournament as IParams).closingTime as string, 'yyyy-MM-dd HH:mm:ss'))) {
      timeError = true;
      timeErrorContent = `Thời gian không được sau ngày bế mạc giải (${formatDateToDisplay((this.props.tournamentInfo!.Tournament as IParams).closingTime as string, 'yyyy-MM-dd', 'yyyy-MM-dd HH:mm:ss')})`;
    }
    if (isBefore(this.state.time, formatStringToDate((this.props.tournamentInfo!.Tournament as IParams).openingTime as string, 'yyyy-MM-dd HH:mm:ss'))) {
      timeError = true;
      timeErrorContent = `Thời gian không được trước ngày khai mạc giải (${formatDateToDisplay((this.props.tournamentInfo!.Tournament as IParams).openingTime as string, 'yyyy-MM-dd', 'yyyy-MM-dd HH:mm:ss')})`;
    }
    if (this.props.datePreviousRound != null) {
      if (isBefore(this.state.time, this.props.datePreviousRound)) {
        timeError = true;
        timeErrorContent = `Thời gian không được trước thời gian của trận muộn nhất vòng trước (${formatDateToString(this.props.datePreviousRound, 'yyyy-MM-dd HH:mm:ss')})`;
      } else {
        if (this.props.dateNextRound != null) {
          if (isAfter(this.state.time, this.props.dateNextRound)) {
            timeError = true;
            timeErrorContent = `Thời gian không được sau thời gian của trận sớm nhất vòng sau (${formatDateToString(this.props.dateNextRound, 'yyyy-MM-dd HH:mm:ss')})`;
          }
        }
      }
    } else {
      if (this.props.dateNextRound != null) {
        if (isAfter(this.state.time, this.props.dateNextRound)) {
          timeError = true;
          timeErrorContent = `Thời gian không được sau thời gian của trận sớm nhất vòng sau (${formatDateToString(this.props.dateNextRound, 'yyyy-MM-dd HH:mm:ss')})`;
        }
      }
    }

    return { timeError, timeErrorContent };
  }

  private handleChangeTime = (value: Date) => {
    this.setState({
      time: value,
    });
  };

  render() {
    return (
      <div
        className="MatchDetail-container"
      >
        <div className="MatchDetail-set-container">
          {this.props.matchInfo != null ?
            (this.props.matchInfo.status !== MATCH_STATUS.FINISHED &&
              (this.state.editMode === false ? <p className="MatchSetting-set-text" onClick={this.onEditMode}>Sửa</p> : <p className="MatchSetting-set-text" onClick={this.offEditMode}>Lưu</p>)) : (this.state.editMode === false ? <p className="MatchSetting-set-text" onClick={this.onEditMode}>Sửa</p> : <p className="MatchSetting-set-text" onClick={this.offEditMode}>Lưu</p>)}
        </div>
        <div
          className="MatchDetail-info-container"
        >
          <p className={'MatchDetail-info-header-text'}>Trận {this.props.info.name}</p>
        </div >
        <div
          className="MatchDetail-teams-container"
        >
          <div
            className="MatchDetail-team1-container"
          >
            <div className={'MatchDetail-team1-name-container'}>
              {this.props.info.team1 != null ?
                ((this.props.info.team1 as IParams).team != null ? <p className={this.state.winner === true ? 'MatchDetail-team-winner' : 'MatchDetail-team'}>{`${((this.props.info.team1 as IParams).team as IParams).fullName} (${((this.props.info.team1 as IParams).team as IParams).shortName})`}</p> :
                  (((this.props.info.team1 as IParams).description as IParams).descType === 0 ? <p className={'MatchDetail-team'}>{this.props.listTeam![(((this.props.info.team1 as IParams).description as IParams).unitIndex as number) - 1].shortName ? this.props.listTeam![(((this.props.info.team1 as IParams).description as IParams).unitIndex as number) - 1].shortName : ''}</p> : <p style={{ fontStyle: 'italic', color: 'white' }}>{((this.props.info.team1 as IParams).description as IParams).description}</p>)) : <p className={'MatchDetail-team'}>(Chưa có)</p>
              }
            </div>
            <div className={'MatchDetail-team1-score-container'}>
              <p className={this.state.winner === true ? 'MatchDetail-team-winner' : 'MatchDetail-team'}>{this.state.team1Score}</p>
            </div>
          </div>
          <div
            className="MatchDetail-teams-middle-container"
          >
            <p className={'MatchDetail-teams-middle-text MatchDetail-team'}>VS</p>
          </div >
          <div
            className="MatchDetail-team2-container"
          >
            <div className={'MatchDetail-team2-name-container'}>
              {this.props.info.team2 != null ?
                ((this.props.info.team2 as IParams).team != null ? <p className={this.state.winner === false ? 'MatchDetail-team-winner' : 'MatchDetail-team'}>{`${((this.props.info.team2 as IParams).team as IParams).fullName} (${((this.props.info.team2 as IParams).team as IParams).shortName})`}</p> :
                  (((this.props.info.team2 as IParams).description as IParams).descType === 0 ?
                    <p className={'MatchDetail-team'}>{this.props.listTeam![(((this.props.info.team2 as IParams).description as IParams).unitIndex as number) - 1].shortName ? this.props.listTeam![(((this.props.info.team2 as IParams).description as IParams).unitIndex as number) - 1].shortName : ''}</p> : <p style={{ fontStyle: 'italic', color: 'white' }}>{((this.props.info.team2 as IParams).description as IParams).description === '-1' ? '(Chưa có)' : ((this.props.info.team2 as IParams).description as IParams).description}</p>)) : <p className={'MatchDetail-team'}>(Không có)</p>
              }
            </div>
            <div className={'MatchDetail-team2-score-container'}>
              <p className={this.state.winner === false ? 'MatchDetail-team-winner' : 'MatchDetail-team'}>{this.state.team2Score}</p>
            </div>
          </div >
        </div >
        <div
          className="MatchDetail-info-container"
        >
          {this.state.editMode === false ? <p className={'MatchDetail-team'}>Địa điểm: {this.props.info.location != null && (this.props.info.location as string).trim() !== '' ? this.props.info.location : '(Chưa có)'}</p> : <label style={{ color: 'white' }}>Địa điểm: <input value={this.state.location} type={'text'} onChange={this.onChangeLocation} /></label>}
          {this.state.editMode === false ? <p className={'MatchDetail-team'}>Thời gian: {this.props.info.time != null && (this.props.info.time as string).trim() !== '' ? this.props.info.time : '(Chưa có)'}</p> : <label style={{ color: 'white' }}>Thời gian:
            <DatePicker
              selected={this.state.time}
              onChange={this.handleChangeTime}
              showTimeSelect
              includeTimes={[
                setHours(setMinutes(new Date(), 0), 0),
                setHours(setMinutes(new Date(), 0), 1),
                setHours(setMinutes(new Date(), 0), 2),
                setHours(setMinutes(new Date(), 0), 3),
                setHours(setMinutes(new Date(), 0), 4),
                setHours(setMinutes(new Date(), 0), 5),
                setHours(setMinutes(new Date(), 0), 6),
                setHours(setMinutes(new Date(), 0), 7),
                setHours(setMinutes(new Date(), 0), 8),
                setHours(setMinutes(new Date(), 0), 9),
                setHours(setMinutes(new Date(), 0), 10),
                setHours(setMinutes(new Date(), 0), 11),
                setHours(setMinutes(new Date(), 0), 12),
                setHours(setMinutes(new Date(), 0), 13),
                setHours(setMinutes(new Date(), 0), 14),
                setHours(setMinutes(new Date(), 0), 15),
                setHours(setMinutes(new Date(), 0), 16),
                setHours(setMinutes(new Date(), 0), 17),
                setHours(setMinutes(new Date(), 0), 18),
                setHours(setMinutes(new Date(), 0), 19),
                setHours(setMinutes(new Date(), 0), 20),
                setHours(setMinutes(new Date(), 0), 21),
                setHours(setMinutes(new Date(), 0), 22),
                setHours(setMinutes(new Date(), 0), 23),
                setHours(setMinutes(new Date(), 30), 0),
                setHours(setMinutes(new Date(), 30), 1),
                setHours(setMinutes(new Date(), 30), 2),
                setHours(setMinutes(new Date(), 30), 3),
                setHours(setMinutes(new Date(), 30), 4),
                setHours(setMinutes(new Date(), 30), 5),
                setHours(setMinutes(new Date(), 30), 6),
                setHours(setMinutes(new Date(), 30), 7),
                setHours(setMinutes(new Date(), 30), 8),
                setHours(setMinutes(new Date(), 30), 9),
                setHours(setMinutes(new Date(), 30), 10),
                setHours(setMinutes(new Date(), 30), 11),
                setHours(setMinutes(new Date(), 30), 12),
                setHours(setMinutes(new Date(), 30), 13),
                setHours(setMinutes(new Date(), 30), 14),
                setHours(setMinutes(new Date(), 30), 15),
                setHours(setMinutes(new Date(), 30), 16),
                setHours(setMinutes(new Date(), 30), 17),
                setHours(setMinutes(new Date(), 30), 18),
                setHours(setMinutes(new Date(), 30), 19),
                setHours(setMinutes(new Date(), 30), 20),
                setHours(setMinutes(new Date(), 30), 21),
                setHours(setMinutes(new Date(), 30), 22),
                setHours(setMinutes(new Date(), 30), 23),
              ]}
              dateFormat="MMMM d, yyyy HH:mm"
            /></label>}
          <div className="MatchDetail-error-text-container">{this.state.timeError && <p className="TextInput-error-text">{this.state.timeErrorContent}</p>}</div>
        </div >
      </div >
    );
  }
}

const mapStateToProps = (state: IState) => {
  return {
    listTeam: state.listTeam,
    tournamentInfo: state.tournamentInfo,
  };
};

export default connect(
  mapStateToProps,
  { updateMatchInfo, updateMatchInfoBeforeStart }
)(MatchDetail);