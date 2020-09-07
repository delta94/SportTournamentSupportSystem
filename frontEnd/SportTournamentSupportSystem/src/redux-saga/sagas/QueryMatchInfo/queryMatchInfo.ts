import { call, takeLatest, put } from 'redux-saga/effects';
import { query, METHOD } from 'utils/socketApi';
import { IRequest, IParams, IBigRequest } from 'interfaces/common';
import { COMMON_SHOW_NOTIFICATION, QUERY_MATCH_INFO } from 'redux-saga/actions';


const queryMatchInfo = (data: IParams, path: string | number, param: IParams) => {
  const uri = 'match';
  const datas = { ...data };
  const paths = path;
  const params = { ...param };
  return query(uri, METHOD.GET, datas, params, paths);
};

function* doQueryMatchInfo(request: IRequest<IBigRequest>) {
  try {
    const response = yield call(queryMatchInfo, request.data.data, request.data.path, request.data.param);
    const data = response.data.result;
    if (response.data.error.MessageCode === 0) {
      yield put({
        type: request.response.success,
        payload: data.Match,
      });
    } else {
      throw new Error(response.data.error.Message);
    }
  } catch (error) {
    yield put({
      type: request.response.failed,
    });
    yield put({
      type: COMMON_SHOW_NOTIFICATION,
      data: {
        type: 'error',
        title: 'QueryMatchInfo',
        content: error,
        time: new Date(),
      },
    });
  }
}

export default function* watchQueryMatchInfo() {
  yield takeLatest(QUERY_MATCH_INFO, doQueryMatchInfo);
}
