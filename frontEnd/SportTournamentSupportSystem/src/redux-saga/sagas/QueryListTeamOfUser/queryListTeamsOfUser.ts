import { call, takeLatest, put } from 'redux-saga/effects';
import { query, METHOD } from 'utils/socketApi';
import { IRequest, IParams, IBigRequest } from 'interfaces/common';
import { COMMON_SHOW_NOTIFICATION, QUERY_LIST_TEAM_OF_USER } from 'redux-saga/actions';


const queryListTeamsOfUser = (data: IParams, path: string | number, param: IParams) => {
  const uri = 'teams/getAllByUserId';
  const datas = { ...data };
  const paths = path;
  const params = { ...param };
  return query(uri, METHOD.GET, datas, params, paths);
};

function* doQueryListTeamsOfUser(request: IRequest<IBigRequest>) {
  try {
    const response = yield call(queryListTeamsOfUser, request.data.data, request.data.path, request.data.param);
    const data = response.data.result;
    if (response.data.error.messageCode === 0) {
      yield put({
        type: request.response.success,
        payload: data,
      });
    } else {
      throw new Error(response.data.error.message);
    }
  } catch (error) {
    yield put({
      type: request.response.failed,
    });
    yield put({
      type: COMMON_SHOW_NOTIFICATION,
      data: {
        type: 'error',
        title: 'QueryListTeamsOfUser',
        content: error,
        time: new Date(),
      },
    });
  }
}

export default function* watchQueryListTeamsOfUser() {
  yield takeLatest(QUERY_LIST_TEAM_OF_USER, doQueryListTeamsOfUser);
}
