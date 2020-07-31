import React from 'react';
import { connect } from 'react-redux';
import ReduxBlockUi from 'react-block-ui/redux';
import { Link } from 'react-router-dom';
import TextInput from 'components/TextInput';
import { IBigRequest } from 'interfaces/common';
import { IState } from 'redux-saga/reducers';
import { LOGIN } from 'redux-saga/actions';
import { login } from './actions';
import { LOGIN_SUCCESS, LOGIN_FAILED } from './reducers';
import './styles.css';

interface ILoginProps extends React.ClassAttributes<Login> {
  login(params: IBigRequest): void;
}

interface ILoginState {
  username: string;
  password: string;
  usernameError: boolean;
  passwordError: boolean;
  usernameErrorContent: string;
  passwordErrorContent: string;
}

class Login extends React.Component<ILoginProps, ILoginState> {
  constructor(props: ILoginProps) {
    super(props);
    this.state = {
      username: '',
      password: '',
      usernameError: false,
      usernameErrorContent: '',
      passwordError: false,
      passwordErrorContent: '',
    };
  }

  private onChangeUserName = (value: string) => {
    this.setState({ username: value, });
  }

  private onChangePassword = (value: string) => {
    this.setState({ password: value, });
  }

  private validate = () => {
    let passwordError = false;
    let passwordErrorContent = '';
    let usernameErrorContent = '';
    let usernameError = false;
    if (this.state.password.includes(' ') || this.state.password.trim() === '') {
      passwordError = true;
      passwordErrorContent = 'Mật khẩu không được trống, và không chứa dấu cách';
    }
    if (this.state.username.trim() === '') {
      usernameError = true;
      usernameErrorContent = 'Tên đăng nhập không được trống';
    }

    return { passwordError, passwordErrorContent, usernameErrorContent, usernameError };
  }

  private handleLogin = () => {
    const { passwordError, passwordErrorContent, usernameErrorContent, usernameError } = this.validate();
    this.setState({
      passwordError,
      passwordErrorContent,
      usernameErrorContent,
      usernameError,
    });
    if (passwordError === true || usernameError === true) {
      return;
    }
    const params = {
      path: '',
      param: {},
      data: {
        username: this.state.username,
        password: this.state.password,
      },
    };

    this.props.login(params);
  }

  render() {
    return (
      <div className="Container-login">
        <ReduxBlockUi
          tag="div"
          block={LOGIN}
          unblock={[LOGIN_SUCCESS, LOGIN_FAILED]}
        >
          <div className="Container-login-middle">
            <h2>Đăng nhập</h2>
            <p className="Long-introduction">Chào mừng! Vui lòng nhập thông tin đăng nhập của bạn hoặc đăng nhập bằng tài khoản mạng xã hội</p>
            <p>Đăng nhập với</p>
            <div className="Alternate-login-container">
              <div className="Alternate-login-option1 Alternate-login-option"><p className="Alternate-login-option-text">FACEBOOK</p></div>
              <div className="Alternate-login-option2 Alternate-login-option"><p className="Alternate-login-option-text">GOOGLE</p></div>
            </div>
            <p>Hoặc</p>

            <TextInput label={'Tên đăng nhập'} onChangeText={this.onChangeUserName} error={this.state.usernameError} errorContent={this.state.usernameErrorContent} onHandleSubmit={this.handleLogin} />
            <TextInput label={'Mật khẩu'} type={'password'} onChangeText={this.onChangePassword} error={this.state.passwordError} errorContent={this.state.passwordErrorContent} onHandleSubmit={this.handleLogin} />
            <div className="Login-option-container">
              <div className="Login-option-container-item">
                <label className="Checkbox-label">
                  <input type="checkbox" />
            Nhớ tài khoản trên thiết bị này
          </label>
              </div>
              <div className="Login-option-container-item Login-option-container-item1">
                <Link to={'/forgotPassword'}>
                  <p className="Forgot-password">Quên mật khẩu</p>
                </Link>
              </div>
            </div>
            <div className="Button-login-container">
              <div className="Button-login" onClick={this.handleLogin}>
                <h4 className="Button-login-text">Đăng nhập</h4>
              </div>
            </div>

          </div>
        </ReduxBlockUi>
      </div>
    );
  }
}

const mapStateToProps = (state: IState) => {
  return {
  };
};

export default connect(
  mapStateToProps,
  {
    login,
  }
)(Login);