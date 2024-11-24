import { Card, CardBody, CardTitle, CardSubtitle, Table } from 'reactstrap';
import user1 from '~/assets/images/users/user5.jpg';



const ProjectTables = ({ data }) => {
    console.log(data)
    const tableData = data.map((item) => ({
        avatar: user1, 
        name: item.TenKhachHang,
        email: item.Email || '', 
        number: item.SoDienThoai, 
        tickets: item.TongSoVeMua, 
        budget: item.TongTien, 
    }));
    return (
        <div>
            <Card>
                <CardBody>
                    <CardTitle tag="h5">Tài khoản đặt vé nhiều nhất</CardTitle>

                    <Table className="no-wrap mt-3 align-middle" responsive borderless>
                        <thead>
                            <tr>
                                <th>Tên người dùng</th>
                                <th>Số điện thoại</th>
                                <th>Số vé đã đặt</th>
                                <th>Tổng tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tableData.map((tdata, index) => (
                                <tr key={index} className="border-top">
                                    <td>
                                        <div className="d-flex align-items-center p-2">
                                            <img
                                                src={tdata.avatar}
                                                className="rounded-circle"
                                                alt="avatar"
                                                width="45"
                                                height="45"
                                            />
                                            <div className="ms-3">
                                                <h6 className="mb-0">{tdata.name}</h6>
                                                <span className="text-muted">{tdata.email}</span>
                                            </div>
                                        </div>
                                    </td>
                                    <td>{tdata.number}</td>
                                   
                                    <td>{tdata.tickets}</td>
                                    <td>{tdata.budget}</td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </CardBody>
            </Card>
        </div>
    );
};

export default ProjectTables;
