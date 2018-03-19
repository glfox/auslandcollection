import React, { Component } from 'react';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import { login } from '../utils/services.js';
import { Form,FormGroup,Button,FormControl,ControlLabel,Table } from 'react-bootstrap';

import CreateUser from '../login/createuser.js';
class Login extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	username: "",
	    	password: "",
	    	details: null,
	    	error: null,
	    	loaded: true
	    };
	    this.handleUserNameChange = this.handleUserNameChange.bind(this);
	    this.handlePasswordChange = this.handlePasswordChange.bind(this);
	    this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleUserNameChange(event) {
		this.setState({
			username: event.target.value
		});
	}
	
	handlePasswordChange(event) {
		this.setState({
			password: event.target.value
		});
	}
	handleSubmit(event) {
		if (this.state.username && this.state.password) {
			this.setState({
				details: null,
	    		error: null,
				loaded: false
			})
			this.login(this.state.username, this.state.password);
		}
		event.preventDefault();
	}

	login(username, password) {
		login(this.state.username, this.state.password)
			.then(result => {
				console.log(result);
				if (result.status === 'ok' ) {
	      			this.setState({
						details: "successfully loged in",
						error: null,
						loaded: true
					});
	      		} else {
					this.setState({
						details: null,
						error: result.errorDetails,
						loaded: true
					});
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
		let loader = this.state.loaded? null : <div className="loader"/>
		return (
		  <Router>
			<div>
				<Form inline onSubmit={this.handleSubmit}>
					<FormGroup controlId="orderForm">
						<ControlLabel>username: </ControlLabel>{' '}
			          	<FormControl
				            type="text"
				            value={this.state.username}
				            placeholder="username"
				            onChange={this.handleUserNameChange}
				        />
			        </FormGroup>{' '}
			        <FormGroup controlId="orderForm">
					<ControlLabel>password: </ControlLabel>{' '}
		          	<FormControl
			            type="text"
			            value={this.state.password}
			            placeholder="password"
			            onChange={this.handlePasswordChange}
			        />
		        </FormGroup>{' '}
			        <Button bsStyle="primary" type="submit">submit</Button>
			    </Form>
		      	<br/>
		      	<div>
			  		<Table striped bordered condensed hover responsive>
			  			<tbody>
			  				{this.state.details}
			  			</tbody>
			  		</Table>
			  		{this.state.error}
			  		{loader}
			  	</div>
			  	<ul>
	                <li><Link to={'/CreateUser'}>创建新的用户</Link></li>
	            </ul>
			  	<Switch>
                  <Route exact path='/CreateUser' component={CreateUser} />   
                </Switch>
			</div>
		  </Router>
		)
	}
}

export default Login;