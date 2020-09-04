import { call, takeLatest, put } from 'redux-saga/effects';
import { query, METHOD } from 'utils/socketApi';
import { IRequest, IParams, IBigRequest } from 'interfaces/common';
import { COMMON_SHOW_NOTIFICATION, QUERY_ALL_REPORTS } from 'redux-saga/actions';


const queryAllReports = (data: IParams, path: string | number, param: IParams) => {
  const uri = data.type === 2 ? 'admin/viewSystemReport' : 'admin/viewViolationReport';
  const datas = { ...data };
  const paths = path;
  const params = { ...param };
  return query(uri, METHOD.GET, datas, params, paths);
};

function* doQueryAllReports(request: IRequest<IBigRequest>) {
  try {
    const response = yield call(queryAllReports, request.data.data, request.data.path, request.data.param);
    const data = response.data.result;
    if (response.data.error.MessageCode === 0) {
      yield put({
        type: request.response.success,
        payload: data,
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
        title: 'QueryAllReports',
        content: error,
        time: new Date(),
      },
    });
  }
}

export default function* watchQueryAllReports() {
  yield takeLatest(QUERY_ALL_REPORTS, doQueryAllReports);
}
