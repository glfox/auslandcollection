import React, { Component } from 'react';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import { login } from '../utils/services.js';
import { Form,FormGroup,Button,FormControl,ControlLabel,Table } from 'react-bootstrap';
import classNames from 'classnames';
import CreateUser from '../login/createuser.js';

class Login extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	username: {value: '', isValid: true, message: ''},
		    password: {value: '', isValid: true, message: ''},
	    };
	     
	}
	onChange = (e) => {
        var state = this.state;
        state[e.target.name].value = e.target.value;

        this.setState(state);
      }
	formIsValid = () => {
		var state = this.state;
		  if(state.username.value.length < 4 || state.username.value.length > 20)
		  {
			  state.username.isValid = false;
			  state.username.message = "用户名长度必须在4和20之间";
			  this.setState(state);
			  return false;
		  }
	      if(state.password.value.length < 4 || state.password.value.length > 20)
	      {
			  state.password.isValid = false;
			  state.password.message = "密码长度必须在4和20之间";
			  this.setState(state);
			  return false;
		  }
	      return true;
	    }
	 
	resetValidationStates = () => {
        var state = this.state;

        Object.keys(state).map(key => {
          if (state[key].hasOwnProperty('isValid')) {
            state[key].isValid = true;
            state[key].message = '';
          }
        });
        this.setState(state);
      }
	
	onSubmit = (e) => {
		e.preventDefault();
		this.resetValidationStates();
		if (this.formIsValid()) {
			this.login(this.state.username.value, this.state.password.value);
		}

	}

	login(username, password) {
		login(this.state.username.value, this.state.password.value)
			.then(result => {
				 
				if (result.status === 'ok' ) {
					this.props.history.push('/SearchOrders');
	      		}  
				else
				{
				    this.setState({password:{value: '', isValid: 'false', message:result.errorDetails}})
				}
			}, err => {
				this.setState({
					details: null,
					error: "请求错误",
					loaded: true
				});
				console.log(err)
			});
	}

	render() {
		var {username, password} = this.state;
		  var usernameGroupClass = classNames('form-group', {'has-error': !username.isValid});
	      var passwordGroupClass = classNames('form-group', {'has-error': !password.isValid});
	    
	        return (
	        		<div className="container">
	                <form className="form-signin" onSubmit={this.onSubmit}>
	                  <h2 className="form-signin-heading">登陆</h2>

	                  <div className={usernameGroupClass}>
	                    <input type="text" name="username" className="form-control"
	                      placeholder="username" value={username.value} onChange={this.onChange} autoFocus />
	                    <span className="help-block">{username.message}</span>
	                  </div>

	                  <div className={passwordGroupClass}>
	                    <input type="password" name="password" className="form-control"
	                      placeholder="Password" value={password.value} onChange={this.onChange} />
	                    <span className="help-block">{password.message}</span>
	                  </div>

	                  <button className="btn btn-lg btn-primary btn-block" type="submit">登陆</button>
	                </form>
	                <Link to='/CreateUser'>注册</Link>
	              </div>
	               
	        );
	}
}

export default Login;