import React, { useState } from 'react';
import { Row, Col, Button, Input, InputGroup, InputGroupText, Table, Card, CardTitle, CardBody } from 'reactstrap';
import { FaPlus, FaEdit, FaTrash } from 'react-icons/fa';

// Dữ liệu mẫu
const initialSchedules = [
    {
        id: 1,
        movieName: 'Movie 1',
        room: 'Room A',
        showTime: '10:00 AM',
        showDate: '2024-12-01',
        status: 'Available',
    },
    {
        id: 2,
        movieName: 'Movie 2',
        room: 'Room B',
        showTime: '02:00 PM',
        showDate: '2024-12-02',
        status: 'Full',
    },
    {
        id: 3,
        movieName: 'Movie 3',
        room: 'Room C',
        showTime: '06:00 PM',
        showDate: '2024-12-03',
        status: 'Available',
    },
    // Thêm dữ liệu mẫu...
];

const Schedule = () => {
    const [schedules, setSchedules] = useState(initialSchedules);
    const [search, setSearch] = useState('');

    // Hàm xử lý tìm kiếm
    const handleSearch = (e) => {
        setSearch(e.target.value);
    };

    // Lọc danh sách lịch chiếu theo tìm kiếm
    const filteredSchedules = schedules.filter((schedule) =>
        schedule.movieName.toLowerCase().includes(search.toLowerCase()),
    );

    return (
        <Row>
            <Col lg="12" className="mb-4">
                <Row>
                    <Col md="9">
                        <InputGroup>
                            <Input placeholder="Tìm kiếm theo tên phim..." value={search} onChange={handleSearch} />
                            <InputGroupText>
                                <i className="bi bi-search"></i>
                            </InputGroupText>
                        </InputGroup>
                    </Col>
                    <Col md="3" className="text-end">
                        <Button color="primary">
                            <FaPlus className="me-2" />
                            Thêm lịch chiếu
                        </Button>
                    </Col>
                </Row>
            </Col>

            <Col lg="12">
                <Card>
                    <CardTitle tag="h6" className="border-bottom p-3 mb-0">
                        <i className="bi bi-card-text me-2"> </i>
                        Lịch Chiếu Phim
                    </CardTitle>
                    <CardBody className="">
                        <Table bordered hover>
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Tên phim</th>
                                    <th>Phòng chiếu</th>
                                    <th>Ca chiếu</th>
                                    <th>Ngày chiếu</th>
                                    <th>Tình trạng</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredSchedules.map((schedule, index) => (
                                    <tr key={schedule.id}>
                                        <th scope="row">{index + 1}</th>
                                        <td>{schedule.movieName}</td>
                                        <td>{schedule.room}</td>
                                        <td>{schedule.showTime}</td>
                                        <td>{schedule.showDate}</td>
                                        <td>
                                            <span
                                                className={`badge ${
                                                    schedule.status === 'Available' ? 'bg-success' : 'bg-danger'
                                                }`}
                                            >
                                                {schedule.status}
                                            </span>
                                        </td>
                                        <td>
                                            <Button color="warning" size="sm" className="me-2">
                                                <FaEdit className="me-1" />
                                                Chỉnh sửa
                                            </Button>
                                            <Button color="danger" size="sm">
                                                <FaTrash className="me-1" />
                                                Xóa
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    </CardBody>
                </Card>
            </Col>
        </Row>
    );
};

export default Schedule;
