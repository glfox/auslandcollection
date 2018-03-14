import React, { Component } from 'react';
import { Navbar, Nav, NavItem} from 'react-bootstrap';

class AuslandNavBar extends Component {

  render() {
    return (
      <Navbar collapseOnSelect>
        <Navbar.Header>
          <Navbar.Brand>
            <a href="#brand">React-Bootstrap</a>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav>
            <NavItem eventKey={1} href="#">
              Link 1
            </NavItem>
            <NavItem eventKey={2} href="#">
              Link 2
            </NavItem>
            <NavItem eventKey={3} href="#">
              Link 3
            </NavItem>
            <NavItem eventKey={4} href="#">
              Link 4
            </NavItem>
          </Nav>
          <Nav pullRight>
            <NavItem eventKey={5} href="#">
              登陆
            </NavItem>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }
}

export default AuslandNavBar;
