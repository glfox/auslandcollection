import React from 'react';
//import { Link } from 'react-router-dom';
import { login } from '../utils/services.js';
import { Modal, Button } from 'react-bootstrap';
import CreateUser from '../login/createuser.js';
import classNames from 'classnames';
//import { withCookies, Cookies, cookie} from 'react-cookies';

class Login extends React.Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	username: {value: '', isValid: true, message: ''},
		    password: {value: '', isValid: true, message: ''},
		    registerShow: false,
		    error: null,
	    };
	    this.showRegister = this.showRegister.bind(this);
    	this.hideRegister = this.hideRegister.bind(this);
    	this.hideLogin = this.hideLogin.bind(this);
    	this.onChange = this.onChange.bind(this);
    	this.onSubmit = this.onSubmit.bind(this);
	}

	onChange (e) {
        var state = this.state;
        state[e.target.name].value = e.target.value;

        this.setState(state);
      }

	formIsValid () {
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
	 
	resetValidationStates (e) {
		this.setState({
			username: {value: this.state.username.value, isValid: true, message: ''},
		    password: {value: this.state.password.value, isValid: true, message: ''},
		})
      }
	
	onSubmit (e) {
		e.preventDefault();
		this.resetValidationStates(e);
		if (this.formIsValid()) {
			this.login(this.state.username.value, this.state.password.value);
		}
	}

	showRegister() {
		this.setState({
			registerShow: true
		})
	}

	hideRegister() {
		this.setState({
			registerShow: false
		})
	}

	login(username, password) {
		login(username, password)
			.then(result => {
				 console.log(result.status);
				if (result.status === 'ok' ) {
					/*this.props.history.push('/SearchOrders');*/
					this.props.loginStatus(true, username);
					this.hideLogin();
	      		}  
				else
				{
				    this.setState({
				    	error: <p className="text-danger">{result.errorDetails}</p>
				    })
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

	resetLoginValues() {
		this.setState({
	    	username: {value: '', isValid: true, message: ''},
		    password: {value: '', isValid: true, message: ''},
		    registerShow: false,
		    error: null,
	    });
	}

	hideLogin() {
		this.resetLoginValues();
		this.props.handleClose();
	}

	render() {
		var {username, password} = this.state;
		var usernameGroupClass = classNames('form-group', {'has-error': !username.isValid});
	    var passwordGroupClass = classNames('form-group', {'has-error': !password.isValid});
        return (
        	<Modal show={this.props.show} onHide={this.hideLogin}>
	          	<Modal.Header closeButton>
	            	<Modal.Title>登陆/注册</Modal.Title>
	          	</Modal.Header>
	          	<Modal.Body>
	                <form className="form-signin" onSubmit={this.onSubmit}>
	                  	<div className={usernameGroupClass}>
	                    	<input type="text" name="username" className="form-control form-group"
	                      		placeholder="用户名" value={username.value} onChange={this.onChange} autoFocus />
	                    	<span className="help-block">{username.message}</span>
	                  	</div>
	                  	<div className={passwordGroupClass}>
	                    	<input type="password" name="password" className="form-control form-group"
	                      		placeholder="登陆密码" value={password.value} onChange={this.onChange} />
	                    	<span className="help-block">{password.message}</span>
	                  	</div>
	                  	{this.state.error}
	                  	<button className="btn btn-primary" type="submit">登陆</button>{' '}
	                  	<button className="btn btn-info" type="button" onClick={this.showRegister}>注册</button>
	                </form>	                
	          	</Modal.Body>
	          	<Modal.Footer>
	            	<Button onClick={this.hideLogin}>关闭</Button>
	          	</Modal.Footer>

	          	<CreateUser show={this.state.registerShow} handleShow={this.showRegister} handleHide={this.hideRegister}/>
	        </Modal>
        )
	}
}

export default Login;