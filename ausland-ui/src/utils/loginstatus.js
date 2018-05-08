import React from 'react';

const LoginContext = React.createContext({
  login: false
});

export function hasUserLogin(Component) {
  return function LoginStatus(props) {
    return (
      <LoginContext.Consumer>
        {hasUserLogin => <Component {...props} hasUserLogin={hasUserLogin} />}
      </LoginContext.Consumer>
    );
  };
}