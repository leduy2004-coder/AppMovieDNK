import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
    Button,
    Collapse,
    Dropdown,
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
    Nav,
    NavItem,
    Navbar,
    NavbarBrand,
} from 'reactstrap';

import { ReactComponent as LogoWhite } from '~/assets/images/logos/materialprowhite.svg';
import user1 from '~/assets/images/users/user4.jpg';
import './header.scss';
import { UserNotify } from '~/components/store/NotifyContext';

const Header = () => {
    const navigate = useNavigate();
    const [isOpen, setIsOpen] = React.useState(false);

    const [dropdownOpen, setDropdownOpen] = React.useState(false);

    const { setInfoNotify } = UserNotify();

    const toggle = () => setDropdownOpen((prevState) => !prevState);
    const Handletoggle = () => {
        setIsOpen(!isOpen);
    };
    const showMobilemenu = () => {
        document.getElementById('sidebarArea').classList.toggle('showSidebar');
    };

    const handleLogout = () => {
        navigate('/login');
        localStorage.clear();
        setInfoNotify({
            content: 'Đăng xuất thành công !!',
            delay: 1500,
            isNotify: true,
            type: 'success',
        });
    };

    return (
        <Navbar color="dark" dark expand="md" className="fix-header">
            <div className="d-flex align-items-center">
                <div className="d-lg-block d-none me-5 pe-3 logo">
                    <Link to="/starter" className="nav-link">
                        DNK
                    </Link>
                </div>
                <NavbarBrand href="/">
                    <LogoWhite className=" d-lg-none" />
                </NavbarBrand>
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
