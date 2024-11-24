import React, { useState } from 'react';
import { Button, Nav, NavItem } from 'reactstrap';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import user1 from '../assets/images/users/user4.jpg';
import probg from '../assets/images/bg/download.jpg';
import { UserNotify } from '~/components/store/NotifyContext';
import { UserAuth } from '~/components/store/AuthContext';
const navigation = [
    {
        title: 'Thống kê',
        href: '/starter',
        icon: 'bi bi-speedometer2',
    },
    {
        title: 'Quản lí phim',
        href: '/movie',
        icon: 'bi bi-layout-split',
    },
    {
        title: 'Quản lí lịch chiếu',
        href: '/badges',
        icon: 'bi bi-columns',
    },
    
    {
        title: 'Thông tin',
        href: '/about',
        icon: 'bi bi-people',
    },
];

const Sidebar = () => {
    const navigate = useNavigate();
    const showMobilemenu = () => {
        document.getElementById('sidebarArea').classList.toggle('showSidebar');
    };
    let location = useLocation();
    const { setInfoNotify } = UserNotify();
    const { userAuth } = UserAuth();

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
        <div>
            <div className="d-flex align-items-center"></div>
            <div className="profilebg" style={{ background: `url(${probg}) no-repeat` }}>
                <div className="p-3 d-flex">
                    <img src={user1} alt="user" width="50" className="rounded-circle" />
                    <Button color="white" className="ms-auto text-white d-lg-none" onClick={() => showMobilemenu()}>
                        <i className="bi bi-x"></i>
                    </Button>
                </div>
                <div className="bg-dark text-white p-2 opacity-75">{userAuth.hoTen}</div>
            </div>
            <div className="p-3 mt-2">
                <Nav vertical className="sidebarNav">
                    {navigation.map((navi, index) => (
                        <NavItem key={index} className="sidenav-bg">
                            <Link
                                to={navi.href}
                                className={
                                    location.pathname === navi.href
                                        ? 'active nav-link py-3'
                                        : 'nav-link text-secondary py-3'
                                }
                            >
                                <i className={navi.icon}></i>
                                <span className="ms-3 d-inline-block">{navi.title}</span>
                            </Link>
                        </NavItem>
                    ))}
                    <Button color="danger" tag="a" target="_blank" className="mt-3" onClick={handleLogout}>
                        Đăng xuất
                    </Button>
                </Nav>
            </div>
        </div>
    );
};

export default Sidebar;
