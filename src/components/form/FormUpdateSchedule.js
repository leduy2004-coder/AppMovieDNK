import React, { useState, useRef, useEffect } from 'react';
import { Row, Col, Card, CardTitle, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import { UserAuth } from '~/components/store/AuthContext';
import { UserNotify } from '~/components/store/NotifyContext';
import './form.scss';
import config from '~/services';


const UpdateScheduleForm = ({ schedule, schedules, onSchedulesUpdate }) => {
    const { setInfoNotify } = UserNotify();
    const { setOpenFormAddSchedule } = UserAuth();
    const [scheduleDetails, setScheduleDetails] = useState({
        maPhim: '',
        maPhong: '',
        maCa: '',
        ngayChieu: '',
        tinhTrang: ''
    });
    const [listMovieTitle, setListMovieTitle] = useState([]);
    const [listRoom, setListRoom] = useState([]);
    const [listShift, setListShift] = useState([]);
    const [date, setDate] = useState();
    const [shift, setShift] = useState();

    useEffect(() => {
        const mappedData = {
            maPhim: schedule.maPhim || '',
            maPhong: schedule.maPhong || '',
            maCa: schedule.maCa || '',
            ngayChieu: schedule.ngayChieu && !isNaN(Date.parse(schedule.ngayChieu))
                ? new Date(schedule.ngayChieu).toISOString().split('T')[0]
                : '',
            tinhTrang: schedule.tinhTrang || '',
        };

        setScheduleDetails(mappedData);

        // lấy tất cả tên phim
        const fetchMovieTitle = async () => {

            try {
                const data = await config.getAllMovies();
                if (data.errCode) {
                    setInfoNotify({
                        content: 'Lỗi dữ liệu !!',
                        delay: 1500,
                        isNotify: true,
                        type: 'error',
                    });
                } else {
                    setListMovieTitle(data); // Set data for combobox
                }
            } catch (error) {
                setInfoNotify({
                    content: 'Lỗi khi lấy dữ liệu từ server !! getAllMovies',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            }
        };

        fetchMovieTitle();

    }, [schedule]);

    useEffect(() => {
        // lấy tất cả ca chiếu còn trống trong ngày
        const fetchRoom = async () => {
            if (!date) return;
            try {
                const data = await config.getAllShift(date);
                console.log("API Response:", data);
                if (data.errCode) {
                    setInfoNotify({
                        content: 'Lỗi dữ liệu !!',
                        delay: 1500,
                        isNotify: true,
                        type: 'error',
                    });
                } else {
                    setListShift(data); // Thiết lập dữ liệu cho combobox
                }
            } catch (error) {
                setInfoNotify({
                    content: 'Lỗi khi lấy dữ liệu từ server !! getAllShift',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            }
        };

        fetchRoom();
    }, [date]);
    useEffect(() => {
        // lấy tất cả phòng chiếu còn trống 
        const fetchShift = async () => {
            try {
                const data = await config.getAllRoom(date, shift);
                console.log("API Response:", data);
                if (data.errCode) {
                    setInfoNotify({
                        content: 'Lỗi dữ liệu !!',
                        delay: 1500,
                        isNotify: true,
                        type: 'error',
                    });
                } else {
                    setListRoom(data); // Thiết lập dữ liệu cho combobox
                }
            } catch (error) {
                setInfoNotify({
                    content: 'Lỗi khi lấy dữ liệu từ server !! getAllRoom',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            }
        };

        if (date && shift) {
            fetchShift();
        }
    }, [date, shift]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setScheduleDetails((prev) => ({ ...prev, [name]: value }));
    };

    const handleChangeDate = (e) => {
        const selectedDate = e.target.value;
        setDate(selectedDate);
        setScheduleDetails((prev) => ({ ...prev, ngayChieu: selectedDate }));
    };
    const handleChangeShift = (e) => {
        const selectedShift = e.target.value;
        setShift(selectedShift);
        setScheduleDetails((prev) => ({ ...prev, maCa: selectedShift }));
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            let data;
            let actionMessage = '';

            data = await config.insertSchedule(scheduleDetails);
            actionMessage = 'Thêm suất chiếu thành công !!';

            if (data.errCode) {
                setInfoNotify({
                    content: 'Lỗi dữ liệu !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            } else {
                // Cập nhật danh sách 
                onSchedulesUpdate(true);
                setOpenFormAddSchedule();
                setInfoNotify({
                    content: actionMessage,
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
        }
    };

    const handleClose = () => {
        setOpenFormAddSchedule(false); // Close the form
    };



    return (
        <div className="background-form">
            <Row>
                <Col>
                    <Card className="form-card">
                        <div className="form-content">
                            <div className="area-close">
                                <span className="close-btn" onClick={handleClose} role="button" aria-label="Close">
                                    &times;
                                </span>
                            </div>
                            <CardTitle tag="h4" className="text-center mb-4" style={{ color: 'red' }}>
                                {schedule.tenPhim ? 'Cập nhật lịch chiếu' : 'Thêm mới lịch chiếu'}
                            </CardTitle>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    {/* Cột 1 */}
                                    <Col md={12} >
                                        {/* Movie Title */}
                                        <FormGroup>
                                            <Label for="maPhim">Tên phim</Label>
                                            <Input
                                                type="select"
                                                id="maPhim"
                                                name="maPhim"
                                                value={scheduleDetails.maPhim}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">-- Chọn phim --</option>
                                                {Array.isArray(listMovieTitle) && listMovieTitle.length > 0 ? (
                                                    listMovieTitle.map((type) => (
                                                        <option key={type.maPhim} value={type.maPhim}>
                                                            {type.tenPhim}
                                                        </option>
                                                    ))
                                                ) : (
                                                    <option disabled>No movies available</option>
                                                )}
                                            </Input>
                                        </FormGroup>

                                        {/* Release Date */}
                                        <FormGroup>
                                            <Label for="ngayChieu">Ngày phát hành</Label>
                                            <Input
                                                type="date"
                                                id="ngayChieu"
                                                name="ngayChieu"
                                                value={scheduleDetails.ngayChieu}
                                                onChange={handleChangeDate}
                                                required
                                            />
                                        </FormGroup>

                                        {/* Age Requirement */}
                                        <FormGroup>
                                            <Label for="maCa">Ca chiếu</Label>
                                            <Input
                                                type="select"
                                                id="maCa"
                                                name="maCa"
                                                value={scheduleDetails.maCa}
                                                onChange={handleChangeShift}
                                                required
                                            >
                                                <option value="">-- Chọn ca chiếu --</option>
                                                {Array.isArray(listShift) && listShift.length > 0 ? (
                                                    listShift.map((type) => (
                                                        <option key={type.maCa} value={type.maCa}>
                                                            {type.tenCa}
                                                        </option>
                                                    ))
                                                ) : (
                                                    <option disabled>Vui lòng chọn ngày</option>
                                                )}
                                            </Input>
                                        </FormGroup>

                                        {/* Director */}
                                        <FormGroup>
                                            <Label for="maPhong">Phòng chiếu</Label>
                                            <Input
                                                type="select"
                                                id="maPhong"
                                                name="maPhong"
                                                value={scheduleDetails.maPhong}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">-- Chọn phòng chiếu --</option>
                                                {Array.isArray(listRoom) && listRoom.length > 0 ? (
                                                    listRoom.map((type) => (
                                                        <option key={type.maPhong} value={type.maPhong}>
                                                            {type.maPhong}
                                                        </option>
                                                    ))
                                                ) : (
                                                    <option disabled>Vui lòng chọn phim và ca</option>
                                                )}
                                            </Input>

                                        </FormGroup>



                                        <FormGroup>
                                            <Label for="tinhTrang">Tình trạng</Label>
                                            <Input
                                                type="select"
                                                id="tinhTrang"
                                                name="tinhTrang"
                                                value={scheduleDetails.tinhTrang}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">-- Chọn tình trạng --</option>
                                                <option value="1">1 - Được chiếu</option>
                                                <option value="0">0 - Ngừng chiếu</option>
                                            </Input>
                                        </FormGroup>
                                    </Col>
                                </Row>



                                <div className="area-btn-submit">
                                    <Button className="btn-submit" type="submit">
                                        {schedule.tenPhim ? 'Cập nhật lịch chiếu' : 'Thêm mới lịch chiếu'}
                                    </Button>
                                </div>
                            </Form>
                        </div>
                    </Card>
                </Col>
            </Row>
        </div>
    );
};

export default UpdateScheduleForm;
