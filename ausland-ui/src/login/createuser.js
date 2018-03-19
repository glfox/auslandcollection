import React, { Component } from 'react';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import { createuser } from '../utils/services.js';
import { Form,FormGroup,Button,FormControl,ControlLabel,Table } from 'react-bootstrap';
import Login from '../login/login.js';

class CreateUser extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	username: "",
	    	password: "",
	    	valid: false,
	    	details: null,
	    	error: null,
	    	loaded: true
	    };
	    this.handleUserNameChange = this.handleUserNameChange.bind(this);
	    this.handlePasswordChange = this.handlePasswordChange.bind(this);
	    this.checkPasswordsMatch = this.checkPasswordsMatch.bind(this);
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
	 
	 checkPasswordsMatch(value) {
	        var match = this.state.password.getValue() === value;
	        this.setState({
	            valid: match,
	            password: value
	        });
	        return match;
	    }
	 
	handleSubmit(event) {
		if (this.state.username && this.state.password && this.state.valid) {
			this.setState({
				details: null,
	    		error: null,
				loaded: false
			})
			this.createuser(this.state.username, this.state.password);
		}
		event.preventDefault();
	}
	
	createuser(username, password) {
		createuser(this.state.username, this.state.password)
			.then(result => {
				console.log(result);
				if (result.status === 'ok' ) {
	      			this.setState({
						details: "successfully created user",
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
		        <FormGroup controlId="orderForm">
				<ControlLabel>password confirm: </ControlLabel>{' '}
	          	<FormControl
		            type="text"
		            value={this.state.passwordConfirm}
		            placeholder="passwordConfirm"
		            onChange={this.handlePasswordConfirmChange}
	          	    validate={this.checkPasswordsMatch}
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
	                 <li><Link to={'/Login'}>Login</Link></li>
                 </ul>
			  	<Switch>
                  <Route exact path='/Login' component={Login} />   
                </Switch>
			</div>
		  </Router>
		)
	}
}

export default CreateUser;