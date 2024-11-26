import React from 'react';
import { Link } from 'react-router-dom';
import {
    Button,
    Collapse,
    Navbar
} from 'reactstrap';

import './header.scss';
const Header = () => {

    const [isOpen, setIsOpen] = React.useState(false);


    const Handletoggle = () => {
        setIsOpen(!isOpen);
    };
    const showMobilemenu = () => {
        document.getElementById('sidebarArea').classList.toggle('showSidebar');
    };



    return (
        <Navbar color="dark" dark expand="md" className="fix-header">
            <div className="d-flex align-items-center">
                <div className="d-lg-block d-none me-5 pe-3 logo">
                    <Link to="/starter" className="nav-link">
                        DNK
                    </Link>
                </div>

                <Button color="primary" className=" d-lg-none" onClick={() => showMobilemenu()}>
                    <i className="bi bi-list"></i>
                </Button>
            </div>
            <div className="hstack gap-2">
                <Button color="primary" size="sm" className="d-sm-block d-md-none" onClick={Handletoggle}>
                    {isOpen ? <i className="bi bi-x"></i> : <i className="bi bi-three-dots-vertical"></i>}
                </Button>
            </div>

            <div className="header-title">Trang quản trị viên</div>
            
            <Collapse navbar isOpen={isOpen}>
                
            </Collapse>
        </Navbar>
    );
};

export default Header;
