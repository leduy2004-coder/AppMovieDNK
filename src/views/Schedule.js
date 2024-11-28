import React, { useState, useEffect } from 'react';
import {
    Row, Col, Button, Input, InputGroup, InputGroupText, Table, Card, CardTitle, CardBodyModal,
    ModalHeader,
    ModalBody,
    ModalFooter, CardBody, Modal,
} from 'reactstrap';
import { FaPlus, FaEdit, FaTrash } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import config from '~/services';
import { UserNotify } from '~/components/store/NotifyContext';
import { UserAuth } from '~/components/store/AuthContext';
import AddScheduleForm from '~/components/form/FormUpdateSchedule';




const Schedule = () => {
    const [schedules, setSchedules] = useState([]);
    const [search, setSearch] = useState('');
    const { setInfoNotify } = UserNotify();
    const { openFormAddSchedule, setOpenFormAddSchedule } = UserAuth();
    const [scheduleToDelete, setScheduleToDelete] = useState(null); // Lưu thông tin phim cần xóa
    const [showConfirmDelete, setShowConfirmDelete] = useState(false); // Điều khiển việc hiển thị modal xác nhận

    const [movieTitles, setmovieTitles] = useState([]);
    const [rooms, setrooms] = useState([]);
    const [shows, setshows] = useState([]);

    const [schedule, setSchedule] = useState({
        maSuat: '',
        maPhim: '',
        maCa: '',
        maPhong: '',
        tenPhim: '',
        tenCa: '',
        ngayChieu: '',
        tinhTrang: ''
    });


    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await config.getAllSchedule();


                setSchedules(data);
                console.log(data);

            } catch (error) {
                console.log(error)
                setInfoNotify({
                    content: 'Lỗi khi lấy dữ liệu từ server getAllSchedule!!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            }
        };

        fetchData();
    }, []);
    // Hàm xử lý tìm kiếm
    const handleSearch = (e) => {
        setSearch(e.target.value);
    };

    const handleViewDetails = (maSuat, tenPhim, maPhong, tenCa, ngayChieu, tinhTrang, maPhim, maCa) => {
        setSchedule({ maSuat, tenPhim, maPhong, tenCa, ngayChieu, tinhTrang, maPhim, maCa });

        setOpenFormAddSchedule(true);
        // Dữ liệu sẽ được chuyển sang form cập nhật lịch chiếu
    };

    const handOpenFormAdd = () => {
        setSchedule('');
        setOpenFormAddSchedule(true);
    };

    // Hàm mở modal xác nhận xóa
    const handleOpenConfirmDelete = (maSuat) => {
        setScheduleToDelete(maSuat);
        setShowConfirmDelete(true);
    };
    // Hàm xóa phim
    const handRemoveMovie = async () => {
        if (!scheduleToDelete) return;

        try {
            const data = await config.removeSchedule(scheduleToDelete);

            if (data.errCode) {
                setInfoNotify({
                    content: 'Lỗi dữ liệu !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            } else {
                // Lọc bỏ phim bị xóa khỏi danh sách
                const updatedMovies = schedules.filter((schedule) => schedule.maSuat !== scheduleToDelete);
                setSchedules(updatedMovies);
                setInfoNotify({
                    content: 'Lịch chiếu đã được xóa thành công.',
                    delay: 1500,
                    isNotify: true,
                    type: 'success',
                });
            }
        } catch (error) {
            setInfoNotify({
                content: 'Lỗi khi lấy dữ liệu từ server !!',
                delay: 1500,
                isNotify: true,
                type: 'error',
            });
        } finally {
            setShowConfirmDelete(false); // Đóng modal xác nhận sau khi thực hiện
            setScheduleToDelete(null); // Reset thông tin phim cần xóa
        }
    };

    // Lọc danh sách lịch chiếu theo tìm kiếm
    const filteredSchedules = schedules.filter((schedule) =>
        schedule.tenPhim.toLowerCase().includes(search.toLowerCase()),
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
                        <Button color="primary" onClick={handOpenFormAdd}>
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
                                    <tr key={schedule.id || index}>
                                        <th scope="row">{index + 1}</th>
                                        <td>{schedule.tenPhim}</td>
                                        <td>{schedule.maPhong}</td>
                                        <td>{schedule.tenCa}</td>
                                        <td>{schedule.ngayChieu}</td>
                                        <td>
                                            <span
                                                className={`badge ${schedule.tinhTrang == '1' ? 'bg-success' : 'bg-danger'
                                                    }`}
                                            >
                                                {schedule.tinhTrang}
                                                {schedule.tinhTrang == '1' ? 'Available' : 'Sold out'}
                                            </span>
                                        </td>
                                        <td>
                                            <Button color="warning" size="sm" className="me-2" onClick={() => handleViewDetails(schedule.maSuat, schedule.maPhim, schedule.maCa, schedule.tenPhim, schedule.maPhong, schedule.tenCa, schedule.ngayChieu, schedule.tinhTrang)}>
                                                <FaEdit className="me-1" />
                                                Chỉnh sửa
                                            </Button>
                                            <Button color="danger" size="sm" onClick={() => handleOpenConfirmDelete(schedule.maSuat)}>
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

            {openFormAddSchedule && (
                <AddScheduleForm
                    schedule={schedule}

                    onClose={() => setOpenFormAddSchedule(false)} // Đóng form khi cần
                />
            )}
            {/* Modal xác nhận xóa */}
            <Modal isOpen={showConfirmDelete} toggle={() => setShowConfirmDelete(false)}>
                <ModalHeader toggle={() => setShowConfirmDelete(false)}>Xác nhận xóa lịch chiếu</ModalHeader>
                <ModalBody>Bạn có chắc chắn muốn xóa lịch chiếu này?</ModalBody>
                <ModalFooter>
                    <Button color="secondary" onClick={() => setShowConfirmDelete(false)}>
                        Hủy
                    </Button>
                    <Button color="danger" onClick={handRemoveMovie}>
                        Xóa
                    </Button>
                </ModalFooter>
            </Modal>
        </Row>
    );
};

export default Schedule;
