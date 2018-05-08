import React, { Component } from 'react';
//import { Link } from 'react-router-dom';
import { createuser } from '../utils/services.js';
import { Modal, Button } from 'react-bootstrap';
import classNames from 'classnames';
import CreateSuccess from './createsuccess.js'
//import Login from '../login/login.js';
 
class CreateUser extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
            username: {value: '', isValid: true, message: ''},
            password: {value: '', isValid: true, message: ''},
            confirmPassword: {value: '', isValid: true, message: ''},
            loaded: true,
            success: false,
            error: "",
        };

	    this.handleHide = this.handleHide.bind(this); 
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
	      if(state.password.value !== state.confirmPassword.value)
	      {
			  state.confirmPassword.isValid = false;
			  state.confirmPassword.message = "确认密码和密码必须完全相同";
			  this.setState(state);
			  return false;
		  }
	      return true;
	    }
	 
	resetValidationStates = () => {
		this.setState({
			username: {isValid: true, message: ''},
            password: {isValid: true, message: ''},
		})
      }
	
	onSubmit = (e) => {
		e.preventDefault();
		this.resetValidationStates();
		if (this.formIsValid()) {
			this.createuser(this.state.username.value, this.state.password.value);
		}

	}
	
	createuser(username, password) {
		this.setState({ loaded: false })
		createuser(this.state.username.value, this.state.password.value)
			.then(result => {
				console.log(result);
				if(result.status === 'ok')
				{
					this.setState({ 
						success: true,
						loaded: true
					})
				}
			}, err => {
				this.setState({
					error: "请求错误",
					loaded: true
				});
				console.log(err)
			});
	}

	handleHide() {
		this.setState({
			username: {value: '', isValid: true, message: ''},
            password: {value: '', isValid: true, message: ''},
            confirmPassword: {value: '', isValid: true, message: ''},
            loaded: true,
            success: false,
            error: "",
		})
		this.props.handleHide()
	}

	render() {
		var {username, password, confirmPassword} = this.state;
		var usernameGroupClass = classNames('form-group', {'has-error': !username.isValid});
	    var passwordGroupClass = classNames('form-group', {'has-error': !password.isValid});
	    var confirmGroupClass = classNames('form-group', {'has-error': !confirmPassword.isValid});
		let loader = (<div className="loader"/>)
		let registerForm = (
				<div>
					<form className="form-signin" onSubmit={this.onSubmit}>
	                  	<div className={usernameGroupClass}>
	                    	<input type="text" name="username" className="form-control"
	                      		placeholder="用户名" value={username.value} onChange={this.onChange} autoFocus />
	                    	<span className="help-block">{username.message}</span>
	                  	</div>

	                  	<div className={passwordGroupClass}>
	                    	<input type="password" name="password" className="form-control"
	                      		placeholder="登陆密码" value={password.value} onChange={this.onChange} />
	                    	<span className="help-block">{password.message}</span>
	                  	</div>

	                  	<div className={confirmGroupClass}>
	                    	<input type="password" name="confirmPassword" className="form-control"
	                      		placeholder="确认密码" value={confirmPassword.value} onChange={this.onChange} />
	                    	<span className="help-block">{confirmPassword.message}</span>
	                  	</div>
	                  	<button className="btn btn-primary" type="submit">注册</button>
	                </form>
	                <p className="text-danger">{this.state.error}</p>
	            </div>
			)
		let success = <CreateSuccess />
		let modalBody;
		if (this.state.loaded) {
			if (this.state.success) {
				modalBody = success;
			} else {
				modalBody = registerForm; 
			}
		} else {
			modalBody = loader;
		}
        return (
        	<Modal show={this.props.show} onHide={this.handleHide}>
	          	<Modal.Header closeButton>
	            	<Modal.Title>注册新用户</Modal.Title>
	          	</Modal.Header>
	          	<Modal.Body>
	          		{modalBody}
            	</Modal.Body>
	          	<Modal.Footer>
	            	<Button onClick={this.handleHide}>关闭</Button>
	          	</Modal.Footer>
	        </Modal>
        );
	}
}

export default CreateUser;