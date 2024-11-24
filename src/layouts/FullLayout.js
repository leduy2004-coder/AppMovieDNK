import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import Header from './header/Header';
import { Container } from 'reactstrap';
import Notify from '~/components/Notify';

const FullLayout = () => {
    return (
        <main>
            <Header />
            <div className="pageWrapper d-lg-flex">
                <aside className="sidebarArea shadow" id="sidebarArea">
                    <Sidebar />
                </aside>
                <div className="contentArea">
                    <Container className="p-4" fluid>
                        <Outlet />
                    </Container>
                </div>
            </div>

            <Notify/>
        </main>
    );
};

export default FullLayout;
