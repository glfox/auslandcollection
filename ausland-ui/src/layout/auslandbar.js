import React, { Component } from 'react';
import { Navbar, Nav, NavItem } from 'react-bootstrap';
import "./auslandbar.css";

class AuslandNavBar extends Component {

  render() {
    return (
      <Navbar collapseOnSelect>
        <Navbar.Header>
          <Navbar.Brand>
            <a href="#brand">Ausland Collection</a>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
          <Navbar.Collapse>
            <Nav pullLeft>
              <NavItem eventKey={1} href="#">
                订单查询
              </NavItem>
              <NavItem eventKey={2} href="#">
                库存查询
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
