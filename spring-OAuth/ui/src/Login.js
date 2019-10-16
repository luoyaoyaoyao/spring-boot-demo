import React from 'react';

// import logo from './logo.svg';

class Login extends React.Component {
  constructor(props) {
    super(props);
  }
  componentDidMount() {

  }

  render() {
    const loginUrl = 'http://localhost:8080/login/github?auth_url=' + window.location.href;

    return (
      <div>
        
        <a href={loginUrl}>Github Login</a>
      </div>
    )
  };
}



export default Login;